package com.neusoft.smartcommunity.mall.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** 商品保存请求（新增/修改） */
public class ProductSaveRequest {

    @NotBlank(message = "商品名称不能为空")
    @Size(max = 100)
    private String name;
    @Size(max = 255)
    private String subTitle;
    private Long categoryId;
    @Size(max = 50)
    private String brandName;
    @Size(max = 255)
    private String mainImage;
    private String albumImages;
    @Size(max = 20)
    private String unit;
    private String description;
    private Integer status;
    private Integer isRecommend;
    private Integer isNew;
    private Integer isHot;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSubTitle() { return subTitle; }
    public void setSubTitle(String subTitle) { this.subTitle = subTitle; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }
    public String getMainImage() { return mainImage; }
    public void setMainImage(String mainImage) { this.mainImage = mainImage; }
    public String getAlbumImages() { return albumImages; }
    public void setAlbumImages(String albumImages) { this.albumImages = albumImages; }
    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getIsRecommend() { return isRecommend; }
    public void setIsRecommend(Integer isRecommend) { this.isRecommend = isRecommend; }
    public Integer getIsNew() { return isNew; }
    public void setIsNew(Integer isNew) { this.isNew = isNew; }
    public Integer getIsHot() { return isHot; }
    public void setIsHot(Integer isHot) { this.isHot = isHot; }
}
