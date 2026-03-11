package com.neusoft.smartcommunity.community.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.smartcommunity.common.api.Result;
import com.neusoft.smartcommunity.community.entity.CommCommunity;
import com.neusoft.smartcommunity.community.mapper.CommCommunityMapper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 小区列表（用户端/管理端） */
@RestController
@RequestMapping("/community")
public class CommunityController {

    private final CommCommunityMapper communityMapper;

    public CommunityController(CommCommunityMapper communityMapper) {
        this.communityMapper = communityMapper;
    }

    @GetMapping("/list")
    public Result<List<CommCommunity>> list(@RequestParam(required = false) Integer status) {
        List<CommCommunity> list = communityMapper.selectList(
                new LambdaQueryWrapper<CommCommunity>()
                        .eq(status != null, CommCommunity::getStatus, status)
                        .orderByAsc(CommCommunity::getId));
        return Result.success(list);
    }

    @GetMapping("/{id}")
    public Result<CommCommunity> get(@PathVariable Long id) {
        return Result.success(communityMapper.selectById(id));
    }
}
