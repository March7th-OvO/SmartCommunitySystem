package com.neusoft.smartcommunity.mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.smartcommunity.mall.entity.MallCategory;
import com.neusoft.smartcommunity.mall.mapper.MallCategoryMapper;
import com.neusoft.smartcommunity.mall.service.MallCategoryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/** 商品分类服务实现 */
@Service
public class MallCategoryServiceImpl extends ServiceImpl<MallCategoryMapper, MallCategory> implements MallCategoryService {

    @Override
    public List<MallCategory> listTree() {
        List<MallCategory> all = list(new LambdaQueryWrapper<MallCategory>()
                .eq(MallCategory::getStatus, 1)
                .orderByAsc(MallCategory::getSort));
        if (all == null || all.isEmpty()) return new ArrayList<>();
        List<MallCategory> roots = all.stream()
                .filter(c -> c.getParentId() == null || c.getParentId() == 0)
                .collect(Collectors.toList());
        for (MallCategory root : roots) {
            root.setChildren(all.stream()
                    .filter(c -> root.getId().equals(c.getParentId()))
                    .collect(Collectors.toList()));
        }
        return roots;
    }
}
