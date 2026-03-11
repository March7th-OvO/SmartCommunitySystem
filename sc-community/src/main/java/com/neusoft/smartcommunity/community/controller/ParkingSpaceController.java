package com.neusoft.smartcommunity.community.controller;

import com.neusoft.smartcommunity.common.api.Result;
import com.neusoft.smartcommunity.community.entity.CommParkingSpace;
import com.neusoft.smartcommunity.community.service.CommParkingSpaceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 车位：管理端查询；用户端可选查看可选车位列表 */
@RestController
@RequestMapping("/community/parking")
public class ParkingSpaceController {

    private final CommParkingSpaceService parkingSpaceService;

    public ParkingSpaceController(CommParkingSpaceService parkingSpaceService) {
        this.parkingSpaceService = parkingSpaceService;
    }

    @GetMapping("/list")
    public Result<List<CommParkingSpace>> list(@RequestParam Long communityId,
                                               @RequestParam(required = false) Integer status) {
        return Result.success(parkingSpaceService.listByCommunity(communityId, status));
    }

    @GetMapping("/admin/list")
    public Result<List<CommParkingSpace>> adminList(@RequestParam Long communityId,
                                                    @RequestParam(required = false) Integer status) {
        return Result.success(parkingSpaceService.listByCommunity(communityId, status));
    }
}
