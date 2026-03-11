package com.neusoft.smartcommunity.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.smartcommunity.community.entity.CommNotice;
import com.neusoft.smartcommunity.community.mapper.CommNoticeMapper;
import com.neusoft.smartcommunity.community.service.CommNoticeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommNoticeServiceImpl extends ServiceImpl<CommNoticeMapper, CommNotice> implements CommNoticeService {

    @Override
    public List<CommNotice> listPublished(Long communityId) {
        LambdaQueryWrapper<CommNotice> w = new LambdaQueryWrapper<CommNotice>()
                .eq(CommNotice::getStatus, 1)
                .orderByDesc(CommNotice::getPublishTime);
        if (communityId != null) {
            w.and(q -> q.eq(CommNotice::getCommunityId, communityId).or().isNull(CommNotice::getCommunityId));
        }
        return list(w);
    }

    @Override
    public Page<CommNotice> pageAdmin(int pageNum, int pageSize, Long communityId, Integer status) {
        return page(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<CommNotice>()
                        .eq(communityId != null, CommNotice::getCommunityId, communityId)
                        .eq(status != null, CommNotice::getStatus, status)
                        .orderByDesc(CommNotice::getId));
    }
}
