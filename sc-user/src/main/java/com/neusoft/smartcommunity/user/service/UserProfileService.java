package com.neusoft.smartcommunity.user.service;

import com.neusoft.smartcommunity.user.entity.UcUser;

import java.math.BigDecimal;

/**
 * 用户资料与钱包服务接口。
 */
public interface UserProfileService {

    /**
     * 根据用户ID查询用户基础信息。
     *
     * @param userId 用户ID
     * @return 用户实体
     */
    UcUser getUserById(Long userId);

    /**
     * 更新用户资料（昵称、头像、性别）。
     *
     * @param user 用户实体
     */
    void updateProfile(UcUser user);

    /**
     * 查询用户钱包可用余额。
     *
     * @param userId 用户ID
     * @return 钱包余额，可能为 null
     */
    BigDecimal getWalletBalance(Long userId);

    /**
     * 修改当前用户密码（校验旧密码后更新）。
     *
     * @param userId    用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void changePassword(Long userId, String oldPassword, String newPassword);
}

