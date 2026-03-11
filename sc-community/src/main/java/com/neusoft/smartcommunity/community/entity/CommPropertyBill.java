package com.neusoft.smartcommunity.community.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** 物业费账单实体，对应表 comm_property_bill */
@TableName("comm_property_bill")
public class CommPropertyBill implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long communityId;
    private Long userId;
    private String houseInfo;
    private String billPeriod;
    private String billType;
    private BigDecimal amount;
    private Integer status;
    private LocalDate dueDate;
    private LocalDateTime payTime;
    private Long payWalletFlowId;
    private String remark;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 状态：0未缴 1已缴 2已作废 */
    public static final int STATUS_UNPAID = 0;
    public static final int STATUS_PAID = 1;
    public static final int STATUS_VOID = 2;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCommunityId() { return communityId; }
    public void setCommunityId(Long communityId) { this.communityId = communityId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getHouseInfo() { return houseInfo; }
    public void setHouseInfo(String houseInfo) { this.houseInfo = houseInfo; }
    public String getBillPeriod() { return billPeriod; }
    public void setBillPeriod(String billPeriod) { this.billPeriod = billPeriod; }
    public String getBillType() { return billType; }
    public void setBillType(String billType) { this.billType = billType; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public LocalDateTime getPayTime() { return payTime; }
    public void setPayTime(LocalDateTime payTime) { this.payTime = payTime; }
    public Long getPayWalletFlowId() { return payWalletFlowId; }
    public void setPayWalletFlowId(Long payWalletFlowId) { this.payWalletFlowId = payWalletFlowId; }
    public String getRemark() { return remark; }
    public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
