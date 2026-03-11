package com.neusoft.smartcommunity.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.smartcommunity.auth.entity.UcUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户账号 Mapper。
 */
@Mapper
public interface UcUserMapper extends BaseMapper<UcUser> {
}

