package com.neusoft.smartcommunity.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.smartcommunity.user.entity.UcUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户账号 Mapper（用户服务侧）。
 */
@Mapper
public interface UcUserMapper extends BaseMapper<UcUser> {
}

