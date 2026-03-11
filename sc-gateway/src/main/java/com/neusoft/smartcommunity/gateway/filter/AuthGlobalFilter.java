package com.neusoft.smartcommunity.gateway.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neusoft.smartcommunity.common.api.Result;
import com.neusoft.smartcommunity.common.api.ResultCode;
import com.neusoft.smartcommunity.common.security.JwtProperties;
import com.neusoft.smartcommunity.common.security.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * 网关鉴权全局过滤器，校验 JWT，有效时将用户信息透传给下游服务。
 */
@Component
public class AuthGlobalFilter implements GlobalFilter, Ordered {

    private final JwtUtil jwtUtil;
    private final JwtProperties jwtProperties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 网关白名单路径（无需登录）
     */
    private static final List<String> WHITE_LIST = Arrays.asList(
            "/api/auth/",      // 登录注册等
            "/api/search",     // 商品搜索（可匿名）
            "/v3/api-docs",    // OpenAPI
            "/swagger",        // Swagger 相关
            "/actuator"        // 健康检查
    );

    public AuthGlobalFilter(JwtUtil jwtUtil, JwtProperties jwtProperties) {
        this.jwtUtil = jwtUtil;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 白名单直接放行
        if (isWhitePath(path)) {
            return chain.filter(exchange);
        }

        HttpHeaders headers = request.getHeaders();
        String authHeader = headers.getFirst(jwtProperties.getHeader());
        if (StringUtils.isBlank(authHeader) || !authHeader.startsWith(jwtProperties.getTokenPrefix())) {
            return unauthorized(exchange.getResponse(), "未登录或登录已过期");
        }

        String token = authHeader.substring(jwtProperties.getTokenPrefix().length());
        try {
            Claims claims = jwtUtil.parseToken(token);
            String userId = claims.getSubject();

            // 将用户ID等信息透传到下游服务
            ServerHttpRequest mutated = request.mutate()
                    .header("X-User-Id", userId)
                    .build();
            return chain.filter(exchange.mutate().request(mutated).build());
        } catch (Exception e) {
            return unauthorized(exchange.getResponse(), "Token 无效或已过期");
        }
    }

    private boolean isWhitePath(String path) {
        String lowerPath = path.toLowerCase();
        for (String prefix : WHITE_LIST) {
            if (lowerPath.startsWith(prefix.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private Mono<Void> unauthorized(ServerHttpResponse response, String message) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        Result<Void> body = Result.failed(ResultCode.UNAUTHORIZED, message);
        byte[] bytes;
        try {
            bytes = objectMapper.writeValueAsBytes(body);
        } catch (JsonProcessingException e) {
            bytes = ("{\"code\":1002,\"message\":\"" + message + "\"}").getBytes(StandardCharsets.UTF_8);
        }
        return response.writeWith(Mono.just(response.bufferFactory().wrap(bytes)));
    }

    @Override
    public int getOrder() {
        return -100;
    }
}

