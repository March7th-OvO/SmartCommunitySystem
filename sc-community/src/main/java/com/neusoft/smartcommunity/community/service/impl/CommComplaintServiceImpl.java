package com.neusoft.smartcommunity.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.smartcommunity.common.api.ResultCode;
import com.neusoft.smartcommunity.common.exception.BusinessException;
import com.neusoft.smartcommunity.community.entity.CommComplaint;
import com.neusoft.smartcommunity.community.mapper.CommComplaintMapper;
import com.neusoft.smartcommunity.community.service.CommComplaintService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommComplaintServiceImpl extends ServiceImpl<CommComplaintMapper, CommComplaint> implements CommComplaintService {

    @Override
    public void submit(CommComplaint complaint) {
        complaint.setStatus(CommComplaint.STATUS_PENDING);
        complaint.setCreatedAt(LocalDateTime.now());
        complaint.setUpdatedAt(LocalDateTime.now());
        save(complaint);
    }

    @Override
    public Page<CommComplaint> pageByUser(Long userId, int pageNum, int pageSize, Integer status) {
        return page(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<CommComplaint>()
                        .eq(CommComplaint::getUserId, userId)
                        .eq(status != null, CommComplaint::getStatus, status)
                        .orderByDesc(CommComplaint::getId));
    }

    @Override
    public Page<CommComplaint> pageAdmin(int pageNum, int pageSize, Long communityId, Integer status) {
        return page(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<CommComplaint>()
                        .eq(communityId != null, CommComplaint::getCommunityId, communityId)
                        .eq(status != null, CommComplaint::getStatus, status)
                        .orderByDesc(CommComplaint::getId));
    }

    @Override
    public void handle(Long id, Long handlerUserId, String handleResult) {
        CommComplaint c = getById(id);
        if (c == null) throw new BusinessException(ResultCode.BUSINESS_ERROR, "记录不存在");
        c.setStatus(CommComplaint.STATUS_FINISHED);
        c.setHandlerUserId(handlerUserId);
        c.setHandleResult(handleResult);
        c.setHandleTime(LocalDateTime.now());
        c.setUpdatedAt(LocalDateTime.now());
        updateById(c);
    }
}
