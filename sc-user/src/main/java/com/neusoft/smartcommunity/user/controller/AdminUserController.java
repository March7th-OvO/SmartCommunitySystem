package com.neusoft.smartcommunity.user.controller;

import com.neusoft.smartcommunity.common.api.PageResult;
import com.neusoft.smartcommunity.common.api.Result;
import com.neusoft.smartcommunity.common.exception.BusinessException;
import com.neusoft.smartcommunity.common.api.ResultCode;
import com.neusoft.smartcommunity.user.dto.AdminAssignRolesRequest;
import com.neusoft.smartcommunity.user.dto.AdminUpdateStatusRequest;
import com.neusoft.smartcommunity.user.dto.RoleOptionDto;
import com.neusoft.smartcommunity.user.dto.UserListItemDto;
import com.neusoft.smartcommunity.user.service.AdminUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 管理端用户与角色管理接口（需管理员身份，由网关透传 X-User-Id 后校验 userType）。
 */
@RestController
@RequestMapping("/user/admin")
public class AdminUserController {

    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    /**
     * 分页查询用户列表。
     *
     * @param pageNum  页码，从 1 开始
     * @param pageSize 每页条数
     * @param phone    手机号模糊（可选）
     * @param status   状态（可选）
     * @param userType 用户类型（可选）
     * @param request  HTTP 请求（取 X-User-Id 做权限校验，可选：仅管理端接口强制校验）
     * @return 分页结果
     */
    @GetMapping("/list")
    public Result<PageResult<UserListItemDto>> list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer userType,
            HttpServletRequest request) {
        Long operatorId = parseUserId(request);
        if (!adminUserService.isAdmin(operatorId)) {
            throw new BusinessException(ResultCode.FORBIDDEN, "没有权限查看用户列表");
        }
        PageResult<UserListItemDto> page = adminUserService.listUsers(pageNum, pageSize, phone, status, userType);
        return Result.success(page);
    }

    /**
     * 更新用户状态（启用/禁用）。
     *
     * @param body    请求体
     * @param request HTTP 请求
     * @return 统一响应
     */
    @PutMapping("/status")
    public Result<Void> updateStatus(@Valid @RequestBody AdminUpdateStatusRequest body, HttpServletRequest request) {
        Long operatorId = parseUserId(request);
        adminUserService.updateUserStatus(operatorId, body);
        return Result.success();
    }

    /**
     * 为用户分配角色。
     *
     * @param body    请求体
     * @param request HTTP 请求
     * @return 统一响应
     */
    @PutMapping("/roles")
    public Result<Void> assignRoles(@Valid @RequestBody AdminAssignRolesRequest body, HttpServletRequest request) {
        Long operatorId = parseUserId(request);
        adminUserService.assignRoles(operatorId, body);
        return Result.success();
    }

    /**
     * 查询所有启用的角色（下拉选项）。
     *
     * @param request HTTP 请求（可选校验管理员）
     * @return 角色列表
     */
    @GetMapping("/roles")
    public Result<List<RoleOptionDto>> listRoles(HttpServletRequest request) {
        Long operatorId = parseUserId(request);
        if (!adminUserService.isAdmin(operatorId)) {
            throw new BusinessException(ResultCode.FORBIDDEN, "没有权限查看角色列表");
        }
        List<RoleOptionDto> list = adminUserService.listRoleOptions();
        return Result.success(list);
    }

    private static Long parseUserId(HttpServletRequest request) {
        String userIdHeader = request.getHeader("X-User-Id");
        if (userIdHeader == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "未获取到用户身份");
        }
        return Long.parseLong(userIdHeader);
    }
}
