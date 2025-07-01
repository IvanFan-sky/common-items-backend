package com.common.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * @Description 用户社交账号绑定DTO，用于第三方登录绑定和解绑功能
 * @Date 2025/1/7 15:30
 * @Author SparkFan
 */
@Data
@Schema(description = "用户社交账号绑定DTO")
public class UserSocialBindDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    /**
     * 社交平台类型
     */
    @NotBlank(message = "社交平台类型不能为空")
    @Size(max = 20, message = "社交平台类型长度不能超过20个字符")
    @Schema(description = "社交平台类型", example = "wechat", requiredMode = Schema.RequiredMode.REQUIRED)
    private String socialType;

    /**
     * 社交平台用户标识
     */
    @NotBlank(message = "社交平台用户标识不能为空")
    @Size(max = 100, message = "社交平台用户标识长度不能超过100个字符")
    @Schema(description = "社交平台用户标识", example = "openid_123456", requiredMode = Schema.RequiredMode.REQUIRED)
    private String socialId;

    /**
     * 社交平台昵称
     */
    @Size(max = 100, message = "社交平台昵称长度不能超过100个字符")
    @Schema(description = "社交平台昵称", example = "微信用户")
    private String socialNickname;

    /**
     * 社交平台头像
     */
    @Size(max = 200, message = "社交平台头像长度不能超过200个字符")
    @Schema(description = "社交平台头像", example = "https://wx.qlogo.cn/mmopen/avatar.jpg")
    private String socialAvatar;

    /**
     * 社交平台邮箱
     */
    @Size(max = 100, message = "社交平台邮箱长度不能超过100个字符")
    @Schema(description = "社交平台邮箱", example = "user@example.com")
    private String socialEmail;

    /**
     * 社交平台手机号
     */
    @Size(max = 20, message = "社交平台手机号长度不能超过20个字符")
    @Schema(description = "社交平台手机号", example = "13800138000")
    private String socialPhone;

    /**
     * 社交平台性别（0-未知，1-男，2-女）
     */
    @Schema(description = "社交平台性别", example = "1", allowableValues = {"0", "1", "2"})
    private Integer socialGender;

    /**
     * 微信UnionID（用于多应用统一身份）
     */
    @Size(max = 100, message = "UnionID长度不能超过100个字符")
    @Schema(description = "微信UnionID", example = "unionid_123456")
    private String unionId;

    /**
     * 微信OpenID
     */
    @Size(max = 100, message = "OpenID长度不能超过100个字符")
    @Schema(description = "微信OpenID", example = "openid_123456")
    private String openId;

    /**
     * 访问令牌
     */
    @Size(max = 500, message = "访问令牌长度不能超过500个字符")
    @Schema(description = "访问令牌")
    private String accessToken;

    /**
     * 刷新令牌
     */
    @Size(max = 500, message = "刷新令牌长度不能超过500个字符")
    @Schema(description = "刷新令牌")
    private String refreshToken;

    /**
     * 令牌过期时间戳
     */
    @Schema(description = "令牌过期时间戳", example = "7200")
    private Long expiresIn;

    /**
     * 授权范围
     */
    @Size(max = 200, message = "授权范围长度不能超过200个字符")
    @Schema(description = "授权范围", example = "snsapi_userinfo")
    private String scope;

    /**
     * 原始用户信息JSON
     */
    @Schema(description = "原始用户信息JSON")
    private String rawUserInfo;

    /**
     * 是否同步用户信息到主账号
     */
    @Schema(description = "是否同步用户信息到主账号", example = "true")
    private Boolean syncUserInfo = false;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500个字符")
    @Schema(description = "备注", example = "微信绑定")
    private String remark;
} 