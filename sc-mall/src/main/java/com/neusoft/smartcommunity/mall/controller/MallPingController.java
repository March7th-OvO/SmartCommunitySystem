package com.neusoft.smartcommunity.mall.controller;

import com.neusoft.smartcommunity.common.api.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商城服务心跳接口
 */
@RestController
@RequestMapping("/mall")
public class MallPingController {

    /**
     * 心跳接口
     *
     * @return 统一响应
     */
    @GetMapping("/ping")
    public Result<String> ping() {
        return Result.success("mall service ok");
    }
}

