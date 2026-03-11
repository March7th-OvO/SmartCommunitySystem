package com.neusoft.smartcommunity.community.client;

import com.neusoft.smartcommunity.common.api.Result;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Map;

/** 调用 sc-user 钱包扣减（物业费缴纳） */
@Component
public class UserWalletClient {

    private static final String USER = "http://sc-user";
    private final RestTemplate restTemplate;

    public UserWalletClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void deduct(Long userId, BigDecimal amount, String bizType, Long bizId, String remark) {
        Map<String, Object> body = Map.of(
                "userId", userId,
                "amount", amount,
                "bizType", bizType,
                "bizId", bizId != null ? bizId : 0,
                "remark", remark != null ? remark : "");
        ResponseEntity<Result> res = restTemplate.postForEntity(USER + "/user/internal/wallet/deduct", body, Result.class);
        if (res.getBody() == null || res.getBody().getCode() != 0) {
            throw new RuntimeException(res.getBody() != null ? res.getBody().getMessage() : "扣减余额失败");
        }
    }
}
