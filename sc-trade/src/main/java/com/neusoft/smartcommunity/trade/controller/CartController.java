package com.neusoft.smartcommunity.trade.controller;

import com.neusoft.smartcommunity.common.api.Result;
import com.neusoft.smartcommunity.common.exception.BusinessException;
import com.neusoft.smartcommunity.common.api.ResultCode;
import com.neusoft.smartcommunity.trade.dto.CartItemRequest;
import com.neusoft.smartcommunity.trade.entity.TradeCartItem;
import com.neusoft.smartcommunity.trade.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 购物车接口 */
@RestController
@RequestMapping("/trade/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    private Long getUserId(HttpServletRequest request) {
        String id = request.getHeader("X-User-Id");
        if (id == null) throw new BusinessException(ResultCode.UNAUTHORIZED, "未登录");
        return Long.parseLong(id);
    }

    @GetMapping("/list")
    public Result<List<TradeCartItem>> list(HttpServletRequest request) {
        return Result.success(cartService.listByUserId(getUserId(request)));
    }

    @PostMapping("/add")
    public Result<Void> add(HttpServletRequest request, @Valid @RequestBody CartItemRequest body) {
        cartService.add(getUserId(request), body);
        return Result.success();
    }

    @PutMapping("/{cartItemId}/quantity")
    public Result<Void> updateQuantity(HttpServletRequest request,
                                      @PathVariable Long cartItemId,
                                      @RequestParam Integer quantity) {
        cartService.updateQuantity(getUserId(request), cartItemId, quantity);
        return Result.success();
    }

    @PutMapping("/{cartItemId}/checked")
    public Result<Void> setChecked(HttpServletRequest request,
                                   @PathVariable Long cartItemId,
                                   @RequestParam Integer checked) {
        cartService.setChecked(getUserId(request), cartItemId, checked);
        return Result.success();
    }

    @DeleteMapping("/{cartItemId}")
    public Result<Void> remove(HttpServletRequest request, @PathVariable Long cartItemId) {
        cartService.remove(getUserId(request), cartItemId);
        return Result.success();
    }
}
