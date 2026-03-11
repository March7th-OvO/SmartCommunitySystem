package com.neusoft.smartcommunity.community.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.smartcommunity.common.api.Result;
import com.neusoft.smartcommunity.common.exception.BusinessException;
import com.neusoft.smartcommunity.common.api.ResultCode;
import com.neusoft.smartcommunity.community.entity.CommVisitor;
import com.neusoft.smartcommunity.community.service.CommVisitorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/** 访客登记：用户提交/我的记录 + 管理端审核 */
@RestController
@RequestMapping("/community/visitor")
public class VisitorController {

    private final CommVisitorService visitorService;

    public VisitorController(CommVisitorService visitorService) {
        this.visitorService = visitorService;
    }

    private Long getUserId(HttpServletRequest request) {
        String id = request.getHeader("X-User-Id");
        if (id == null) throw new BusinessException(ResultCode.UNAUTHORIZED, "未登录");
        return Long.parseLong(id);
    }

    @PostMapping("/submit")
    public Result<Long> submit(HttpServletRequest request, @Valid @RequestBody CommVisitor body) {
        body.setHostUserId(getUserId(request));
        visitorService.submit(body);
        return Result.success(body.getId());
    }

    @GetMapping("/my")
    public Result<Page<CommVisitor>> my(HttpServletRequest request,
                                       @RequestParam(defaultValue = "1") int pageNum,
                                       @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(visitorService.pageByHost(getUserId(request), pageNum, pageSize));
    }

    @GetMapping("/admin/page")
    public Result<Page<CommVisitor>> pageAdmin(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long communityId,
            @RequestParam(required = false) Integer status) {
        return Result.success(visitorService.pageAdmin(pageNum, pageSize, communityId, status));
    }

    @PostMapping("/admin/{id}/audit")
    public Result<Void> audit(@PathVariable Long id,
                             @RequestParam Integer status,
                             HttpServletRequest request,
                             @RequestParam(required = false) String remark) {
        visitorService.audit(id, status, getUserId(request), remark);
        return Result.success();
    }
}
