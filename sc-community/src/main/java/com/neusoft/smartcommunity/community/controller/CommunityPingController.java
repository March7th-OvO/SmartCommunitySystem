package com.neusoft.smartcommunity.community.controller;

import com.neusoft.smartcommunity.common.api.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 社区服务心跳接口
 */
@RestController
@RequestMapping("/community")
public class CommunityPingController {

    /**
     * 心跳接口
     *
     * @return 统一响应
     */
    @GetMapping("/ping")
    public Result<String> ping() {
        return Result.success("community service ok");
    }
}

