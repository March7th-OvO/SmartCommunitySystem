package com.neusoft.smartcommunity.mall.dto;

import java.math.BigDecimal;

/** 门店商品信息（价格、名称、主图），供下单使用 */
public class StoreProductInfoDto {

    private Long storeId;
    private Long productId;
    private BigDecimal salePrice;
    private String productName;
    private String productMainImage;

    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getProductMainImage() { return productMainImage; }
    public void setProductMainImage(String productMainImage) { this.productMainImage = productMainImage; }
}
