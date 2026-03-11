package com.neusoft.smartcommunity.community.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.smartcommunity.community.entity.CommVisitor;

/** 访客登记服务 */
public interface CommVisitorService extends IService<CommVisitor> {

    /** 用户提交访客登记 */
    void submit(CommVisitor visitor);

    /** 用户查询自己的访客记录 */
    Page<CommVisitor> pageByHost(Long hostUserId, int pageNum, int pageSize);

    /** 管理端分页（按小区、状态） */
    Page<CommVisitor> pageAdmin(int pageNum, int pageSize, Long communityId, Integer status);

    /** 管理端审核：通过/拒绝 */
    void audit(Long id, Integer status, Long auditUserId, String remark);
}
