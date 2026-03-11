package com.neusoft.smartcommunity.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.neusoft.smartcommunity.common.api.ResultCode;
import com.neusoft.smartcommunity.common.exception.BusinessException;
import com.neusoft.smartcommunity.user.entity.PayWallet;
import com.neusoft.smartcommunity.user.entity.PayWalletFlow;
import com.neusoft.smartcommunity.user.mapper.PayWalletMapper;
import com.neusoft.smartcommunity.user.mapper.PayWalletFlowMapper;
import com.neusoft.smartcommunity.user.service.WalletPayService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class WalletPayServiceImpl implements WalletPayService {

    private final PayWalletMapper payWalletMapper;
    private final PayWalletFlowMapper payWalletFlowMapper;

    public WalletPayServiceImpl(PayWalletMapper payWalletMapper, PayWalletFlowMapper payWalletFlowMapper) {
        this.payWalletMapper = payWalletMapper;
        this.payWalletFlowMapper = payWalletFlowMapper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deduct(Long userId, BigDecimal amount, String bizType, Long bizId, String remark) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "扣减金额必须大于0");
        }
        PayWallet wallet = payWalletMapper.selectOne(new LambdaQueryWrapper<PayWallet>().eq(PayWallet::getUserId, userId).last("LIMIT 1"));
        if (wallet == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "钱包不存在");
        }
        if (wallet.getBalance() == null || wallet.getBalance().compareTo(amount) < 0) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "余额不足");
        }
        BigDecimal beforeBalance = wallet.getBalance();
        BigDecimal afterBalance = beforeBalance.subtract(amount);
        int rows = payWalletMapper.update(null, new LambdaUpdateWrapper<PayWallet>()
                .eq(PayWallet::getUserId, userId)
                .ge(PayWallet::getBalance, amount)
                .set(PayWallet::getBalance, afterBalance)
                .set(PayWallet::getUpdatedAt, LocalDateTime.now()));
        if (rows <= 0) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "余额不足或扣减失败");
        }
        PayWalletFlow flow = new PayWalletFlow();
        flow.setWalletId(wallet.getId());
        flow.setUserId(userId);
        flow.setBizType(bizType);
        flow.setBizId(bizId);
        flow.setDirection(PayWalletFlow.DIRECTION_OUT);
        flow.setAmount(amount);
        flow.setBeforeBalance(beforeBalance);
        flow.setAfterBalance(afterBalance);
        flow.setRemark(remark != null ? remark : bizType);
        flow.setCreatedAt(LocalDateTime.now());
        payWalletFlowMapper.insert(flow);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refund(Long userId, BigDecimal amount, String bizType, Long bizId, String remark) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "退款金额必须大于0");
        }
        PayWallet wallet = payWalletMapper.selectOne(new LambdaQueryWrapper<PayWallet>().eq(PayWallet::getUserId, userId).last("LIMIT 1"));
        if (wallet == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "钱包不存在");
        }
        BigDecimal beforeBalance = wallet.getBalance() != null ? wallet.getBalance() : BigDecimal.ZERO;
        BigDecimal afterBalance = beforeBalance.add(amount);
        wallet.setBalance(afterBalance);
        wallet.setUpdatedAt(LocalDateTime.now());
        payWalletMapper.updateById(wallet);
        PayWalletFlow flow = new PayWalletFlow();
        flow.setWalletId(wallet.getId());
        flow.setUserId(userId);
        flow.setBizType(bizType);
        flow.setBizId(bizId);
        flow.setDirection(PayWalletFlow.DIRECTION_IN);
        flow.setAmount(amount);
        flow.setBeforeBalance(beforeBalance);
        flow.setAfterBalance(afterBalance);
        flow.setRemark(remark != null ? remark : bizType);
        flow.setCreatedAt(LocalDateTime.now());
        payWalletFlowMapper.insert(flow);
    }
}
