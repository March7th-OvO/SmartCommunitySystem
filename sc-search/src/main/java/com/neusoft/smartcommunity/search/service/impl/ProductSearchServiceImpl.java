package com.neusoft.smartcommunity.search.service.impl;

import com.neusoft.smartcommunity.common.api.PageResult;
import com.neusoft.smartcommunity.search.document.ProductDocument;
import com.neusoft.smartcommunity.search.dto.ProductIndexRequest;
import com.neusoft.smartcommunity.search.repository.ProductSearchRepository;
import com.neusoft.smartcommunity.search.service.ProductSearchService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.stream.Collectors;

/** 商品搜索服务实现 */
@Service
public class ProductSearchServiceImpl implements ProductSearchService {

    private final ProductSearchRepository productSearchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public ProductSearchServiceImpl(ProductSearchRepository productSearchRepository,
                                    ElasticsearchOperations elasticsearchOperations) {
        this.productSearchRepository = productSearchRepository;
        this.elasticsearchOperations = elasticsearchOperations;
    }

    @Override
    public void indexProduct(ProductIndexRequest request) {
        ProductDocument doc = new ProductDocument();
        doc.setId(request.getId());
        doc.setName(request.getName());
        doc.setSubTitle(request.getSubTitle());
        doc.setCategoryId(request.getCategoryId());
        doc.setCategoryName(request.getCategoryName());
        doc.setBrandName(request.getBrandName());
        doc.setMainImage(request.getMainImage());
        doc.setDescription(request.getDescription());
        doc.setStatus(request.getStatus());
        productSearchRepository.save(doc);
    }

    @Override
    public void deleteProduct(Long productId) {
        productSearchRepository.deleteById(productId);
    }

    @Override
    public PageResult<ProductDocument> search(String keyword, Long categoryId, int pageNum, int pageSize) {
        Criteria c = new Criteria("status").is(1);
        if (categoryId != null) {
            c = c.and(new Criteria("categoryId").is(categoryId));
        }
        if (StringUtils.hasText(keyword)) {
            Criteria k = new Criteria("name").contains(keyword)
                    .or(new Criteria("subTitle").contains(keyword))
                    .or(new Criteria("description").contains(keyword));
            c = c.and(k);
        }
        Query query = new CriteriaQuery(c).setPageable(PageRequest.of(pageNum - 1, pageSize));
        SearchHits<ProductDocument> hits = elasticsearchOperations.search(query, ProductDocument.class);
        long total = hits.getTotalHits();
        var list = hits.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
        return new PageResult<>(total, list);
    }
}
