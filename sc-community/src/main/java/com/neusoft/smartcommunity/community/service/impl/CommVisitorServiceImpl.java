package com.neusoft.smartcommunity.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.smartcommunity.common.api.ResultCode;
import com.neusoft.smartcommunity.common.exception.BusinessException;
import com.neusoft.smartcommunity.community.entity.CommVisitor;
import com.neusoft.smartcommunity.community.mapper.CommVisitorMapper;
import com.neusoft.smartcommunity.community.service.CommVisitorService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CommVisitorServiceImpl extends ServiceImpl<CommVisitorMapper, CommVisitor> implements CommVisitorService {

    @Override
    public void submit(CommVisitor visitor) {
        visitor.setStatus(CommVisitor.STATUS_PENDING);
        visitor.setCreatedAt(LocalDateTime.now());
        visitor.setUpdatedAt(LocalDateTime.now());
        save(visitor);
    }

    @Override
    public Page<CommVisitor> pageByHost(Long hostUserId, int pageNum, int pageSize) {
        return page(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<CommVisitor>()
                        .eq(CommVisitor::getHostUserId, hostUserId)
                        .orderByDesc(CommVisitor::getCreatedAt));
    }

    @Override
    public Page<CommVisitor> pageAdmin(int pageNum, int pageSize, Long communityId, Integer status) {
        return page(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<CommVisitor>()
                        .eq(communityId != null, CommVisitor::getCommunityId, communityId)
                        .eq(status != null, CommVisitor::getStatus, status)
                        .orderByDesc(CommVisitor::getCreatedAt));
    }

    @Override
    public void audit(Long id, Integer status, Long auditUserId, String remark) {
        CommVisitor v = getById(id);
        if (v == null) throw new BusinessException(ResultCode.BUSINESS_ERROR, "记录不存在");
        if (v.getStatus() != CommVisitor.STATUS_PENDING) throw new BusinessException(ResultCode.BUSINESS_ERROR, "已审核");
        v.setStatus(status);
        v.setAuditUserId(auditUserId);
        v.setAuditTime(LocalDateTime.now());
        v.setRemark(remark);
        v.setUpdatedAt(LocalDateTime.now());
        updateById(v);
    }
}
