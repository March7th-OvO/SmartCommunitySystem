package com.neusoft.smartcommunity.community.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.smartcommunity.community.entity.CommNotice;

import java.util.List;

/** 公告服务 */
public interface CommNoticeService extends IService<CommNotice> {

    /** 用户端：查询已发布公告列表（可按小区） */
    List<CommNotice> listPublished(Long communityId);

    /** 管理端：分页 */
    Page<CommNotice> pageAdmin(int pageNum, int pageSize, Long communityId, Integer status);
}
