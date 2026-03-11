package com.neusoft.smartcommunity.auth;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.neusoft.smartcommunity.common.security.JwtProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 认证服务启动类
 */
@SpringBootApplication
@MapperScan("com.neusoft.smartcommunity.auth.mapper")
@EnableDiscoveryClient
@EnableConfigurationProperties(JwtProperties.class)
public class AuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }
}

