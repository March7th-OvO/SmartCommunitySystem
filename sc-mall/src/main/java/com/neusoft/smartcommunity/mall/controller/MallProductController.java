package com.neusoft.smartcommunity.mall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.smartcommunity.common.api.Result;
import com.neusoft.smartcommunity.mall.dto.ProductSaveRequest;
import com.neusoft.smartcommunity.mall.entity.MallProduct;
import com.neusoft.smartcommunity.mall.service.MallProductService;
import com.neusoft.smartcommunity.mall.client.SearchSyncClient;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/** 商品 SPU 管理接口（增删改后同步 ES） */
@RestController
@RequestMapping("/mall/product")
public class MallProductController {

    private final MallProductService mallProductService;
    private final SearchSyncClient searchSyncClient;

    public MallProductController(MallProductService mallProductService, SearchSyncClient searchSyncClient) {
        this.mallProductService = mallProductService;
        this.searchSyncClient = searchSyncClient;
    }

    @GetMapping("/page")
    public Result<Page<MallProduct>> page(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String keyword) {
        return Result.success(mallProductService.pageList(pageNum, pageSize, categoryId, status, keyword));
    }

    @GetMapping("/{id}")
    public Result<MallProduct> getById(@PathVariable Long id) {
        return Result.success(mallProductService.getById(id));
    }

    @PostMapping
    public Result<Long> save(@Valid @RequestBody ProductSaveRequest request) {
        MallProduct entity = new MallProduct();
        BeanUtils.copyProperties(request, entity);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        if (entity.getStatus() == null) entity.setStatus(1);
        if (entity.getIsRecommend() == null) entity.setIsRecommend(0);
        if (entity.getIsNew() == null) entity.setIsNew(0);
        if (entity.getIsHot() == null) entity.setIsHot(0);
        if (entity.getUnit() == null) entity.setUnit("件");
        mallProductService.save(entity);
        searchSyncClient.indexProduct(entity);
        return Result.success(entity.getId());
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @Valid @RequestBody ProductSaveRequest request) {
        MallProduct entity = mallProductService.getById(id);
        if (entity == null) return Result.failed(com.neusoft.smartcommunity.common.api.ResultCode.BUSINESS_ERROR, "商品不存在");
        BeanUtils.copyProperties(request, entity, "id", "createdAt");
        entity.setId(id);
        entity.setUpdatedAt(LocalDateTime.now());
        mallProductService.updateById(entity);
        searchSyncClient.indexProduct(entity);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        mallProductService.removeById(id);
        searchSyncClient.deleteProduct(id);
        return Result.success();
    }
}
