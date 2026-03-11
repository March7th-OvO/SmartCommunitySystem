package com.neusoft.smartcommunity.community.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.smartcommunity.community.entity.CommRepair;

/** 报事报修服务 */
public interface CommRepairService extends IService<CommRepair> {

    void submit(CommRepair repair);

    Page<CommRepair> pageByUser(Long userId, int pageNum, int pageSize, Integer status);

    Page<CommRepair> pageAdmin(int pageNum, int pageSize, Long communityId, Integer status);

    void startHandle(Long id, Long handlerUserId);

    void finish(Long id, Long handlerUserId);

    void cancel(Long id, Long userId);
}
