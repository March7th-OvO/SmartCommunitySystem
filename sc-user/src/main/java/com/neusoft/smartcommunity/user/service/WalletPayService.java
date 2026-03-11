package com.neusoft.smartcommunity.user.service;

import java.math.BigDecimal;

/**
 * 钱包支付服务：扣减、退款（供 sc-trade 等调用）。
 */
public interface WalletPayService {

    /**
     * 扣减用户余额（订单支付等）。
     *
     * @param userId  用户ID
     * @param amount  扣减金额
     * @param bizType 业务类型，如 ORDER_PAY
     * @param bizId   业务ID，如订单ID
     * @param remark  备注
     * @throws com.neusoft.smartcommunity.common.exception.BusinessException 余额不足等
     */
    void deduct(Long userId, BigDecimal amount, String bizType, Long bizId, String remark);

    /**
     * 退款（取消订单等）：增加用户余额并记流水。
     *
     * @param userId  用户ID
     * @param amount  退款金额
     * @param bizType 业务类型，如 ORDER_REFUND
     * @param bizId   业务ID
     * @param remark  备注
     */
    void refund(Long userId, BigDecimal amount, String bizType, Long bizId, String remark);
}
