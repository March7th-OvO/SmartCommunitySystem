package com.neusoft.smartcommunity.community.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.smartcommunity.community.entity.CommParkingSpace;

import java.util.List;

/** 车位服务 */
public interface CommParkingSpaceService extends IService<CommParkingSpace> {

    List<CommParkingSpace> listByCommunity(Long communityId, Integer status);
}
