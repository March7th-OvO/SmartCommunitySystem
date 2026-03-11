package com.neusoft.smartcommunity.community.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/** 报事报修实体，对应表 comm_repair */
@TableName("comm_repair")
public class CommRepair implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long communityId;
    private Long userId;
    private String houseInfo;
    private String category;
    private String title;
    private String content;
    private String images;
    private Integer status;
    private Long handlerUserId;
    private LocalDateTime appointmentTime;
    private LocalDateTime handleTime;
    private LocalDateTime finishTime;
    private Integer rateScore;
    private String rateContent;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 状态：0待受理 1处理中 2已完成 3已取消 */
    public static final int STATUS_PENDING = 0;
    public static final int STATUS_PROCESSING = 1;
    public static final int STATUS_FINISHED = 2;
    public static final int STATUS_CANCELLED = 3;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCommunityId() { return communityId; }
    public void setCommunityId(Long communityId) { this.communityId = communityId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getHouseInfo() { return houseInfo; }
    public void setHouseInfo(String houseInfo) { this.houseInfo = houseInfo; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getImages() { return images; }
    public void setImages(String images) { this.images = images; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Long getHandlerUserId() { return handlerUserId; }
    public void setHandlerUserId(Long handlerUserId) { this.handlerUserId = handlerUserId; }
    public LocalDateTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalDateTime appointmentTime) { this.appointmentTime = appointmentTime; }
    public LocalDateTime getHandleTime() { return handleTime; }
    public void setHandleTime(LocalDateTime handleTime) { this.handleTime = handleTime; }
    public LocalDateTime getFinishTime() { return finishTime; }
    public void setFinishTime(LocalDateTime finishTime) { this.finishTime = finishTime; }
    public Integer getRateScore() { return rateScore; }
    public void setRateScore(Integer rateScore) { this.rateScore = rateScore; }
    public String getRateContent() { return rateContent; }
    public void setRateContent(String rateContent) { this.rateContent = rateContent; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
