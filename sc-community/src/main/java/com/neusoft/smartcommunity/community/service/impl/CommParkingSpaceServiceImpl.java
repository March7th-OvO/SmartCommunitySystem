package com.neusoft.smartcommunity.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.smartcommunity.community.entity.CommParkingSpace;
import com.neusoft.smartcommunity.community.mapper.CommParkingSpaceMapper;
import com.neusoft.smartcommunity.community.service.CommParkingSpaceService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommParkingSpaceServiceImpl extends ServiceImpl<CommParkingSpaceMapper, CommParkingSpace> implements CommParkingSpaceService {

    @Override
    public List<CommParkingSpace> listByCommunity(Long communityId, Integer status) {
        return list(new LambdaQueryWrapper<CommParkingSpace>()
                .eq(CommParkingSpace::getCommunityId, communityId)
                .eq(status != null, CommParkingSpace::getStatus, status)
                .orderByAsc(CommParkingSpace::getCode));
    }
}
