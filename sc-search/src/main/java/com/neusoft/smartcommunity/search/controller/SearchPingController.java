package com.neusoft.smartcommunity.search.controller;

import com.neusoft.smartcommunity.common.api.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 搜索服务心跳接口
 */
@RestController
@RequestMapping("/search")
public class SearchPingController {

    /**
     * 心跳接口
     *
     * @return 统一响应
     */
    @GetMapping("/ping")
    public Result<String> ping() {
        return Result.success("search service ok");
    }
}

