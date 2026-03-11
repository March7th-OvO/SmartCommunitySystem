package com.neusoft.smartcommunity.auth.controller;

import com.neusoft.smartcommunity.auth.dto.ForgotPasswordRequest;
import com.neusoft.smartcommunity.auth.dto.LoginRequest;
import com.neusoft.smartcommunity.auth.dto.LoginResponse;
import com.neusoft.smartcommunity.auth.dto.RegisterRequest;
import com.neusoft.smartcommunity.auth.service.AuthService;
import com.neusoft.smartcommunity.common.api.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 认证服务基础控制器（后续 Phase 3 实现登录注册）
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 心跳接口，用于验证服务是否正常注册到 Nacos
     *
     * @return 统一响应
     */
    @GetMapping("/ping")
    public Result<String> ping() {
        return Result.success("auth service ok");
    }

    /**
     * 用户注册接口。
     *
     * @param request 注册请求体
     * @return 统一响应
     */
    @PostMapping("/register")
    public Result<Void> register(@Valid @RequestBody RegisterRequest request) {
        authService.register(request);
        return Result.success();
    }

    /**
     * 用户登录接口，成功后发放 JWT。
     *
     * @param request 登录请求体
     * @param httpRequest HTTP 请求对象，用于获取 IP 和 UA
     * @return 登录响应，包含 JWT
     */
    @PostMapping("/login")
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        String clientIp = httpRequest.getRemoteAddr();
        String userAgent = httpRequest.getHeader("User-Agent");
        LoginResponse resp = authService.login(request, clientIp, userAgent);
        return Result.success(resp);
    }

    /**
     * 找回密码（手机号 + 验证码重置，验证码当前 mock 为配置项 auth.forgot-password.mock-code）。
     *
     * @param request 找回密码请求体
     * @return 统一响应
     */
    @PostMapping("/forgot-password")
    public Result<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request);
        return Result.success();
    }
}

