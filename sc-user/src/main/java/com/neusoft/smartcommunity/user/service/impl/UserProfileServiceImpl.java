package com.neusoft.smartcommunity.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neusoft.smartcommunity.common.api.ResultCode;
import com.neusoft.smartcommunity.common.exception.BusinessException;
import com.neusoft.smartcommunity.user.entity.PayWallet;
import com.neusoft.smartcommunity.user.entity.UcUser;
import com.neusoft.smartcommunity.user.mapper.PayWalletMapper;
import com.neusoft.smartcommunity.user.mapper.UcUserMapper;
import com.neusoft.smartcommunity.user.service.UserProfileService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户资料与钱包服务实现。
 */
@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UcUserMapper ucUserMapper;
    private final PayWalletMapper payWalletMapper;
    private final PasswordEncoder passwordEncoder;

    public UserProfileServiceImpl(UcUserMapper ucUserMapper, PayWalletMapper payWalletMapper,
                                  PasswordEncoder passwordEncoder) {
        this.ucUserMapper = ucUserMapper;
        this.payWalletMapper = payWalletMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UcUser getUserById(Long userId) {
        UcUser user = ucUserMapper.selectById(userId);
        if (user == null || (user.getDeleted() != null && user.getDeleted() == 1)) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "用户不存在");
        }
        return user;
    }

    @Override
    public void updateProfile(UcUser user) {
        int updated = ucUserMapper.updateById(user);
        if (updated <= 0) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "更新用户资料失败");
        }
    }

    @Override
    public BigDecimal getWalletBalance(Long userId) {
        PayWallet wallet = payWalletMapper.selectOne(new LambdaQueryWrapper<PayWallet>()
                .eq(PayWallet::getUserId, userId)
                .last("LIMIT 1"));
        return wallet == null ? null : wallet.getBalance();
    }

    @Override
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        UcUser user = getUserById(userId);
        if (!passwordEncoder.matches(oldPassword, user.getPasswordHash())) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "旧密码错误");
        }
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        user.setUpdatedAt(LocalDateTime.now());
        ucUserMapper.updateById(user);
    }
}

