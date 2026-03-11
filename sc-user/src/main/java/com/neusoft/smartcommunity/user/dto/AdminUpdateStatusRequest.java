package com.neusoft.smartcommunity.user.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 管理端更新用户状态请求 DTO。
 */
public class AdminUpdateStatusRequest {

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 状态：1 启用，0 禁用
     */
    @NotNull(message = "状态不能为空")
    @Min(0)
    @Max(1)
    private Integer status;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
