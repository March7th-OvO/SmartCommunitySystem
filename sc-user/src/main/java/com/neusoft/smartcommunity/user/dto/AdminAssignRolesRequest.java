package com.neusoft.smartcommunity.user.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * 管理端分配角色请求 DTO。
 */
public class AdminAssignRolesRequest {

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /** 角色ID列表，可为空表示清空所有角色 */
    private List<Long> roleIds;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }
}
