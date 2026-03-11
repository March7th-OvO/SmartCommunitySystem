package com.neusoft.smartcommunity.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.neusoft.smartcommunity.mall.entity.MallStoreProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/** 门店商品及库存 Mapper */
@Mapper
public interface MallStoreProductMapper extends BaseMapper<MallStoreProduct> {

    /** 锁定库存：stock -= quantity, locked_stock += quantity，WHERE stock >= quantity，返回影响行数 */
    int lockStock(@Param("storeId") Long storeId, @Param("productId") Long productId, @Param("quantity") int quantity);

    /** 解锁库存：locked_stock -= quantity, stock += quantity */
    int unlockStock(@Param("storeId") Long storeId, @Param("productId") Long productId, @Param("quantity") int quantity);

    /** 确认扣减：locked_stock -= quantity */
    int confirmStock(@Param("storeId") Long storeId, @Param("productId") Long productId, @Param("quantity") int quantity);
}
