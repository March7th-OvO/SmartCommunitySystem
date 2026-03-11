package com.neusoft.smartcommunity.trade.controller;

import com.neusoft.smartcommunity.common.api.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 交易服务心跳接口
 */
@RestController
@RequestMapping("/trade")
public class TradePingController {

    /**
     * 心跳接口
     *
     * @return 统一响应
     */
    @GetMapping("/ping")
    public Result<String> ping() {
        return Result.success("trade service ok");
    }
}

