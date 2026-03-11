package com.neusoft.smartcommunity.trade.service;

import com.neusoft.smartcommunity.trade.dto.CartItemRequest;
import com.neusoft.smartcommunity.trade.entity.TradeCartItem;

import java.util.List;

/** 购物车服务 */
public interface CartService {

    List<TradeCartItem> listByUserId(Long userId);

    void add(Long userId, CartItemRequest request);

    void updateQuantity(Long userId, Long cartItemId, Integer quantity);

    void setChecked(Long userId, Long cartItemId, Integer checked);

    void remove(Long userId, Long cartItemId);

    void removeByUserIdAndStoreId(Long userId, Long storeId);
}
