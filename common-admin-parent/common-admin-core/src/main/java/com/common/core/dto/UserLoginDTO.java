package com.common.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * @Description 用户登录DTO，用于接收用户登录请求参数
 * @Date 2025/1/7 16:05
 * @Author SparkFan
 */
@Data
@Schema(description = "用户登录DTO")
public class UserLoginDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名（支持用户名/邮箱/手机号登录）
     */
    @NotBlank(message = "用户名不能为空")
    @Size(max = 100, message = "用户名长度不能超过100个字符")
    @Schema(description = "用户名（支持用户名/邮箱/手机号）", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    @Schema(description = "密码", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    /**
     * 验证码（如果需要）
     */
    @Schema(description = "验证码", example = "abcd")
    private String captcha;

    /**
     * 验证码key（如果需要）
     */
    @Schema(description = "验证码key", example = "uuid-key")
    private String captchaKey;

    /**
     * 记住我
     */
    @Schema(description = "记住我", example = "false")
    private Boolean rememberMe = false;
} 