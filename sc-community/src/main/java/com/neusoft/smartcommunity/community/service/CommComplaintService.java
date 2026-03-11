package com.neusoft.smartcommunity.community.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.smartcommunity.community.entity.CommComplaint;

/** 投诉建议服务 */
public interface CommComplaintService extends IService<CommComplaint> {

    void submit(CommComplaint complaint);

    Page<CommComplaint> pageByUser(Long userId, int pageNum, int pageSize, Integer status);

    Page<CommComplaint> pageAdmin(int pageNum, int pageSize, Long communityId, Integer status);

    void handle(Long id, Long handlerUserId, String handleResult);
}
