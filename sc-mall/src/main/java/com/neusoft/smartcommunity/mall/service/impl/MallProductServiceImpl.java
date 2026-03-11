package com.neusoft.smartcommunity.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.smartcommunity.mall.entity.MallProduct;
import com.neusoft.smartcommunity.mall.mapper.MallProductMapper;
import com.neusoft.smartcommunity.mall.service.MallProductService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/** 商品 SPU 服务实现 */
@Service
public class MallProductServiceImpl extends ServiceImpl<MallProductMapper, MallProduct> implements MallProductService {

    @Override
    public Page<MallProduct> pageList(int pageNum, int pageSize, Long categoryId, Integer status, String keyword) {
        LambdaQueryWrapper<MallProduct> w = new LambdaQueryWrapper<MallProduct>()
                .eq(categoryId != null, MallProduct::getCategoryId, categoryId)
                .eq(status != null, MallProduct::getStatus, status)
                .and(StringUtils.hasText(keyword), q -> q
                        .like(MallProduct::getName, keyword)
                        .or().like(MallProduct::getSubTitle, keyword))
                .orderByDesc(MallProduct::getId);
        return page(new Page<>(pageNum, pageSize), w);
    }
}
