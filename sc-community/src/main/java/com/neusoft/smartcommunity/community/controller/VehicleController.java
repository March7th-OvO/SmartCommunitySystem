package com.neusoft.smartcommunity.community.controller;

import com.neusoft.smartcommunity.common.api.Result;
import com.neusoft.smartcommunity.common.exception.BusinessException;
import com.neusoft.smartcommunity.common.api.ResultCode;
import com.neusoft.smartcommunity.community.entity.CommVehicle;
import com.neusoft.smartcommunity.community.service.CommVehicleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/** 车辆/车位绑定：用户绑定车牌、我的车辆列表 */
@RestController
@RequestMapping("/community/vehicle")
public class VehicleController {

    private final CommVehicleService vehicleService;

    public VehicleController(CommVehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    private Long getUserId(HttpServletRequest request) {
        String id = request.getHeader("X-User-Id");
        if (id == null) throw new BusinessException(ResultCode.UNAUTHORIZED, "未登录");
        return Long.parseLong(id);
    }

    @GetMapping("/my")
    public Result<List<CommVehicle>> my(HttpServletRequest request,
                                       @RequestParam(required = false) Long communityId) {
        Long userId = getUserId(request);
        if (communityId != null) {
            return Result.success(vehicleService.listByUserIdAndCommunity(userId, communityId));
        }
        return Result.success(vehicleService.listByUserId(userId));
    }

    @PostMapping("/bind")
    public Result<Void> bind(HttpServletRequest request, @RequestBody Map<String, Object> body) {
        Long userId = getUserId(request);
        Long communityId = body.get("communityId") != null ? Long.valueOf(body.get("communityId").toString()) : null;
        String plateNumber = (String) body.get("plateNumber");
        Long parkingSpaceId = body.get("parkingSpaceId") != null ? Long.valueOf(body.get("parkingSpaceId").toString()) : null;
        if (communityId == null || plateNumber == null) throw new BusinessException(ResultCode.VALIDATE_ERROR, "缺少参数");
        vehicleService.bind(userId, communityId, plateNumber, parkingSpaceId);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> unbind(HttpServletRequest request, @PathVariable Long id) {
        vehicleService.unbind(id, getUserId(request));
        return Result.success();
    }
}
