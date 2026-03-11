package com.neusoft.smartcommunity.trade.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.smartcommunity.trade.dto.CreateOrderRequest;
import com.neusoft.smartcommunity.trade.entity.TradeOrder;

/** 订单服务 */
public interface OrderService {

    /**
     * 创建订单：锁库存、写订单及明细、发延迟消息。
     * @return 订单ID
     */
    Long createOrder(Long userId, CreateOrderRequest request);

    /** 钱包支付订单 */
    void payOrder(Long userId, Long orderId);

    /** 取消订单（用户主动或超时）：解锁库存，若已支付则退款 */
    void cancelOrder(Long orderId, String reason);

    TradeOrder getById(Long orderId);

    Page<TradeOrder> pageByUserId(Long userId, int pageNum, int pageSize, Integer orderStatus);

    /** 管理端：分页查询全部订单（不按用户过滤） */
    Page<TradeOrder> pageForAdmin(int pageNum, int pageSize, Integer orderStatus);

    /** 超时未支付检查（由延迟消息消费者调用） */
    void tryCancelUnpaidOrder(Long orderId);

    /** 发货（管理端）：待发货 -> 待取货 */
    void deliverOrder(Long orderId);

    /** 确认完成（用户取货后）：待取货 -> 已完成 */
    void finishOrder(Long userId, Long orderId);
}
