package com.common.core.vo.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description 登录响应视图对象，包含token和用户基本信息
 * @Date 2025/1/7 16:40
 * @Author SparkFan
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录响应对象")
public class LoginVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 访问令牌
     */
    @Schema(description = "访问令牌", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String accessToken;

    /**
     * 刷新令牌
     */
    @Schema(description = "刷新令牌", example = "eyJhbGciOiJIUzI1NiJ9...")
    private String refreshToken;

    /**
     * 令牌类型
     */
    @Builder.Default
    @Schema(description = "令牌类型", example = "Bearer")
    private String tokenType = "Bearer";

    /**
     * 令牌过期时间（秒）
     */
    @Schema(description = "令牌过期时间（秒）", example = "7200")
    private Long expiresIn;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1")
    private Long userId;

    /**
     * 用户名
     */
    @Schema(description = "用户名", example = "admin")
    private String username;

    /**
     * 用户昵称
     */
    @Schema(description = "用户昵称", example = "管理员")
    private String nickname;

    /**
     * 头像地址
     */
    @Schema(description = "头像地址", example = "https://example.com/avatar.jpg")
    private String avatar;

    /**
     * 邮箱地址
     */
    @Schema(description = "邮箱地址", example = "admin@example.com")
    private String email;

    /**
     * 手机号码
     */
    @Schema(description = "手机号码", example = "13800138000")
    private String phone;

    /**
     * 用户状态
     */
    @Schema(description = "用户状态", example = "1")
    private Integer status;

    /**
     * 角色列表
     */
    @Schema(description = "角色列表")
    private List<String> roles;

    /**
     * 权限列表
     */
    @Schema(description = "权限列表")
    private List<String> permissions;

    /**
     * 是否为超级管理员
     */
    @Builder.Default
    @Schema(description = "是否为超级管理员", example = "false")
    private Boolean superAdmin = false;

    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "登录时间", example = "2024-01-01 12:00:00")
    private LocalDateTime loginTime;

    /**
     * 登录IP
     */
    @Schema(description = "登录IP", example = "192.168.1.1")
    private String loginIp;

    /**
     * 上次登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "上次登录时间", example = "2024-01-01 10:00:00")
    private LocalDateTime lastLoginTime;

    /**
     * 显示名称
     */
    @Schema(description = "显示名称", example = "管理员")
    private String displayName;

    /**
     * 首次登录标识
     */
    @Builder.Default
    @Schema(description = "是否首次登录", example = "false")
    private Boolean firstLogin = false;

    /**
     * 会话ID
     */
    @Schema(description = "会话ID", example = "session-uuid")
    private String sessionId;

    /**
     * 登录成功消息
     */
    @Builder.Default
    @Schema(description = "登录成功消息", example = "登录成功")
    private String message = "登录成功";
} 