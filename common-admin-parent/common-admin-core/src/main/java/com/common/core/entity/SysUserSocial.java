package com.common.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.common.core.enums.Gender;
import com.common.core.enums.SocialType;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * @Description 系统用户社交登录实体类，存储第三方平台登录信息和令牌数据
 * @Date 2025/1/7 15:30
 * @Author SparkFan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user_social")
@Schema(description = "系统用户社交登录实体")
public class SysUserSocial extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    @TableField("user_id")
    @Schema(description = "用户ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    /**
     * 社交平台类型
     */
    @NotBlank(message = "社交平台类型不能为空")
    @Size(max = 20, message = "社交平台类型长度不能超过20个字符")
    @TableField("social_type")
    @Schema(description = "社交平台类型", example = "wechat", requiredMode = Schema.RequiredMode.REQUIRED)
    private String socialType;

    /**
     * 社交平台用户标识
     */
    @NotBlank(message = "社交平台用户标识不能为空")
    @Size(max = 100, message = "社交平台用户标识长度不能超过100个字符")
    @TableField("social_id")
    @Schema(description = "社交平台用户标识", example = "openid_123456", requiredMode = Schema.RequiredMode.REQUIRED)
    private String socialId;

    /**
     * 社交平台昵称
     */
    @Size(max = 100, message = "社交平台昵称长度不能超过100个字符")
    @TableField("social_nickname")
    @Schema(description = "社交平台昵称", example = "微信用户")
    private String socialNickname;

    /**
     * 社交平台头像
     */
    @Size(max = 200, message = "社交平台头像长度不能超过200个字符")
    @TableField("social_avatar")
    @Schema(description = "社交平台头像", example = "https://wx.qlogo.cn/mmopen/avatar.jpg")
    private String socialAvatar;

    /**
     * 社交平台邮箱
     */
    @Size(max = 100, message = "社交平台邮箱长度不能超过100个字符")
    @TableField("social_email")
    @Schema(description = "社交平台邮箱", example = "user@example.com")
    private String socialEmail;

    /**
     * 社交平台手机号
     */
    @Size(max = 20, message = "社交平台手机号长度不能超过20个字符")
    @TableField("social_phone")
    @Schema(description = "社交平台手机号", example = "13800138000")
    private String socialPhone;

    /**
     * 社交平台性别（0-未知，1-男，2-女）
     */
    @TableField("social_gender")
    @Schema(description = "社交平台性别", example = "1", allowableValues = {"0", "1", "2"})
    private Integer socialGender;

    /**
     * 微信UnionID（用于多应用统一身份）
     */
    @Size(max = 100, message = "UnionID长度不能超过100个字符")
    @TableField("union_id")
    @Schema(description = "微信UnionID", example = "unionid_123456")
    private String unionId;

    /**
     * 微信OpenID
     */
    @Size(max = 100, message = "OpenID长度不能超过100个字符")
    @TableField("open_id")
    @Schema(description = "微信OpenID", example = "openid_123456")
    private String openId;

    /**
     * 访问令牌
     */
    @Size(max = 500, message = "访问令牌长度不能超过500个字符")
    @TableField("access_token")
    @Schema(description = "访问令牌")
    private String accessToken;

    /**
     * 刷新令牌
     */
    @Size(max = 500, message = "刷新令牌长度不能超过500个字符")
    @TableField("refresh_token")
    @Schema(description = "刷新令牌")
    private String refreshToken;

    /**
     * 令牌过期时间戳
     */
    @TableField("expires_in")
    @Schema(description = "令牌过期时间戳", example = "7200")
    private Long expiresIn;

    /**
     * 授权范围
     */
    @Size(max = 200, message = "授权范围长度不能超过200个字符")
    @TableField("scope")
    @Schema(description = "授权范围", example = "snsapi_userinfo")
    private String scope;

    /**
     * 原始用户信息JSON
     */
    @TableField("raw_user_info")
    @Schema(description = "原始用户信息JSON")
    private String rawUserInfo;

    /**
     * 绑定时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("bind_time")
    @Schema(description = "绑定时间", example = "2024-01-01 12:00:00")
    private LocalDateTime bindTime;

    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("last_login_time")
    @Schema(description = "最后登录时间", example = "2024-01-01 12:00:00")
    private LocalDateTime lastLoginTime;

    /**
     * 登录次数
     */
    @TableField("login_count")
    @Schema(description = "登录次数", example = "10")
    private Integer loginCount;

    /**
     * 状态（0-解绑，1-已绑定）
     */
    @TableField("status")
    @Schema(description = "状态", example = "1", allowableValues = {"0", "1"})
    private Integer status;

    // ==================== 业务方法 ====================

    /**
     * 获取社交平台类型枚举
     */
    public SocialType getSocialTypeEnum() {
        return SocialType.getByCode(this.socialType);
    }

    /**
     * 设置社交平台类型枚举
     */
    public void setSocialTypeEnum(SocialType socialType) {
        this.socialType = socialType != null ? socialType.getCode() : null;
    }

    /**
     * 获取社交平台性别枚举
     */
    public Gender getSocialGenderEnum() {
        return Gender.getByCode(this.socialGender);
    }

    /**
     * 设置社交平台性别枚举
     */
    public void setSocialGenderEnum(Gender gender) {
        this.socialGender = gender != null ? gender.getCode() : Gender.UNKNOWN.getCode();
    }

    /**
     * 判断是否已绑定
     */
    public boolean isBound() {
        return Integer.valueOf(1).equals(this.status);
    }

    /**
     * 判断是否已解绑
     */
    public boolean isUnbound() {
        return Integer.valueOf(0).equals(this.status);
    }

    /**
     * 判断令牌是否过期
     */
    public boolean isTokenExpired() {
        if (expiresIn == null) {
            return true;
        }
        return System.currentTimeMillis() / 1000 > expiresIn;
    }

    /**
     * 获取显示名称（优先使用社交平台昵称）
     */
    public String getDisplayName() {
        if (socialNickname != null && !socialNickname.trim().isEmpty()) {
            return socialNickname;
        }
        SocialType type = getSocialTypeEnum();
        return type != null ? type.getName() + "用户" : "社交用户";
    }

    /**
     * 判断是否微信登录
     */
    public boolean isWechatLogin() {
        return SocialType.WECHAT.getCode().equals(this.socialType);
    }

    /**
     * 判断是否QQ登录
     */
    public boolean isQQLogin() {
        return SocialType.QQ.getCode().equals(this.socialType);
    }

    /**
     * 增加登录次数
     */
    public void incrementLoginCount() {
        this.loginCount = (this.loginCount == null ? 0 : this.loginCount) + 1;
        this.lastLoginTime = LocalDateTime.now();
    }

    /**
     * 判断是否有头像
     */
    public boolean hasSocialAvatar() {
        return socialAvatar != null && !socialAvatar.trim().isEmpty();
    }

    /**
     * 判断是否有邮箱
     */
    public boolean hasSocialEmail() {
        return socialEmail != null && !socialEmail.trim().isEmpty();
    }

    /**
     * 判断是否有手机号
     */
    public boolean hasSocialPhone() {
        return socialPhone != null && !socialPhone.trim().isEmpty();
    }
} 