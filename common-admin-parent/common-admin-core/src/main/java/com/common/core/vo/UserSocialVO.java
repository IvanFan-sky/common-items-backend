package com.common.core.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description 用户社交登录视图对象，用于返回给前端的社交账号信息
 * @Date 2025/1/7 15:30
 * @Author SparkFan
 */
@Data
@Schema(description = "用户社交登录视图对象")
public class UserSocialVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 社交登录记录ID
     */
    @Schema(description = "社交登录记录ID", example = "1")
    private Long id;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1")
    private Long userId;

    /**
     * 社交平台类型
     */
    @Schema(description = "社交平台类型", example = "wechat")
    private String socialType;

    /**
     * 社交平台名称
     */
    @Schema(description = "社交平台名称", example = "微信")
    private String socialTypeName;

    /**
     * 社交平台用户标识（脱敏）
     */
    @Schema(description = "社交平台用户标识", example = "open****3456")
    private String socialIdMasked;

    /**
     * 社交平台昵称
     */
    @Schema(description = "社交平台昵称", example = "微信用户")
    private String socialNickname;

    /**
     * 社交平台头像
     */
    @Schema(description = "社交平台头像", example = "https://wx.qlogo.cn/mmopen/avatar.jpg")
    private String socialAvatar;

    /**
     * 社交平台邮箱（脱敏）
     */
    @Schema(description = "社交平台邮箱", example = "us***@example.com")
    private String socialEmailMasked;

    /**
     * 社交平台手机号（脱敏）
     */
    @Schema(description = "社交平台手机号", example = "138****8000")
    private String socialPhoneMasked;

    /**
     * 社交平台性别
     */
    @Schema(description = "社交平台性别", example = "1", allowableValues = {"0", "1", "2"})
    private Integer socialGender;

    /**
     * 社交平台性别描述
     */
    @Schema(description = "社交平台性别描述", example = "男")
    private String socialGenderText;

    /**
     * 绑定时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "绑定时间", example = "2024-01-01 12:00:00")
    private LocalDateTime bindTime;

    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "最后登录时间", example = "2024-01-01 12:00:00")
    private LocalDateTime lastLoginTime;

    /**
     * 登录次数
     */
    @Schema(description = "登录次数", example = "10")
    private Integer loginCount;

    /**
     * 状态（0-解绑，1-已绑定）
     */
    @Schema(description = "状态", example = "1", allowableValues = {"0", "1"})
    private Integer status;

    /**
     * 状态描述
     */
    @Schema(description = "状态描述", example = "已绑定")
    private String statusText;

    /**
     * 令牌是否过期
     */
    @Schema(description = "令牌是否过期", example = "false")
    private Boolean tokenExpired = false;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "2024-01-01 12:00:00")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "2024-01-01 12:00:00")
    private LocalDateTime updateTime;

    /**
     * 备注
     */
    @Schema(description = "备注", example = "微信绑定")
    private String remark;

    /**
     * 显示名称
     */
    @Schema(description = "显示名称", example = "微信用户")
    private String displayName;

    /**
     * 平台图标
     */
    @Schema(description = "平台图标", example = "wechat-icon.png")
    private String platformIcon;

    /**
     * 是否主要登录方式
     */
    @Schema(description = "是否主要登录方式", example = "true")
    private Boolean isPrimary = false;
} 