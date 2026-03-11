package com.neusoft.smartcommunity.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.smartcommunity.common.api.ResultCode;
import com.neusoft.smartcommunity.common.exception.BusinessException;
import com.neusoft.smartcommunity.community.entity.CommRepair;
import com.neusoft.smartcommunity.community.mapper.CommRepairMapper;
import com.neusoft.smartcommunity.community.service.CommRepairService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommRepairServiceImpl extends ServiceImpl<CommRepairMapper, CommRepair> implements CommRepairService {

    @Override
    public void submit(CommRepair repair) {
        repair.setStatus(CommRepair.STATUS_PENDING);
        repair.setCreatedAt(LocalDateTime.now());
        repair.setUpdatedAt(LocalDateTime.now());
        save(repair);
    }

    @Override
    public Page<CommRepair> pageByUser(Long userId, int pageNum, int pageSize, Integer status) {
        return page(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<CommRepair>()
                        .eq(CommRepair::getUserId, userId)
                        .eq(status != null, CommRepair::getStatus, status)
                        .orderByDesc(CommRepair::getId));
    }

    @Override
    public Page<CommRepair> pageAdmin(int pageNum, int pageSize, Long communityId, Integer status) {
        return page(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<CommRepair>()
                        .eq(communityId != null, CommRepair::getCommunityId, communityId)
                        .eq(status != null, CommRepair::getStatus, status)
                        .orderByDesc(CommRepair::getId));
    }

    @Override
    public void startHandle(Long id, Long handlerUserId) {
        CommRepair r = getById(id);
        if (r == null) throw new BusinessException(ResultCode.BUSINESS_ERROR, "记录不存在");
        r.setStatus(CommRepair.STATUS_PROCESSING);
        r.setHandlerUserId(handlerUserId);
        r.setHandleTime(LocalDateTime.now());
        r.setUpdatedAt(LocalDateTime.now());
        updateById(r);
    }

    @Override
    public void finish(Long id, Long handlerUserId) {
        CommRepair r = getById(id);
        if (r == null) throw new BusinessException(ResultCode.BUSINESS_ERROR, "记录不存在");
        r.setStatus(CommRepair.STATUS_FINISHED);
        r.setHandlerUserId(handlerUserId);
        r.setFinishTime(LocalDateTime.now());
        r.setUpdatedAt(LocalDateTime.now());
        updateById(r);
    }

    @Override
    public void cancel(Long id, Long userId) {
        CommRepair r = getById(id);
        if (r == null || !r.getUserId().equals(userId)) throw new BusinessException(ResultCode.BUSINESS_ERROR, "记录不存在或无权操作");
        if (r.getStatus() != CommRepair.STATUS_PENDING) throw new BusinessException(ResultCode.BUSINESS_ERROR, "当前状态不可取消");
        r.setStatus(CommRepair.STATUS_CANCELLED);
        r.setUpdatedAt(LocalDateTime.now());
        updateById(r);
    }
}
