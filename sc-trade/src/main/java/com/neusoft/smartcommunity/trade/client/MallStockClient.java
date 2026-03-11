package com.neusoft.smartcommunity.trade.client;

import com.neusoft.smartcommunity.common.api.Result;
import com.neusoft.smartcommunity.trade.dto.StoreProductInfoVo;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * 调用 sc-mall 库存锁定/解锁/确认及门店商品信息。
 */
@Component
public class MallStockClient {

    private static final String MALL = "http://sc-mall";
    private final RestTemplate restTemplate;

    public MallStockClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /** 获取门店商品信息（价格、名称、主图） */
    public StoreProductInfoVo getStoreProductInfo(Long storeId, Long productId) {
        String url = UriComponentsBuilder.fromHttpUrl(MALL + "/mall/internal/store-product/info")
                .queryParam("storeId", storeId).queryParam("productId", productId).toUriString();
        ResponseEntity<Result<StoreProductInfoVo>> res = restTemplate.exchange(url, HttpMethod.GET, null,
                new ParameterizedTypeReference<Result<StoreProductInfoVo>>() {});
        if (res.getBody() == null || res.getBody().getCode() != 0 || res.getBody().getData() == null) {
            throw new RuntimeException("获取商品信息失败");
        }
        return res.getBody().getData();
    }

    public void lockStock(Long storeId, Long productId, int quantity) {
        Map<String, Object> body = Map.of("storeId", storeId, "productId", productId, "quantity", quantity);
        ResponseEntity<Result<Map<String, Boolean>>> res = restTemplate.exchange(
                MALL + "/mall/internal/stock/lock",
                HttpMethod.POST,
                new org.springframework.http.HttpEntity<>(body),
                new ParameterizedTypeReference<Result<Map<String, Boolean>>>() {});
        if (res.getBody() == null || res.getBody().getCode() != 0) {
            throw new RuntimeException(res.getBody() != null ? res.getBody().getMessage() : "锁库存失败");
        }
    }

    public void unlockStock(Long storeId, Long productId, int quantity) {
        Map<String, Object> body = Map.of("storeId", storeId, "productId", productId, "quantity", quantity);
        restTemplate.postForObject(MALL + "/mall/internal/stock/unlock", body, Result.class);
    }

    public void confirmStock(Long storeId, Long productId, int quantity) {
        Map<String, Object> body = Map.of("storeId", storeId, "productId", productId, "quantity", quantity);
        ResponseEntity<Result<Void>> res = restTemplate.exchange(
                MALL + "/mall/internal/stock/confirm",
                HttpMethod.POST,
                new org.springframework.http.HttpEntity<>(body),
                new ParameterizedTypeReference<Result<Void>>() {});
        if (res.getBody() == null || res.getBody().getCode() != 0) {
            throw new RuntimeException(res.getBody() != null ? res.getBody().getMessage() : "确认库存失败");
        }
    }
}
