package com.neusoft.smartcommunity.mall.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.smartcommunity.common.api.Result;
import com.neusoft.smartcommunity.mall.entity.MallStore;
import com.neusoft.smartcommunity.mall.service.MallStoreService;
import org.springframework.web.bind.annotation.*;

/** 门店管理接口 */
@RestController
@RequestMapping("/mall/store")
public class MallStoreController {

    private final MallStoreService mallStoreService;

    public MallStoreController(MallStoreService mallStoreService) {
        this.mallStoreService = mallStoreService;
    }

    @GetMapping("/{id}")
    public Result<MallStore> getById(@PathVariable Long id) {
        return Result.success(mallStoreService.getById(id));
    }

    @GetMapping("/page")
    public Result<Page<MallStore>> page(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long communityId,
            @RequestParam(required = false) Integer status) {
        LambdaQueryWrapper<MallStore> w = new LambdaQueryWrapper<MallStore>()
                .eq(communityId != null, MallStore::getCommunityId, communityId)
                .eq(status != null, MallStore::getStatus, status)
                .orderByDesc(MallStore::getId);
        return Result.success(mallStoreService.page(new Page<>(pageNum, pageSize), w));
    }

    @PostMapping
    public Result<Void> save(@RequestBody MallStore entity) {
        mallStoreService.save(entity);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody MallStore entity) {
        entity.setId(id);
        mallStoreService.updateById(entity);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        mallStoreService.removeById(id);
        return Result.success();
    }
}
