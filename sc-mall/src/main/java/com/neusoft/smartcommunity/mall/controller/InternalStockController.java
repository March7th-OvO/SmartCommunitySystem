package com.neusoft.smartcommunity.mall.controller;

import com.neusoft.smartcommunity.common.api.Result;
import com.neusoft.smartcommunity.common.api.ResultCode;
import com.neusoft.smartcommunity.common.exception.BusinessException;
import com.neusoft.smartcommunity.mall.dto.StockOpRequest;
import com.neusoft.smartcommunity.mall.dto.StoreProductInfoDto;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.neusoft.smartcommunity.mall.entity.MallProduct;
import com.neusoft.smartcommunity.mall.entity.MallStoreProduct;
import com.neusoft.smartcommunity.mall.service.MallProductService;
import com.neusoft.smartcommunity.mall.service.MallStoreProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 内部接口：供 sc-trade 调用库存锁定/解锁/确认及查询门店商品信息。
 */
@RestController
@RequestMapping("/mall/internal")
public class InternalStockController {

    private final MallStoreProductService mallStoreProductService;

    private final MallProductService mallProductService;

    public InternalStockController(MallStoreProductService mallStoreProductService, MallProductService mallProductService) {
        this.mallStoreProductService = mallStoreProductService;
        this.mallProductService = mallProductService;
    }

    /** 查询门店商品信息（价格、名称、主图），供下单快照 */
    @GetMapping("/store-product/info")
    public Result<StoreProductInfoDto> getStoreProductInfo(@RequestParam Long storeId, @RequestParam Long productId) {
        MallStoreProduct sp = mallStoreProductService.getOne(
                new LambdaQueryWrapper<MallStoreProduct>()
                        .eq(MallStoreProduct::getStoreId, storeId)
                        .eq(MallStoreProduct::getProductId, productId)
                        .last("LIMIT 1"));
        if (sp == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "门店未上架该商品");
        }
        MallProduct p = mallProductService.getById(productId);
        StoreProductInfoDto dto = new StoreProductInfoDto();
        dto.setStoreId(storeId);
        dto.setProductId(productId);
        dto.setSalePrice(sp.getSalePrice());
        dto.setProductName(p != null ? p.getName() : "");
        dto.setProductMainImage(p != null ? p.getMainImage() : null);
        return Result.success(dto);
    }

    /** 锁定库存，成功返回 success，库存不足抛业务异常 */
    @PostMapping("/stock/lock")
    public Result<Map<String, Boolean>> lock(@Valid @RequestBody StockOpRequest request) {
        boolean ok = mallStoreProductService.lockStock(request.getStoreId(), request.getProductId(), request.getQuantity());
        if (!ok) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "库存不足");
        }
        return Result.success(Map.of("success", true));
    }

    @PostMapping("/stock/unlock")
    public Result<Void> unlock(@Valid @RequestBody StockOpRequest request) {
        mallStoreProductService.unlockStock(request.getStoreId(), request.getProductId(), request.getQuantity());
        return Result.success();
    }

    @PostMapping("/stock/confirm")
    public Result<Void> confirm(@Valid @RequestBody StockOpRequest request) {
        mallStoreProductService.confirmStock(request.getStoreId(), request.getProductId(), request.getQuantity());
        return Result.success();
    }
}
