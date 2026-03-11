package com.neusoft.smartcommunity.search.controller;

import com.neusoft.smartcommunity.common.api.PageResult;
import com.neusoft.smartcommunity.common.api.Result;
import com.neusoft.smartcommunity.search.document.ProductDocument;
import com.neusoft.smartcommunity.search.service.ProductSearchService;
import org.springframework.web.bind.annotation.*;

/**
 * 商品搜索接口（对外，可走网关 /api/search/search）。
 */
@RestController
@RequestMapping("/search")
public class SearchController {

    private final ProductSearchService productSearchService;

    public SearchController(ProductSearchService productSearchService) {
        this.productSearchService = productSearchService;
    }

    /**
     * 商品关键词搜索，支持分类筛选、分页。
     */
    @GetMapping
    public Result<PageResult<ProductDocument>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        PageResult<ProductDocument> page = productSearchService.search(keyword, categoryId, pageNum, pageSize);
        return Result.success(page);
    }
}
