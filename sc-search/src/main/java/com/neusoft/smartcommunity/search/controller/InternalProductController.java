package com.neusoft.smartcommunity.search.controller;

import com.neusoft.smartcommunity.common.api.Result;
import com.neusoft.smartcommunity.search.dto.ProductIndexRequest;
import com.neusoft.smartcommunity.search.service.ProductSearchService;
import org.springframework.web.bind.annotation.*;

/**
 * 内部接口：供 sc-mall 同步商品到 ES（索引/删除）。
 */
@RestController
@RequestMapping("/internal/product")
public class InternalProductController {

    private final ProductSearchService productSearchService;

    public InternalProductController(ProductSearchService productSearchService) {
        this.productSearchService = productSearchService;
    }

    @PostMapping("/index")
    public Result<Void> index(@RequestBody ProductIndexRequest request) {
        productSearchService.indexProduct(request);
        return Result.success();
    }

    @DeleteMapping("/{productId}")
    public Result<Void> delete(@PathVariable Long productId) {
        productSearchService.deleteProduct(productId);
        return Result.success();
    }
}
