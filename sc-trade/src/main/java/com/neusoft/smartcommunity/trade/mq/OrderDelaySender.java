package com.neusoft.smartcommunity.trade.mq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * 发送订单超时延迟消息。使用 TTL + 死信队列：消息先进入 delay 队列，TTL 后进入 cancel 队列被消费。
 */
@Component
public class OrderDelaySender {

    private final RabbitTemplate rabbitTemplate;

    public OrderDelaySender(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 发送订单过期消息，TTL 分钟后进入取消队列。
     */
    public void sendOrderExpire(Long orderId, int ttlMinutes) {
        rabbitTemplate.convertAndSend(OrderDelayConfig.ORDER_DELAY_EXCHANGE, OrderDelayConfig.ORDER_DELAY_KEY,
                String.valueOf(orderId), msg -> {
                    msg.getMessageProperties().setExpiration(String.valueOf(ttlMinutes * 60 * 1000));
                    return msg;
                });
    }
}
