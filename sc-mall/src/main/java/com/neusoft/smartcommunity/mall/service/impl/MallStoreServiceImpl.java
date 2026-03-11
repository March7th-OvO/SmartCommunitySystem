package com.neusoft.smartcommunity.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.smartcommunity.mall.entity.MallStore;
import com.neusoft.smartcommunity.mall.mapper.MallStoreMapper;
import com.neusoft.smartcommunity.mall.service.MallStoreService;
import org.springframework.stereotype.Service;

/** 门店服务实现 */
@Service
public class MallStoreServiceImpl extends ServiceImpl<MallStoreMapper, MallStore> implements MallStoreService {
}
