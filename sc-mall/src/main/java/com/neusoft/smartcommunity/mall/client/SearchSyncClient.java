package com.neusoft.smartcommunity.mall.client;

import com.neusoft.smartcommunity.mall.entity.MallProduct;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 调用 sc-search 同步商品到 ES（索引/删除）。使用服务名 sc-search 做负载均衡。
 */
@Component
public class SearchSyncClient {

    private static final String SEARCH_SERVICE = "http://sc-search";
    private static final String INDEX_URL = SEARCH_SERVICE + "/internal/product/index";
    private static final String DELETE_URL = SEARCH_SERVICE + "/internal/product/";

    private final RestTemplate restTemplate;

    public SearchSyncClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * 将商品索引到 ES（新增或全量更新）。
     */
    public void indexProduct(MallProduct product) {
        IndexBody body = new IndexBody();
        body.setId(product.getId());
        body.setName(product.getName());
        body.setSubTitle(product.getSubTitle());
        body.setCategoryId(product.getCategoryId());
        body.setBrandName(product.getBrandName());
        body.setMainImage(product.getMainImage());
        body.setDescription(product.getDescription());
        body.setStatus(product.getStatus());
        try {
            restTemplate.postForObject(INDEX_URL, body, Void.class);
        } catch (Exception e) {
            // 记录日志，不阻塞主流程；生产可改为 MQ 异步重试
        }
    }

    /**
     * 从 ES 删除商品文档。
     */
    public void deleteProduct(Long productId) {
        try {
            restTemplate.exchange(DELETE_URL + productId, HttpMethod.DELETE, null, Void.class);
        } catch (Exception e) {
            // 同上
        }
    }

    /** 与 sc-search ProductIndexRequest 字段一致 */
    public static class IndexBody {
        private Long id;
        private String name;
        private String subTitle;
        private Long categoryId;
        private String categoryName;
        private String brandName;
        private String mainImage;
        private String description;
        private Integer status;

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getSubTitle() { return subTitle; }
        public void setSubTitle(String subTitle) { this.subTitle = subTitle; }
        public Long getCategoryId() { return categoryId; }
        public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
        public String getCategoryName() { return categoryName; }
        public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
        public String getBrandName() { return brandName; }
        public void setBrandName(String brandName) { this.brandName = brandName; }
        public String getMainImage() { return mainImage; }
        public void setMainImage(String mainImage) { this.mainImage = mainImage; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Integer getStatus() { return status; }
        public void setStatus(Integer status) { this.status = status; }
    }
}
