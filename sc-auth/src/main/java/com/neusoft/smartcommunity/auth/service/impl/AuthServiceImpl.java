package com.neusoft.smartcommunity.auth.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.neusoft.smartcommunity.auth.dto.ForgotPasswordRequest;
import com.neusoft.smartcommunity.auth.dto.LoginRequest;
import com.neusoft.smartcommunity.auth.dto.LoginResponse;
import com.neusoft.smartcommunity.auth.dto.RegisterRequest;
import com.neusoft.smartcommunity.auth.entity.PayWallet;
import com.neusoft.smartcommunity.auth.entity.SysLoginLog;
import com.neusoft.smartcommunity.auth.entity.UcUser;
import com.neusoft.smartcommunity.auth.mapper.PayWalletMapper;
import com.neusoft.smartcommunity.auth.mapper.SysLoginLogMapper;
import com.neusoft.smartcommunity.auth.service.AuthService;
import com.neusoft.smartcommunity.auth.service.UcUserService;
import com.neusoft.smartcommunity.common.api.ResultCode;
import com.neusoft.smartcommunity.common.exception.BusinessException;
import com.neusoft.smartcommunity.common.security.JwtProperties;
import com.neusoft.smartcommunity.common.security.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 认证与授权领域服务实现。
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final UcUserService ucUserService;
    private final PayWalletMapper payWalletMapper;
    private final SysLoginLogMapper sysLoginLogMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final JwtProperties jwtProperties;

    @Value("${auth.forgot-password.mock-code:123456}")
    private String forgotPasswordMockCode;

    public AuthServiceImpl(UcUserService ucUserService,
                           PayWalletMapper payWalletMapper,
                           SysLoginLogMapper sysLoginLogMapper,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil,
                           JwtProperties jwtProperties) {
        this.ucUserService = ucUserService;
        this.payWalletMapper = payWalletMapper;
        this.sysLoginLogMapper = sysLoginLogMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.jwtProperties = jwtProperties;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterRequest request) {
        UcUser exists = ucUserService.findByPhone(request.getPhone());
        if (exists != null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "该手机号已注册");
        }

        UcUser user = new UcUser();
        user.setPhone(request.getPhone());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setNickname(StringUtils.hasText(request.getNickname()) ? request.getNickname() : "用户" + request.getPhone().substring(7));
        user.setGender(0);
        user.setUserType(1);
        user.setStatus(1);
        user.setDeleted(0);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        ucUserService.save(user);

        PayWallet wallet = new PayWallet();
        wallet.setUserId(user.getId());
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setFrozenBalance(BigDecimal.ZERO);
        wallet.setStatus(1);
        wallet.setCreatedAt(LocalDateTime.now());
        wallet.setUpdatedAt(LocalDateTime.now());
        payWalletMapper.insert(wallet);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public LoginResponse login(LoginRequest request, String clientIp, String userAgent) {
        UcUser user = ucUserService.findByPhone(request.getPhone());
        if (user == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "账号或密码错误");
        }
        if (user.getStatus() != null && user.getStatus() == 0) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "账号已被禁用");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "账号或密码错误");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("phone", user.getPhone());
        claims.put("nickname", user.getNickname());
        claims.put("userType", user.getUserType());

        String token = jwtUtil.generateToken(String.valueOf(user.getId()), claims);

        user.setLastLoginIp(clientIp);
        user.setLastLoginTime(LocalDateTime.now());
        ucUserService.updateById(user);

        SysLoginLog log = new SysLoginLog();
        log.setUserId(user.getId());
        log.setUsername(user.getPhone());
        log.setIp(clientIp);
        log.setUserAgent(userAgent);
        log.setStatus(1);
        log.setMsg("登录成功");
        log.setLoginTime(LocalDateTime.now());
        sysLoginLogMapper.insert(log);

        LoginResponse resp = new LoginResponse();
        resp.setToken(token);
        resp.setExpireSeconds(jwtProperties.getExpireSeconds());
        resp.setUserId(user.getId());
        resp.setPhone(user.getPhone());
        resp.setNickname(user.getNickname());
        resp.setUserType(user.getUserType());
        return resp;
    }

    @Override
    public void forgotPassword(ForgotPasswordRequest request) {
        if (!forgotPasswordMockCode.equals(request.getCode())) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "验证码错误");
        }
        UcUser user = ucUserService.findByPhone(request.getPhone());
        if (user == null) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "该手机号未注册");
        }
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        ucUserService.updateById(user);
    }
}

