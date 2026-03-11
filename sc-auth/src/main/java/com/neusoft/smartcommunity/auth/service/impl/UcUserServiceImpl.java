package com.neusoft.smartcommunity.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.smartcommunity.auth.entity.UcUser;
import com.neusoft.smartcommunity.auth.mapper.UcUserMapper;
import com.neusoft.smartcommunity.auth.service.UcUserService;
import org.springframework.stereotype.Service;

/**
 * 用户账号服务实现。
 */
@Service
public class UcUserServiceImpl extends ServiceImpl<UcUserMapper, UcUser> implements UcUserService {

    @Override
    public UcUser findByPhone(String phone) {
        return this.getOne(new LambdaQueryWrapper<UcUser>()
                .eq(UcUser::getPhone, phone)
                .last("LIMIT 1"));
    }
}

