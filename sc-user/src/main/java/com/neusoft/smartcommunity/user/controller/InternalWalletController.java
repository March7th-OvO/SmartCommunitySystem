package com.neusoft.smartcommunity.user.controller;

import com.neusoft.smartcommunity.common.api.Result;
import com.neusoft.smartcommunity.user.dto.WalletPayRequest;
import com.neusoft.smartcommunity.user.service.WalletPayService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 内部接口：供 sc-trade 调用钱包扣减/退款。
 */
@RestController
@RequestMapping("/user/internal/wallet")
public class InternalWalletController {

    private final WalletPayService walletPayService;

    public InternalWalletController(WalletPayService walletPayService) {
        this.walletPayService = walletPayService;
    }

    @PostMapping("/deduct")
    public Result<Void> deduct(@Valid @RequestBody WalletPayRequest request) {
        walletPayService.deduct(request.getUserId(), request.getAmount(), request.getBizType(),
                request.getBizId(), request.getRemark());
        return Result.success();
    }

    @PostMapping("/refund")
    public Result<Void> refund(@Valid @RequestBody WalletPayRequest request) {
        walletPayService.refund(request.getUserId(), request.getAmount(), request.getBizType(),
                request.getBizId(), request.getRemark());
        return Result.success();
    }
}
