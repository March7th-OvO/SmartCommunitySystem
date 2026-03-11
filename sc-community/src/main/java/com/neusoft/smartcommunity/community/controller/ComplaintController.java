package com.neusoft.smartcommunity.community.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.smartcommunity.common.api.Result;
import com.neusoft.smartcommunity.common.exception.BusinessException;
import com.neusoft.smartcommunity.common.api.ResultCode;
import com.neusoft.smartcommunity.community.entity.CommComplaint;
import com.neusoft.smartcommunity.community.service.CommComplaintService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/** 投诉建议：用户提交/我的列表 + 管理端处理 */
@RestController
@RequestMapping("/community/complaint")
public class ComplaintController {

    private final CommComplaintService complaintService;

    public ComplaintController(CommComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    private Long getUserId(HttpServletRequest request) {
        String id = request.getHeader("X-User-Id");
        if (id == null) throw new BusinessException(ResultCode.UNAUTHORIZED, "未登录");
        return Long.parseLong(id);
    }

    @PostMapping("/submit")
    public Result<Long> submit(HttpServletRequest request, @Valid @RequestBody CommComplaint body) {
        body.setUserId(getUserId(request));
        complaintService.submit(body);
        return Result.success(body.getId());
    }

    @GetMapping("/my")
    public Result<Page<CommComplaint>> my(HttpServletRequest request,
                                         @RequestParam(defaultValue = "1") int pageNum,
                                         @RequestParam(defaultValue = "10") int pageSize,
                                         @RequestParam(required = false) Integer status) {
        return Result.success(complaintService.pageByUser(getUserId(request), pageNum, pageSize, status));
    }

    @GetMapping("/admin/page")
    public Result<Page<CommComplaint>> pageAdmin(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long communityId,
            @RequestParam(required = false) Integer status) {
        return Result.success(complaintService.pageAdmin(pageNum, pageSize, communityId, status));
    }

    @PostMapping("/admin/{id}/handle")
    public Result<Void> handle(@PathVariable Long id, HttpServletRequest request,
                              @RequestParam String handleResult) {
        complaintService.handle(id, getUserId(request), handleResult);
        return Result.success();
    }
}
