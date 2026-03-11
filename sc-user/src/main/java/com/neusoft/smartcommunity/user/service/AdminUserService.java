package com.neusoft.smartcommunity.user.service;

import com.neusoft.smartcommunity.common.api.PageResult;
import com.neusoft.smartcommunity.user.dto.AdminAssignRolesRequest;
import com.neusoft.smartcommunity.user.dto.AdminUpdateStatusRequest;
import com.neusoft.smartcommunity.user.dto.RoleOptionDto;
import com.neusoft.smartcommunity.user.dto.UserListItemDto;

import java.util.List;

/**
 * 管理端用户与角色管理服务接口。
 */
public interface AdminUserService {

    /**
     * 分页查询用户列表（支持手机号、状态、用户类型筛选）。
     *
     * @param pageNum  页码，从 1 开始
     * @param pageSize 每页条数
     * @param phone    手机号模糊（可选）
     * @param status   状态（可选）
     * @param userType 用户类型（可选）
     * @return 分页结果
     */
    PageResult<UserListItemDto> listUsers(int pageNum, int pageSize, String phone, Integer status, Integer userType);

    /**
     * 更新用户状态（启用/禁用），仅管理员可操作。
     *
     * @param operatorUserId 当前操作人用户ID
     * @param request       请求体
     */
    void updateUserStatus(Long operatorUserId, AdminUpdateStatusRequest request);

    /**
     * 为用户分配角色，仅管理员可操作。
     *
     * @param operatorUserId 当前操作人用户ID
     * @param request        请求体
     */
    void assignRoles(Long operatorUserId, AdminAssignRolesRequest request);

    /**
     * 查询所有启用的角色（用于下拉等）。
     *
     * @return 角色选项列表
     */
    List<RoleOptionDto> listRoleOptions();

    /**
     * 校验当前用户是否为管理员（社区管理员或平台管理员）。
     *
     * @param userId 用户ID
     * @return 是否为管理员
     */
    boolean isAdmin(Long userId);
}
