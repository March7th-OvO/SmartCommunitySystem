package com.neusoft.smartcommunity.community.controller;

import com.neusoft.smartcommunity.common.api.Result;
import com.neusoft.smartcommunity.community.entity.CommNotice;
import com.neusoft.smartcommunity.community.service.CommNoticeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 公告：用户端查看 + 管理端 CRUD */
@RestController
@RequestMapping("/community/notice")
public class NoticeController {

    private final CommNoticeService noticeService;

    public NoticeController(CommNoticeService noticeService) {
        this.noticeService = noticeService;
    }

    /** 用户端：已发布公告列表 */
    @GetMapping("/list")
    public Result<List<CommNotice>> list(@RequestParam(required = false) Long communityId) {
        return Result.success(noticeService.listPublished(communityId));
    }

    @GetMapping("/{id}")
    public Result<CommNotice> get(@PathVariable Long id) {
        return Result.success(noticeService.getById(id));
    }

    /** 管理端：分页 */
    @GetMapping("/admin/page")
    public Result<com.baomidou.mybatisplus.extension.plugins.pagination.Page<CommNotice>> pageAdmin(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long communityId,
            @RequestParam(required = false) Integer status) {
        return Result.success(noticeService.pageAdmin(pageNum, pageSize, communityId, status));
    }

    @PostMapping("/admin")
    public Result<Void> save(@RequestBody CommNotice entity) {
        entity.setCreatedAt(java.time.LocalDateTime.now());
        entity.setUpdatedAt(java.time.LocalDateTime.now());
        noticeService.save(entity);
        return Result.success();
    }

    @PutMapping("/admin/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody CommNotice entity) {
        entity.setId(id);
        entity.setUpdatedAt(java.time.LocalDateTime.now());
        noticeService.updateById(entity);
        return Result.success();
    }

    @DeleteMapping("/admin/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        noticeService.removeById(id);
        return Result.success();
    }
}
