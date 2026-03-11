package com.neusoft.smartcommunity.search.service;

import com.neusoft.smartcommunity.common.api.PageResult;
import com.neusoft.smartcommunity.search.document.ProductDocument;
import com.neusoft.smartcommunity.search.dto.ProductIndexRequest;

/** 商品搜索服务：索引、删除、关键词搜索 */
public interface ProductSearchService {

    void indexProduct(ProductIndexRequest request);

    void deleteProduct(Long productId);

    PageResult<ProductDocument> search(String keyword, Long categoryId, int pageNum, int pageSize);
}
