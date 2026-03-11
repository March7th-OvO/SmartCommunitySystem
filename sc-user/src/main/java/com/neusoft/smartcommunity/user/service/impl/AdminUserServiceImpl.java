package com.neusoft.smartcommunity.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.neusoft.smartcommunity.common.api.PageResult;
import com.neusoft.smartcommunity.common.api.ResultCode;
import com.neusoft.smartcommunity.common.exception.BusinessException;
import com.neusoft.smartcommunity.user.dto.AdminAssignRolesRequest;
import com.neusoft.smartcommunity.user.dto.AdminUpdateStatusRequest;
import com.neusoft.smartcommunity.user.dto.RoleOptionDto;
import com.neusoft.smartcommunity.user.dto.UserListItemDto;
import com.neusoft.smartcommunity.user.entity.SysRole;
import com.neusoft.smartcommunity.user.entity.SysUserRole;
import com.neusoft.smartcommunity.user.entity.UcUser;
import com.neusoft.smartcommunity.user.mapper.SysRoleMapper;
import com.neusoft.smartcommunity.user.mapper.SysUserRoleMapper;
import com.neusoft.smartcommunity.user.mapper.UcUserMapper;
import com.neusoft.smartcommunity.user.service.AdminUserService;
import com.neusoft.smartcommunity.user.service.UserProfileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 管理端用户与角色管理服务实现。
 */
@Service
public class AdminUserServiceImpl implements AdminUserService {

    /** 用户类型：社区管理员、平台管理员 可执行管理端操作 */
    private static final int USER_TYPE_COMMUNITY_ADMIN = 3;
    private static final int USER_TYPE_PLATFORM_ADMIN = 9;

    private final UcUserMapper ucUserMapper;
    private final SysRoleMapper sysRoleMapper;
    private final SysUserRoleMapper sysUserRoleMapper;
    private final UserProfileService userProfileService;

    public AdminUserServiceImpl(UcUserMapper ucUserMapper, SysRoleMapper sysRoleMapper,
                                SysUserRoleMapper sysUserRoleMapper, UserProfileService userProfileService) {
        this.ucUserMapper = ucUserMapper;
        this.sysRoleMapper = sysRoleMapper;
        this.sysUserRoleMapper = sysUserRoleMapper;
        this.userProfileService = userProfileService;
    }

    @Override
    public PageResult<UserListItemDto> listUsers(int pageNum, int pageSize, String phone, Integer status, Integer userType) {
        LambdaQueryWrapper<UcUser> wrapper = new LambdaQueryWrapper<UcUser>()
                .like(StringUtils.hasText(phone), UcUser::getPhone, phone)
                .eq(status != null, UcUser::getStatus, status)
                .eq(userType != null, UcUser::getUserType, userType)
                .orderByDesc(UcUser::getId);
        Page<UcUser> page = ucUserMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        List<UcUser> records = page.getRecords();
        if (records == null || records.isEmpty()) {
            return new PageResult<>(page.getTotal(), Collections.emptyList());
        }
        List<Long> userIds = records.stream().map(UcUser::getId).collect(Collectors.toList());
        Map<Long, List<Long>> userIdToRoleIds = getRoleIdsByUserIds(userIds);
        Map<Long, String> roleIdToName = getRoleIdToName(userIdToRoleIds.values().stream()
                .flatMap(List::stream).distinct().collect(Collectors.toList()));
        List<UserListItemDto> list = new ArrayList<>();
        for (UcUser u : records) {
            UserListItemDto dto = new UserListItemDto();
            dto.setUserId(u.getId());
            dto.setPhone(u.getPhone());
            dto.setNickname(u.getNickname());
            dto.setUserType(u.getUserType());
            dto.setStatus(u.getStatus());
            dto.setCreatedAt(u.getCreatedAt());
            List<Long> rids = userIdToRoleIds.getOrDefault(u.getId(), Collections.emptyList());
            dto.setRoleIds(rids);
            dto.setRoleNames(rids.stream().map(roleIdToName::get).collect(Collectors.toList()));
            list.add(dto);
        }
        return new PageResult<>(page.getTotal(), list);
    }

    @Override
    public void updateUserStatus(Long operatorUserId, AdminUpdateStatusRequest request) {
        ensureAdmin(operatorUserId);
        UcUser target = userProfileService.getUserById(request.getUserId());
        target.setStatus(request.getStatus());
        target.setUpdatedAt(LocalDateTime.now());
        ucUserMapper.updateById(target);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoles(Long operatorUserId, AdminAssignRolesRequest request) {
        ensureAdmin(operatorUserId);
        userProfileService.getUserById(request.getUserId());
        sysUserRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, request.getUserId()));
        if (CollectionUtils.isNotEmpty(request.getRoleIds())) {
            LocalDateTime now = LocalDateTime.now();
            for (Long roleId : request.getRoleIds()) {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(request.getUserId());
                ur.setRoleId(roleId);
                ur.setCreatedAt(now);
                sysUserRoleMapper.insert(ur);
            }
        }
    }

    @Override
    public List<RoleOptionDto> listRoleOptions() {
        List<SysRole> roles = sysRoleMapper.selectList(
                new LambdaQueryWrapper<SysRole>().eq(SysRole::getStatus, 1).orderByAsc(SysRole::getId));
        if (roles == null) return Collections.emptyList();
        return roles.stream().map(r -> {
            RoleOptionDto dto = new RoleOptionDto();
            dto.setRoleId(r.getId());
            dto.setRoleCode(r.getRoleCode());
            dto.setRoleName(r.getRoleName());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public boolean isAdmin(Long userId) {
        UcUser user = ucUserMapper.selectById(userId);
        if (user == null || user.getDeleted() != null && user.getDeleted() == 1) return false;
        Integer type = user.getUserType();
        return type != null && (type == USER_TYPE_COMMUNITY_ADMIN || type == USER_TYPE_PLATFORM_ADMIN);
    }

    private void ensureAdmin(Long operatorUserId) {
        if (!isAdmin(operatorUserId)) {
            throw new BusinessException(ResultCode.FORBIDDEN, "没有权限执行该操作");
        }
    }

    private Map<Long, List<Long>> getRoleIdsByUserIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) return Collections.emptyMap();
        List<SysUserRole> list = sysUserRoleMapper.selectList(
                new LambdaQueryWrapper<SysUserRole>().in(SysUserRole::getUserId, userIds));
        if (list == null) return Collections.emptyMap();
        return list.stream().collect(Collectors.groupingBy(SysUserRole::getUserId,
                Collectors.mapping(SysUserRole::getRoleId, Collectors.toList())));
    }

    private Map<Long, String> getRoleIdToName(List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) return Collections.emptyMap();
        List<SysRole> roles = sysRoleMapper.selectBatchIds(roleIds);
        if (roles == null) return Collections.emptyMap();
        return roles.stream().collect(Collectors.toMap(SysRole::getId, r -> r.getRoleName() != null ? r.getRoleName() : ""));
    }
}
