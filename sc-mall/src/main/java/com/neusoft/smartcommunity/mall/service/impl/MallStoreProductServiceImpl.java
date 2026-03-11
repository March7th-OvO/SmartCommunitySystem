package com.neusoft.smartcommunity.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.smartcommunity.common.api.ResultCode;
import com.neusoft.smartcommunity.common.exception.BusinessException;
import com.neusoft.smartcommunity.mall.entity.MallStoreProduct;
import com.neusoft.smartcommunity.mall.mapper.MallStoreProductMapper;
import com.neusoft.smartcommunity.mall.service.MallStoreProductService;
import org.springframework.stereotype.Service;

import java.util.List;

/** 门店商品及库存服务实现 */
@Service
public class MallStoreProductServiceImpl extends ServiceImpl<MallStoreProductMapper, MallStoreProduct> implements MallStoreProductService {

    @Override
    public List<MallStoreProduct> listByStoreId(Long storeId) {
        return list(new LambdaQueryWrapper<MallStoreProduct>()
                .eq(MallStoreProduct::getStoreId, storeId)
                .eq(MallStoreProduct::getStatus, 1));
    }

    @Override
    public List<MallStoreProduct> listByProductId(Long productId) {
        return list(new LambdaQueryWrapper<MallStoreProduct>()
                .eq(MallStoreProduct::getProductId, productId));
    }

    @Override
    public boolean lockStock(Long storeId, Long productId, int quantity) {
        return baseMapper.lockStock(storeId, productId, quantity) > 0;
    }

    @Override
    public void unlockStock(Long storeId, Long productId, int quantity) {
        baseMapper.unlockStock(storeId, productId, quantity);
    }

    @Override
    public void confirmStock(Long storeId, Long productId, int quantity) {
        int rows = baseMapper.confirmStock(storeId, productId, quantity);
        if (rows <= 0) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "确认扣减库存失败，locked_stock 不足");
        }
    }
}
