package com.neusoft.smartcommunity.mall.controller;

import com.neusoft.smartcommunity.common.api.Result;
import com.neusoft.smartcommunity.mall.entity.MallCategory;
import com.neusoft.smartcommunity.mall.service.MallCategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 商品分类管理接口 */
@RestController
@RequestMapping("/mall/category")
public class MallCategoryController {

    private final MallCategoryService mallCategoryService;

    public MallCategoryController(MallCategoryService mallCategoryService) {
        this.mallCategoryService = mallCategoryService;
    }

    /** 树形分类列表（仅启用） */
    @GetMapping("/tree")
    public Result<List<MallCategory>> tree() {
        return Result.success(mallCategoryService.listTree());
    }

    @GetMapping("/{id}")
    public Result<MallCategory> getById(@PathVariable Long id) {
        return Result.success(mallCategoryService.getById(id));
    }

    @PostMapping
    public Result<Void> save(@RequestBody MallCategory entity) {
        mallCategoryService.save(entity);
        return Result.success();
    }

    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody MallCategory entity) {
        entity.setId(id);
        mallCategoryService.updateById(entity);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        mallCategoryService.removeById(id);
        return Result.success();
    }
}
