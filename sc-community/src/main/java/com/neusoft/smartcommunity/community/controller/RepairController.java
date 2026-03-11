package com.neusoft.smartcommunity.community.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.smartcommunity.common.api.Result;
import com.neusoft.smartcommunity.common.exception.BusinessException;
import com.neusoft.smartcommunity.common.api.ResultCode;
import com.neusoft.smartcommunity.community.entity.CommRepair;
import com.neusoft.smartcommunity.community.service.CommRepairService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/** 报事报修：用户提交/我的列表/取消 + 管理端处理 */
@RestController
@RequestMapping("/community/repair")
public class RepairController {

    private final CommRepairService repairService;

    public RepairController(CommRepairService repairService) {
        this.repairService = repairService;
    }

    private Long getUserId(HttpServletRequest request) {
        String id = request.getHeader("X-User-Id");
        if (id == null) throw new BusinessException(ResultCode.UNAUTHORIZED, "未登录");
        return Long.parseLong(id);
    }

    @PostMapping("/submit")
    public Result<Long> submit(HttpServletRequest request, @Valid @RequestBody CommRepair body) {
        body.setUserId(getUserId(request));
        repairService.submit(body);
        return Result.success(body.getId());
    }

    @GetMapping("/my")
    public Result<Page<CommRepair>> my(HttpServletRequest request,
                                      @RequestParam(defaultValue = "1") int pageNum,
                                      @RequestParam(defaultValue = "10") int pageSize,
                                      @RequestParam(required = false) Integer status) {
        return Result.success(repairService.pageByUser(getUserId(request), pageNum, pageSize, status));
    }

    @PostMapping("/{id}/cancel")
    public Result<Void> cancel(HttpServletRequest request, @PathVariable Long id) {
        repairService.cancel(id, getUserId(request));
        return Result.success();
    }

    @GetMapping("/admin/page")
    public Result<Page<CommRepair>> pageAdmin(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) Long communityId,
            @RequestParam(required = false) Integer status) {
        return Result.success(repairService.pageAdmin(pageNum, pageSize, communityId, status));
    }

    @PostMapping("/admin/{id}/start")
    public Result<Void> startHandle(@PathVariable Long id, HttpServletRequest request) {
        repairService.startHandle(id, getUserId(request));
        return Result.success();
    }

    @PostMapping("/admin/{id}/finish")
    public Result<Void> finish(@PathVariable Long id, HttpServletRequest request) {
        repairService.finish(id, getUserId(request));
        return Result.success();
    }
}
