package com.neusoft.smartcommunity.community.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.neusoft.smartcommunity.community.entity.CommPropertyBill;

/** 物业费账单服务 */
public interface CommPropertyBillService extends IService<CommPropertyBill> {

    Page<CommPropertyBill> pageByUser(Long userId, int pageNum, int pageSize, Integer status);

    Page<CommPropertyBill> pageAdmin(int pageNum, int pageSize, Long communityId, String billPeriod, Integer status);

    /** 用户缴纳：扣钱包并更新账单状态 */
    void pay(Long userId, Long billId);

    /** 管理端：创建账单 */
    void createBill(CommPropertyBill bill);
}
