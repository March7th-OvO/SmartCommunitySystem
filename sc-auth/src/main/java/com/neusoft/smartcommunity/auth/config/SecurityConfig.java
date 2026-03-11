package com.neusoft.smartcommunity.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 安全相关基础配置，仅提供密码加密组件。
 */
@Configuration
public class SecurityConfig {

    /**
     * 密码编码器，用于对用户密码进行哈希处理。
     *
     * @return 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

