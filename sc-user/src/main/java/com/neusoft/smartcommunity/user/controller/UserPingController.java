package com.neusoft.smartcommunity.user.controller;

import com.neusoft.smartcommunity.common.api.Result;
import com.neusoft.smartcommunity.user.dto.ChangePasswordRequest;
import com.neusoft.smartcommunity.user.dto.UpdateProfileRequest;
import com.neusoft.smartcommunity.user.dto.UserProfileResponse;
import com.neusoft.smartcommunity.user.entity.UcUser;
import com.neusoft.smartcommunity.user.service.UserProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 用户服务心跳接口
 */
@RestController
@RequestMapping("/user")
public class UserPingController {

    private final UserProfileService userProfileService;

    public UserPingController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    /**
     * 心跳接口
     *
     * @return 统一响应
     */
    @GetMapping("/ping")
    public Result<String> ping() {
        return Result.success("user service ok");
    }

    /**
     * 查询当前登录用户的个人资料与钱包信息。
     *
     * @param request HTTP 请求对象，包含网关透传的用户ID
     * @return 用户资料与钱包信息
     */
    @GetMapping("/profile")
    public Result<UserProfileResponse> profile(HttpServletRequest request) {
        Long userId = parseUserId(request);
        UcUser user = userProfileService.getUserById(userId);
        UserProfileResponse resp = new UserProfileResponse();
        resp.setUserId(user.getId());
        resp.setPhone(user.getPhone());
        resp.setNickname(user.getNickname());
        resp.setAvatar(user.getAvatar());
        resp.setGender(user.getGender());
        resp.setUserType(user.getUserType());
        resp.setWalletBalance(userProfileService.getWalletBalance(userId));
        return Result.success(resp);
    }

    /**
     * 更新当前登录用户的个人资料。
     *
     * @param request HTTP 请求对象
     * @param updateRequest 更新请求体
     * @return 统一响应
     */
    @PutMapping("/profile")
    public Result<Void> updateProfile(HttpServletRequest request,
                                      @Valid @RequestBody UpdateProfileRequest updateRequest) {
        Long userId = parseUserId(request);
        UcUser user = userProfileService.getUserById(userId);
        if (updateRequest.getNickname() != null) {
            user.setNickname(updateRequest.getNickname());
        }
        if (updateRequest.getAvatar() != null) {
            user.setAvatar(updateRequest.getAvatar());
        }
        if (updateRequest.getGender() != null) {
            user.setGender(updateRequest.getGender());
        }
        userProfileService.updateProfile(user);
        return Result.success();
    }

    /**
     * 修改当前登录用户密码。
     *
     * @param request HTTP 请求对象
     * @param changeRequest 旧密码与新密码
     * @return 统一响应
     */
    @PutMapping("/password")
    public Result<Void> changePassword(HttpServletRequest request,
                                       @Valid @RequestBody ChangePasswordRequest changeRequest) {
        Long userId = parseUserId(request);
        userProfileService.changePassword(userId, changeRequest.getOldPassword(), changeRequest.getNewPassword());
        return Result.success();
    }

    private Long parseUserId(HttpServletRequest request) {
        String userIdHeader = request.getHeader("X-User-Id");
        if (userIdHeader == null) {
            throw new com.neusoft.smartcommunity.common.exception.BusinessException(
                    com.neusoft.smartcommunity.common.api.ResultCode.UNAUTHORIZED,
                    "未获取到用户身份，请检查网关配置");
        }
        return Long.parseLong(userIdHeader);
    }
}

