package com.neusoft.smartcommunity.mall.controller;

import com.neusoft.smartcommunity.common.api.Result;
import com.neusoft.smartcommunity.mall.entity.MallStoreProduct;
import com.neusoft.smartcommunity.mall.service.MallStoreProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 门店商品及库存管理接口 */
@RestController
@RequestMapping("/mall/store-product")
public class MallStoreProductController {

    private final MallStoreProductService mallStoreProductService;

    public MallStoreProductController(MallStoreProductService mallStoreProductService) {
        this.mallStoreProductService = mallStoreProductService;
    }

    /** 按门店查询已上架的门店商品 */
    @GetMapping("/store/{storeId}")
    public Result<List<MallStoreProduct>> listByStore(@PathVariable Long storeId) {
        return Result.success(mallStoreProductService.listByStoreId(storeId));
    }

    /** 按商品查询绑定的门店及库存 */
    @GetMapping("/product/{productId}")
    public Result<List<MallStoreProduct>> listByProduct(@PathVariable Long productId) {
        return Result.success(mallStoreProductService.listByProductId(productId));
    }

    @GetMapping("/{id}")
    public Result<MallStoreProduct> getById(@PathVariable Long id) {
        return Result.success(mallStoreProductService.getById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody MallStoreProduct entity) {
        mallStoreProductService.save(entity);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody MallStoreProduct entity) {
        entity.setId(id);
        mallStoreProductService.updateById(entity);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        mallStoreProductService.removeById(id);
        return Result.success();
    }
}
