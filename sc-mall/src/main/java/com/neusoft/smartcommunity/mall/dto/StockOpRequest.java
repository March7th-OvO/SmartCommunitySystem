package com.neusoft.smartcommunity.mall.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** 库存操作请求（锁/解锁/确认），供 sc-trade 调用 */
public class StockOpRequest {

    @NotNull
    private Long storeId;
    @NotNull
    private Long productId;
    @NotNull
    @Min(1)
    private Integer quantity;

    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
