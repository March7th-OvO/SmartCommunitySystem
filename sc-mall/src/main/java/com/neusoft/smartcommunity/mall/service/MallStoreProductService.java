package com.neusoft.smartcommunity.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.smartcommunity.mall.entity.MallStoreProduct;

import java.util.List;

/** 门店商品及库存服务 */
public interface MallStoreProductService extends IService<MallStoreProduct> {

    /** 按门店查询已上架的门店商品列表 */
    List<MallStoreProduct> listByStoreId(Long storeId);

    /** 按商品查询绑定的门店及库存列表 */
    List<MallStoreProduct> listByProductId(Long productId);

    /**
     * 锁定库存（下单时）：stock -= quantity, locked_stock += quantity，需 stock >= quantity。
     * @return true 锁定成功，false 库存不足
     */
    boolean lockStock(Long storeId, Long productId, int quantity);

    /**
     * 解锁库存（取消订单时）：locked_stock -= quantity, stock += quantity。
     */
    void unlockStock(Long storeId, Long productId, int quantity);

    /**
     * 确认扣减（支付成功后）：locked_stock -= quantity，不再回写 stock。
     */
    void confirmStock(Long storeId, Long productId, int quantity);
}
