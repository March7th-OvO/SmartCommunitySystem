package com.neusoft.smartcommunity.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.smartcommunity.common.api.ResultCode;
import com.neusoft.smartcommunity.common.exception.BusinessException;
import com.neusoft.smartcommunity.trade.client.MallStockClient;
import com.neusoft.smartcommunity.trade.client.UserWalletClient;
import com.neusoft.smartcommunity.trade.dto.CreateOrderRequest;
import com.neusoft.smartcommunity.trade.dto.OrderItemRequest;
import com.neusoft.smartcommunity.trade.dto.StoreProductInfoVo;
import com.neusoft.smartcommunity.trade.entity.TradeOrder;
import com.neusoft.smartcommunity.trade.entity.TradeOrderItem;
import com.neusoft.smartcommunity.trade.entity.TradeOrderStatusLog;
import com.neusoft.smartcommunity.trade.mapper.TradeOrderItemMapper;
import com.neusoft.smartcommunity.trade.mapper.TradeOrderMapper;
import com.neusoft.smartcommunity.trade.mapper.TradeOrderStatusLogMapper;
import com.neusoft.smartcommunity.trade.mq.OrderDelaySender;
import com.neusoft.smartcommunity.trade.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OrderServiceImpl implements OrderService {

    private final TradeOrderMapper orderMapper;
    private final TradeOrderItemMapper orderItemMapper;
    private final TradeOrderStatusLogMapper statusLogMapper;
    private final MallStockClient mallStockClient;
    private final UserWalletClient userWalletClient;
    private final OrderDelaySender orderDelaySender;

    @Value("${trade.order.unpaid-timeout-minutes:30}")
    private int unpaidTimeoutMinutes;

    public OrderServiceImpl(TradeOrderMapper orderMapper, TradeOrderItemMapper orderItemMapper,
                            TradeOrderStatusLogMapper statusLogMapper, MallStockClient mallStockClient,
                            UserWalletClient userWalletClient, OrderDelaySender orderDelaySender) {
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
        this.statusLogMapper = statusLogMapper;
        this.mallStockClient = mallStockClient;
        this.userWalletClient = userWalletClient;
        this.orderDelaySender = orderDelaySender;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOrder(Long userId, CreateOrderRequest request) {
        Long storeId = request.getStoreId();
        List<OrderItemRequest> items = request.getItems();
        List<StoreProductInfoVo> infos = new ArrayList<>();
        for (OrderItemRequest item : items) {
            StoreProductInfoVo info = mallStockClient.getStoreProductInfo(storeId, item.getProductId());
            infos.add(info);
        }
        for (int i = 0; i < items.size(); i++) {
            try {
                mallStockClient.lockStock(storeId, items.get(i).getProductId(), items.get(i).getQuantity());
            } catch (Exception e) {
                for (int j = 0; j < i; j++) {
                    mallStockClient.unlockStock(storeId, items.get(j).getProductId(), items.get(j).getQuantity());
                }
                throw new BusinessException(ResultCode.BUSINESS_ERROR, e.getMessage());
            }
        }
        String orderNo = "ORD" + System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(1000, 9999);
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<TradeOrderItem> orderItems = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            OrderItemRequest req = items.get(i);
            StoreProductInfoVo info = infos.get(i);
            BigDecimal itemTotal = info.getSalePrice().multiply(BigDecimal.valueOf(req.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
            TradeOrderItem oi = new TradeOrderItem();
            oi.setProductId(req.getProductId());
            oi.setProductName(info.getProductName());
            oi.setProductPic(info.getProductMainImage());
            oi.setStoreId(storeId);
            oi.setSalePrice(info.getSalePrice());
            oi.setQuantity(req.getQuantity());
            oi.setTotalAmount(itemTotal);
            orderItems.add(oi);
        }
        TradeOrder order = new TradeOrder();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setStoreId(storeId);
        order.setTotalAmount(totalAmount);
        order.setPayAmount(totalAmount);
        order.setPayStatus(TradeOrder.PAY_STATUS_UNPAID);
        order.setOrderStatus(TradeOrder.ORDER_STATUS_UNPAID);
        order.setDeliveryType(request.getDeliveryType());
        order.setReceiverName(request.getReceiverName());
        order.setReceiverPhone(request.getReceiverPhone());
        order.setReceiverAddress(request.getReceiverAddress());
        order.setRemark(request.getRemark());
        order.setExpireTime(LocalDateTime.now().plusMinutes(unpaidTimeoutMinutes));
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.insert(order);
        for (TradeOrderItem oi : orderItems) {
            oi.setOrderId(order.getId());
            oi.setCreatedAt(LocalDateTime.now());
            orderItemMapper.insert(oi);
        }
        orderDelaySender.sendOrderExpire(order.getId(), unpaidTimeoutMinutes);
        return order.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payOrder(Long userId, Long orderId) {
        TradeOrder order = orderMapper.selectById(orderId);
        if (order == null) throw new BusinessException(ResultCode.BUSINESS_ERROR, "订单不存在");
        if (!order.getUserId().equals(userId)) throw new BusinessException(ResultCode.FORBIDDEN, "无权操作");
        if (order.getOrderStatus() != TradeOrder.ORDER_STATUS_UNPAID) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "订单状态不允许支付");
        }
        userWalletClient.deduct(userId, order.getPayAmount(), "ORDER_PAY", orderId, "订单支付");
        order.setPayStatus(TradeOrder.PAY_STATUS_PAID);
        order.setOrderStatus(TradeOrder.ORDER_STATUS_PENDING_DELIVERY);
        order.setPayType(1);
        order.setPayTime(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateById(order);
        List<TradeOrderItem> items = orderItemMapper.selectList(new LambdaQueryWrapper<TradeOrderItem>().eq(TradeOrderItem::getOrderId, orderId));
        for (TradeOrderItem item : items) {
            mallStockClient.confirmStock(order.getStoreId(), item.getProductId(), item.getQuantity());
        }
        logStatus(orderId, TradeOrder.ORDER_STATUS_UNPAID, TradeOrder.ORDER_STATUS_PENDING_DELIVERY, userId, "USER", "用户支付");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderId, String reason) {
        TradeOrder order = orderMapper.selectById(orderId);
        if (order == null) return;
        if (order.getOrderStatus() == TradeOrder.ORDER_STATUS_CANCELLED) return;
        if (order.getPayStatus() == TradeOrder.PAY_STATUS_PAID) {
            userWalletClient.refund(order.getUserId(), order.getPayAmount(), "ORDER_REFUND", orderId, "订单取消退款");
        }
        List<TradeOrderItem> items = orderItemMapper.selectList(new LambdaQueryWrapper<TradeOrderItem>().eq(TradeOrderItem::getOrderId, orderId));
        for (TradeOrderItem item : items) {
            mallStockClient.unlockStock(order.getStoreId(), item.getProductId(), item.getQuantity());
        }
        int fromStatus = order.getOrderStatus();
        order.setOrderStatus(TradeOrder.ORDER_STATUS_CANCELLED);
        order.setCancelReason(reason);
        order.setCancelTime(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateById(order);
        logStatus(orderId, fromStatus, TradeOrder.ORDER_STATUS_CANCELLED, null, "SYSTEM", reason);
    }

    @Override
    public TradeOrder getById(Long orderId) {
        return orderMapper.selectById(orderId);
    }

    @Override
    public Page<TradeOrder> pageByUserId(Long userId, int pageNum, int pageSize, Integer orderStatus) {
        LambdaQueryWrapper<TradeOrder> w = new LambdaQueryWrapper<TradeOrder>()
                .eq(TradeOrder::getUserId, userId)
                .eq(orderStatus != null, TradeOrder::getOrderStatus, orderStatus)
                .orderByDesc(TradeOrder::getId);
        return orderMapper.selectPage(new Page<>(pageNum, pageSize), w);
    }

    @Override
    public Page<TradeOrder> pageForAdmin(int pageNum, int pageSize, Integer orderStatus) {
        LambdaQueryWrapper<TradeOrder> w = new LambdaQueryWrapper<TradeOrder>()
                .eq(orderStatus != null, TradeOrder::getOrderStatus, orderStatus)
                .orderByDesc(TradeOrder::getId);
        return orderMapper.selectPage(new Page<>(pageNum, pageSize), w);
    }

    @Override
    public void tryCancelUnpaidOrder(Long orderId) {
        TradeOrder order = orderMapper.selectById(orderId);
        if (order == null) return;
        if (order.getOrderStatus() == TradeOrder.ORDER_STATUS_UNPAID) {
            cancelOrder(orderId, "超时未支付自动取消");
        }
    }

    @Override
    public void deliverOrder(Long orderId) {
        TradeOrder order = orderMapper.selectById(orderId);
        if (order == null) throw new BusinessException(ResultCode.BUSINESS_ERROR, "订单不存在");
        if (order.getOrderStatus() != TradeOrder.ORDER_STATUS_PENDING_DELIVERY) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "当前状态不允许发货");
        }
        int from = order.getOrderStatus();
        order.setOrderStatus(TradeOrder.ORDER_STATUS_PENDING_PICKUP);
        order.setDeliveryTime(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateById(order);
        logStatus(orderId, from, TradeOrder.ORDER_STATUS_PENDING_PICKUP, null, "MERCHANT", "商家发货");
    }

    @Override
    public void finishOrder(Long userId, Long orderId) {
        TradeOrder order = orderMapper.selectById(orderId);
        if (order == null) throw new BusinessException(ResultCode.BUSINESS_ERROR, "订单不存在");
        if (!order.getUserId().equals(userId)) throw new BusinessException(ResultCode.FORBIDDEN, "无权操作");
        if (order.getOrderStatus() != TradeOrder.ORDER_STATUS_PENDING_PICKUP) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "当前状态不允许确认完成");
        }
        int from = order.getOrderStatus();
        order.setOrderStatus(TradeOrder.ORDER_STATUS_FINISHED);
        order.setFinishTime(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        orderMapper.updateById(order);
        logStatus(orderId, from, TradeOrder.ORDER_STATUS_FINISHED, userId, "USER", "用户确认完成");
    }

    private void logStatus(Long orderId, Integer from, Integer to, Long operateUserId, String role, String remark) {
        TradeOrderStatusLog log = new TradeOrderStatusLog();
        log.setOrderId(orderId);
        log.setFromStatus(from);
        log.setToStatus(to);
        log.setOperateUserId(operateUserId);
        log.setOperateRole(role);
        log.setRemark(remark);
        log.setCreatedAt(LocalDateTime.now());
        statusLogMapper.insert(log);
    }
}
