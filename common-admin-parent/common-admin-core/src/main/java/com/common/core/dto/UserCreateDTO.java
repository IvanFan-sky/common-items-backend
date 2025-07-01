package com.common.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @Description 用户创建DTO，用于用户注册和管理员添加用户功能
 * @Date 2025/1/7 15:30
 * @Author SparkFan
 */
@Data
@Schema(description = "用户创建DTO")
public class UserCreateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名（登录账号）
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 50, message = "用户名长度必须在2-50个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "用户名只能包含字母、数字、下划线和中划线")
    @Schema(description = "用户名", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{6,20}$", 
             message = "密码必须包含大小写字母和数字，长度6-20位")
    @Schema(description = "密码", example = "Admin123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    /**
     * 确认密码
     */
    @NotBlank(message = "确认密码不能为空")
    @Schema(description = "确认密码", example = "Admin123", requiredMode = Schema.RequiredMode.REQUIRED)
    private String confirmPassword;

    /**
     * 用户昵称
     */
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    @Schema(description = "用户昵称", example = "管理员")
    private String nickname;

    /**
     * 邮箱地址
     */
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    @Schema(description = "邮箱地址", example = "admin@example.com")
    private String email;

    /**
     * 手机号码
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    @Schema(description = "手机号码", example = "13800138000")
    private String phone;

    /**
     * 性别（0-未知，1-男，2-女）
     */
    @Min(value = 0, message = "性别值必须在0-2之间")
    @Max(value = 2, message = "性别值必须在0-2之间")
    @Schema(description = "性别", example = "1", allowableValues = {"0", "1", "2"})
    private Integer gender;

    /**
     * 生日
     */
    @Schema(description = "生日", example = "1990-01-01")
    private LocalDate birthday;

    /**
     * 用户状态（0-禁用，1-启用）
     */
    @NotNull(message = "用户状态不能为空")
    @Min(value = 0, message = "用户状态值必须在0-1之间")
    @Max(value = 1, message = "用户状态值必须在0-1之间")
    @Schema(description = "用户状态", example = "1", allowableValues = {"0", "1"}, requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;

    /**
     * 角色ID列表
     */
    @Schema(description = "角色ID列表", example = "[1, 2]")
    private Long[] roleIds;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500个字符")
    @Schema(description = "备注", example = "这是一个备注")
    private String remark;

    /**
     * 验证密码一致性
     */
    @AssertTrue(message = "两次输入的密码不一致")
    public boolean isPasswordMatch() {
        if (password == null || confirmPassword == null) {
            return false;
        }
        return password.equals(confirmPassword);
    }

    /**
     * 验证邮箱或手机号至少填写一个（可选验证）
     */
    public boolean hasContactInfo() {
        return (email != null && !email.trim().isEmpty()) || 
               (phone != null && !phone.trim().isEmpty());
    }
} 