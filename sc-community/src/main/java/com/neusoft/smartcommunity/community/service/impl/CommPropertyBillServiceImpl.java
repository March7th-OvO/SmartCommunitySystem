package com.neusoft.smartcommunity.community.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.neusoft.smartcommunity.common.api.ResultCode;
import com.neusoft.smartcommunity.common.exception.BusinessException;
import com.neusoft.smartcommunity.community.client.UserWalletClient;
import com.neusoft.smartcommunity.community.entity.CommPropertyBill;
import com.neusoft.smartcommunity.community.mapper.CommPropertyBillMapper;
import com.neusoft.smartcommunity.community.service.CommPropertyBillService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CommPropertyBillServiceImpl extends ServiceImpl<CommPropertyBillMapper, CommPropertyBill> implements CommPropertyBillService {

    private final UserWalletClient userWalletClient;

    public CommPropertyBillServiceImpl(UserWalletClient userWalletClient) {
        this.userWalletClient = userWalletClient;
    }

    @Override
    public Page<CommPropertyBill> pageByUser(Long userId, int pageNum, int pageSize, Integer status) {
        return page(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<CommPropertyBill>()
                        .eq(CommPropertyBill::getUserId, userId)
                        .eq(status != null, CommPropertyBill::getStatus, status)
                        .orderByDesc(CommPropertyBill::getBillPeriod));
    }

    @Override
    public Page<CommPropertyBill> pageAdmin(int pageNum, int pageSize, Long communityId, String billPeriod, Integer status) {
        return page(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<CommPropertyBill>()
                        .eq(communityId != null, CommPropertyBill::getCommunityId, communityId)
                        .eq(billPeriod != null, CommPropertyBill::getBillPeriod, billPeriod)
                        .eq(status != null, CommPropertyBill::getStatus, status)
                        .orderByDesc(CommPropertyBill::getBillPeriod));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pay(Long userId, Long billId) {
        CommPropertyBill bill = getById(billId);
        if (bill == null) throw new BusinessException(ResultCode.BUSINESS_ERROR, "账单不存在");
        if (!bill.getUserId().equals(userId)) throw new BusinessException(ResultCode.FORBIDDEN, "无权操作");
        if (bill.getStatus() != CommPropertyBill.STATUS_UNPAID) throw new BusinessException(ResultCode.BUSINESS_ERROR, "账单已缴或已作废");
        userWalletClient.deduct(userId, bill.getAmount(), "PROPERTY_PAY", billId, "物业费-" + bill.getBillPeriod());
        bill.setStatus(CommPropertyBill.STATUS_PAID);
        bill.setPayTime(LocalDateTime.now());
        bill.setUpdatedAt(LocalDateTime.now());
        updateById(bill);
    }

    @Override
    public void createBill(CommPropertyBill bill) {
        bill.setStatus(CommPropertyBill.STATUS_UNPAID);
        bill.setCreatedAt(LocalDateTime.now());
        bill.setUpdatedAt(LocalDateTime.now());
        if (bill.getBillType() == null) bill.setBillType("PROPERTY_FEE");
        save(bill);
    }
}
