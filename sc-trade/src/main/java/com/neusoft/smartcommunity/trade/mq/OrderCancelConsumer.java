package com.neusoft.smartcommunity.trade.mq;

import com.neusoft.smartcommunity.trade.service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 消费订单超时取消消息：检查订单若仍为待支付则取消。
 */
@Component
public class OrderCancelConsumer {

    private final OrderService orderService;

    public OrderCancelConsumer(OrderService orderService) {
        this.orderService = orderService;
    }

    @RabbitListener(queues = OrderDelayConfig.ORDER_CANCEL_QUEUE)
    public void handleOrderCancel(String orderIdStr) {
        try {
            Long orderId = Long.parseLong(orderIdStr);
            orderService.tryCancelUnpaidOrder(orderId);
        } catch (Exception e) {
            // 记录日志，避免重复消费异常导致消息堆积
        }
    }
}
