package com.neusoft.smartcommunity.trade.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** 购物车项请求（添加/修改数量） */
public class CartItemRequest {

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
