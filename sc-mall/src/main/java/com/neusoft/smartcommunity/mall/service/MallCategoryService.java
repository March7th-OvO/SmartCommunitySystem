package com.neusoft.smartcommunity.mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.smartcommunity.mall.entity.MallCategory;

import java.util.List;

/** 商品分类服务 */
public interface MallCategoryService extends IService<MallCategory> {

    /** 查询树形分类列表（一级及其子级，仅启用） */
    List<MallCategory> listTree();
}
