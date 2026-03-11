package com.neusoft.smartcommunity.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.smartcommunity.common.api.ResultCode;
import com.neusoft.smartcommunity.common.exception.BusinessException;
import com.neusoft.smartcommunity.community.entity.CommVehicle;
import com.neusoft.smartcommunity.community.mapper.CommVehicleMapper;
import com.neusoft.smartcommunity.community.service.CommVehicleService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommVehicleServiceImpl extends ServiceImpl<CommVehicleMapper, CommVehicle> implements CommVehicleService {

    @Override
    public List<CommVehicle> listByUserId(Long userId) {
        return list(new LambdaQueryWrapper<CommVehicle>().eq(CommVehicle::getUserId, userId));
    }

    @Override
    public List<CommVehicle> listByUserIdAndCommunity(Long userId, Long communityId) {
        return list(new LambdaQueryWrapper<CommVehicle>()
                .eq(CommVehicle::getUserId, userId)
                .eq(CommVehicle::getCommunityId, communityId));
    }

    @Override
    public void bind(Long userId, Long communityId, String plateNumber, Long parkingSpaceId) {
        long c = count(new LambdaQueryWrapper<CommVehicle>()
                .eq(CommVehicle::getCommunityId, communityId)
                .eq(CommVehicle::getPlateNumber, plateNumber));
        if (c > 0) throw new BusinessException(ResultCode.BUSINESS_ERROR, "该小区已存在此车牌");
        CommVehicle v = new CommVehicle();
        v.setUserId(userId);
        v.setCommunityId(communityId);
        v.setParkingSpaceId(parkingSpaceId);
        v.setPlateNumber(plateNumber);
        v.setStatus(1);
        v.setCreatedAt(LocalDateTime.now());
        v.setUpdatedAt(LocalDateTime.now());
        save(v);
    }

    @Override
    public void unbind(Long id, Long userId) {
        CommVehicle v = getById(id);
        if (v == null || !v.getUserId().equals(userId)) throw new BusinessException(ResultCode.BUSINESS_ERROR, "记录不存在或无权操作");
        removeById(id);
    }
}
