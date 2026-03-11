package com.neusoft.smartcommunity.trade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.neusoft.smartcommunity.common.api.ResultCode;
import com.neusoft.smartcommunity.common.exception.BusinessException;
import com.neusoft.smartcommunity.trade.dto.CartItemRequest;
import com.neusoft.smartcommunity.trade.entity.TradeCartItem;
import com.neusoft.smartcommunity.trade.mapper.TradeCartItemMapper;
import com.neusoft.smartcommunity.trade.service.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    private final TradeCartItemMapper cartItemMapper;

    public CartServiceImpl(TradeCartItemMapper cartItemMapper) {
        this.cartItemMapper = cartItemMapper;
    }

    @Override
    public List<TradeCartItem> listByUserId(Long userId) {
        return cartItemMapper.selectList(new LambdaQueryWrapper<TradeCartItem>()
                .eq(TradeCartItem::getUserId, userId)
                .orderByDesc(TradeCartItem::getUpdatedAt));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Long userId, CartItemRequest request) {
        TradeCartItem one = cartItemMapper.selectOne(new LambdaQueryWrapper<TradeCartItem>()
                .eq(TradeCartItem::getUserId, userId)
                .eq(TradeCartItem::getStoreId, request.getStoreId())
                .eq(TradeCartItem::getProductId, request.getProductId())
                .last("LIMIT 1"));
        if (one != null) {
            one.setQuantity(one.getQuantity() + request.getQuantity());
            one.setUpdatedAt(LocalDateTime.now());
            cartItemMapper.updateById(one);
        } else {
            TradeCartItem item = new TradeCartItem();
            item.setUserId(userId);
            item.setStoreId(request.getStoreId());
            item.setProductId(request.getProductId());
            item.setQuantity(request.getQuantity());
            item.setChecked(1);
            item.setCreatedAt(LocalDateTime.now());
            item.setUpdatedAt(LocalDateTime.now());
            cartItemMapper.insert(item);
        }
    }

    @Override
    public void updateQuantity(Long userId, Long cartItemId, Integer quantity) {
        TradeCartItem item = cartItemMapper.selectOne(new LambdaQueryWrapper<TradeCartItem>()
                .eq(TradeCartItem::getId, cartItemId).eq(TradeCartItem::getUserId, userId));
        if (item == null) throw new BusinessException(ResultCode.BUSINESS_ERROR, "购物车项不存在");
        item.setQuantity(quantity);
        item.setUpdatedAt(LocalDateTime.now());
        cartItemMapper.updateById(item);
    }

    @Override
    public void setChecked(Long userId, Long cartItemId, Integer checked) {
        cartItemMapper.update(null, new LambdaUpdateWrapper<TradeCartItem>()
                .eq(TradeCartItem::getId, cartItemId)
                .eq(TradeCartItem::getUserId, userId)
                .set(TradeCartItem::getChecked, checked)
                .set(TradeCartItem::getUpdatedAt, LocalDateTime.now()));
    }

    @Override
    public void remove(Long userId, Long cartItemId) {
        cartItemMapper.delete(new LambdaQueryWrapper<TradeCartItem>()
                .eq(TradeCartItem::getId, cartItemId)
                .eq(TradeCartItem::getUserId, userId));
    }

    @Override
    public void removeByUserIdAndStoreId(Long userId, Long storeId) {
        cartItemMapper.delete(new LambdaQueryWrapper<TradeCartItem>()
                .eq(TradeCartItem::getUserId, userId)
                .eq(TradeCartItem::getStoreId, storeId));
    }
}
