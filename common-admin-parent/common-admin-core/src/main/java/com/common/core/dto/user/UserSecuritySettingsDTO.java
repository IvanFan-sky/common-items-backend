package com.common.core.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * @Description 用户安全设置DTO，用于安全相关设置的更新
 * @Date 2025/1/7 15:30
 * @Author SparkFan
 */
@Data
@Schema(description = "用户安全设置DTO")
public class UserSecuritySettingsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 是否启用双因子认证
     */
    @Schema(description = "是否启用双因子认证", example = "true")
    private Boolean twoFactorEnabled;

    /**
     * 双因子认证密钥（TOTP）
     */
    @Size(max = 32, message = "双因子认证密钥长度不能超过32个字符")
    @Schema(description = "双因子认证密钥", example = "JBSWY3DPEHPK3PXP")
    private String twoFactorSecret;

    /**
     * 验证码（用于启用/禁用双因子认证时验证）
     */
    @Size(min = 6, max = 6, message = "验证码必须为6位数字")
    @Schema(description = "验证码", example = "123456")
    private String verificationCode;

    /**
     * 是否允许多设备同时登录
     */
    @Schema(description = "是否允许多设备同时登录", example = "false")
    private Boolean allowMultipleLogin;

    /**
     * 登录IP白名单（JSON格式）
     */
    @Size(max = 1000, message = "IP白名单长度不能超过1000个字符")
    @Schema(description = "登录IP白名单", example = "[\"192.168.1.1\", \"10.0.0.1\"]")
    private String ipWhitelist;

    /**
     * 会话超时时间（分钟）
     */
    @Schema(description = "会话超时时间（分钟）", example = "30")
    private Integer sessionTimeout;
} 