package com.neusoft.smartcommunity.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.smartcommunity.community.entity.CommVehicle;

import java.util.List;

/** 车辆绑定服务（用户绑定车牌/车位） */
public interface CommVehicleService extends IService<CommVehicle> {

    List<CommVehicle> listByUserId(Long userId);

    List<CommVehicle> listByUserIdAndCommunity(Long userId, Long communityId);

    /** 绑定车辆（同一小区+车牌唯一） */
    void bind(Long userId, Long communityId, String plateNumber, Long parkingSpaceId);

    void unbind(Long id, Long userId);
}
