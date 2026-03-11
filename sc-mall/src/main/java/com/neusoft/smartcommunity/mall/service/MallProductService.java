package com.neusoft.smartcommunity.mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.smartcommunity.mall.entity.MallProduct;

/** 商品 SPU 服务 */
public interface MallProductService extends IService<MallProduct> {

    /** 分页查询（支持分类、状态、关键词） */
    Page<MallProduct> pageList(int pageNum, int pageSize, Long categoryId, Integer status, String keyword);
}
