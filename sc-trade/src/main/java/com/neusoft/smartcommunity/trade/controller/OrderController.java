package com.neusoft.smartcommunity.trade.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.smartcommunity.common.api.Result;
import com.neusoft.smartcommunity.common.exception.BusinessException;
import com.neusoft.smartcommunity.common.api.ResultCode;
import com.neusoft.smartcommunity.trade.dto.CreateOrderRequest;
import com.neusoft.smartcommunity.trade.entity.TradeOrder;
import com.neusoft.smartcommunity.trade.entity.TradeOrderItem;
import com.neusoft.smartcommunity.trade.mapper.TradeOrderItemMapper;
import com.neusoft.smartcommunity.trade.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/** 订单接口 */
@RestController
@RequestMapping("/trade/order")
public class OrderController {

    private final OrderService orderService;
    private final TradeOrderItemMapper orderItemMapper;

    public OrderController(OrderService orderService, TradeOrderItemMapper orderItemMapper) {
        this.orderService = orderService;
        this.orderItemMapper = orderItemMapper;
    }

    private Long getUserId(HttpServletRequest request) {
        String id = request.getHeader("X-User-Id");
        if (id == null) throw new BusinessException(ResultCode.UNAUTHORIZED, "未登录");
        return Long.parseLong(id);
    }

    @PostMapping("/create")
    public Result<Long> create(HttpServletRequest request, @Valid @RequestBody CreateOrderRequest body) {
        Long orderId = orderService.createOrder(getUserId(request), body);
        return Result.success(orderId);
    }

    @PostMapping("/{orderId}/pay")
    public Result<Void> pay(HttpServletRequest request, @PathVariable Long orderId) {
        orderService.payOrder(getUserId(request), orderId);
        return Result.success();
    }

    @PostMapping("/{orderId}/cancel")
    public Result<Void> cancel(HttpServletRequest request, @PathVariable Long orderId,
                               @RequestParam(required = false) String reason) {
        Long userId = getUserId(request);
        TradeOrder order = orderService.getById(orderId);
        if (order == null) throw new BusinessException(ResultCode.BUSINESS_ERROR, "订单不存在");
        if (!order.getUserId().equals(userId)) throw new BusinessException(ResultCode.FORBIDDEN, "无权操作");
        orderService.cancelOrder(orderId, reason != null ? reason : "用户取消");
        return Result.success();
    }

    @GetMapping("/{orderId}")
    public Result<TradeOrder> get(HttpServletRequest request, @PathVariable Long orderId) {
        TradeOrder order = orderService.getById(orderId);
        if (order == null) throw new BusinessException(ResultCode.BUSINESS_ERROR, "订单不存在");
        if (!order.getUserId().equals(getUserId(request))) throw new BusinessException(ResultCode.FORBIDDEN, "无权查看");
        return Result.success(order);
    }

    @GetMapping("/{orderId}/items")
    public Result<List<TradeOrderItem>> getItems(HttpServletRequest request, @PathVariable Long orderId) {
        TradeOrder order = orderService.getById(orderId);
        if (order == null) throw new BusinessException(ResultCode.BUSINESS_ERROR, "订单不存在");
        if (!order.getUserId().equals(getUserId(request))) throw new BusinessException(ResultCode.FORBIDDEN, "无权查看");
        List<TradeOrderItem> items = orderItemMapper.selectList(
                new LambdaQueryWrapper<TradeOrderItem>().eq(TradeOrderItem::getOrderId, orderId));
        return Result.success(items);
    }

    @PostMapping("/{orderId}/deliver")
    public Result<Void> deliver(@PathVariable Long orderId) {
        orderService.deliverOrder(orderId);
        return Result.success();
    }

    @PostMapping("/{orderId}/finish")
    public Result<Void> finish(HttpServletRequest request, @PathVariable Long orderId) {
        orderService.finishOrder(getUserId(request), orderId);
        return Result.success();
    }

    @GetMapping("/page")
    public Result<Page<TradeOrder>> page(HttpServletRequest request,
                                          @RequestParam(defaultValue = "1") int pageNum,
                                          @RequestParam(defaultValue = "10") int pageSize,
                                          @RequestParam(required = false) Integer orderStatus) {
        return Result.success(orderService.pageByUserId(getUserId(request), pageNum, pageSize, orderStatus));
    }

    /** 管理端：分页查询全部订单 */
    @GetMapping("/admin/page")
    public Result<Page<TradeOrder>> pageAdmin(HttpServletRequest request,
                                               @RequestParam(defaultValue = "1") int pageNum,
                                               @RequestParam(defaultValue = "10") int pageSize,
                                               @RequestParam(required = false) Integer orderStatus) {
        getUserId(request); // 要求已登录
        return Result.success(orderService.pageForAdmin(pageNum, pageSize, orderStatus));
    }
}
