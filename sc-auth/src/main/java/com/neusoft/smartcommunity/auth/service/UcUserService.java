package com.neusoft.smartcommunity.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.smartcommunity.auth.entity.UcUser;

/**
 * 用户账号服务接口。
 */
public interface UcUserService extends IService<UcUser> {

    /**
     * 根据手机号查询用户。
     *
     * @param phone 手机号
     * @return 用户实体或 null
     */
    UcUser findByPhone(String phone);
}

