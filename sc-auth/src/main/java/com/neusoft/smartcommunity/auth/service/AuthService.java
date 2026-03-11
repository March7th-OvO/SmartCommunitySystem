package com.neusoft.smartcommunity.auth.service;

import com.neusoft.smartcommunity.auth.dto.ForgotPasswordRequest;
import com.neusoft.smartcommunity.auth.dto.LoginRequest;
import com.neusoft.smartcommunity.auth.dto.LoginResponse;
import com.neusoft.smartcommunity.auth.dto.RegisterRequest;

/**
 * 认证与授权领域服务接口。
 */
public interface AuthService {

    /**
     * 用户注册。
     *
     * @param request 注册请求
     */
    void register(RegisterRequest request);

    /**
     * 用户登录。
     *
     * @param request 登录请求
     * @param clientIp 客户端 IP
     * @param userAgent UA 信息
     * @return 登录结果，包含 JWT
     */
    LoginResponse login(LoginRequest request, String clientIp, String userAgent);

    /**
     * 找回密码（通过手机号 + 验证码重置密码，验证码当前为 mock，后续对接短信）。
     *
     * @param request 找回密码请求
     */
    void forgotPassword(ForgotPasswordRequest request);
}

