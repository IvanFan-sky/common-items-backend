package com.common.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.common.core.enums.Gender;
import com.common.core.enums.UserStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @Description 系统用户实体类，存储用户基本信息和登录相关数据
 * @Date 2025/1/7 15:30
 * @Author SparkFan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
@Schema(description = "系统用户实体")
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名（登录账号）
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 50, message = "用户名长度必须在2-50个字符之间")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "用户名只能包含字母、数字、下划线和中划线")
    @TableField("username")
    @Schema(description = "用户名", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    /**
     * 密码（BCrypt加密）
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 100, message = "密码长度必须在6-100个字符之间")
    @TableField("password")
    @Schema(description = "密码", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    /**
     * 用户昵称
     */
    @Size(max = 50, message = "昵称长度不能超过50个字符")
    @TableField("nickname")
    @Schema(description = "用户昵称", example = "管理员")
    private String nickname;

    /**
     * 邮箱地址
     */
    @Email(message = "邮箱格式不正确")
    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    @TableField("email")
    @Schema(description = "邮箱地址", example = "admin@example.com")
    private String email;

    /**
     * 手机号码
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    @TableField("phone")
    @Schema(description = "手机号码", example = "13800138000")
    private String phone;

    /**
     * 头像地址
     */
    @Size(max = 200, message = "头像地址长度不能超过200个字符")
    @TableField("avatar")
    @Schema(description = "头像地址", example = "https://example.com/avatar.jpg")
    private String avatar;

    /**
     * 性别（0-未知，1-男，2-女）
     */
    @TableField("gender")
    @Schema(description = "性别", example = "1", allowableValues = {"0", "1", "2"})
    private Integer gender;

    /**
     * 生日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @TableField("birthday")
    @Schema(description = "生日", example = "1990-01-01")
    private LocalDate birthday;

    /**
     * 用户状态（0-禁用，1-启用）
     */
    @TableField("status")
    @Schema(description = "用户状态", example = "1", allowableValues = {"0", "1"})
    private Integer status;

    /**
     * 最近登录IP
     */
    @Size(max = 50, message = "登录IP长度不能超过50个字符")
    @TableField("login_ip")
    @Schema(description = "最近登录IP", example = "192.168.1.1")
    private String loginIp;

    /**
     * 最近登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("login_time")
    @Schema(description = "最近登录时间", example = "2024-01-01 12:00:00")
    private LocalDateTime loginTime;

    /**
     * 最近登录IP（新字段）
     */
    @Size(max = 50, message = "最近登录IP长度不能超过50个字符")
    @TableField("last_login_ip")
    @Schema(description = "最近登录IP", example = "192.168.1.1")
    private String lastLoginIp;

    /**
     * 最近登录时间（新字段）
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("last_login_time")
    @Schema(description = "最近登录时间", example = "2024-01-01 12:00:00")
    private LocalDateTime lastLoginTime;

    /**
     * 登录次数
     */
    @TableField("login_count")
    @Schema(description = "登录次数", example = "10")
    private Integer loginCount = 0;

    /**
     * 密码修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("password_update_time")
    @Schema(description = "密码修改时间", example = "2024-01-01 12:00:00")
    private LocalDateTime passwordUpdateTime;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500个字符")
    @TableField("remark")
    @Schema(description = "备注", example = "这是一个备注")
    private String remark;

    // ==================== 业务方法 ====================

    /**
     * 获取性别枚举
     */
    public Gender getGenderEnum() {
        return Gender.getByCode(this.gender);
    }

    /**
     * 设置性别枚举
     */
    public void setGenderEnum(Gender gender) {
        this.gender = gender != null ? gender.getCode() : Gender.UNKNOWN.getCode();
    }

    /**
     * 获取用户状态枚举
     */
    public UserStatus getStatusEnum() {
        return UserStatus.getByCode(this.status);
    }

    /**
     * 设置用户状态枚举
     */
    public void setStatusEnum(UserStatus status) {
        this.status = status != null ? status.getCode() : UserStatus.DISABLED.getCode();
    }

    /**
     * 判断用户是否启用
     */
    public boolean isEnabled() {
        return UserStatus.ENABLED.getCode().equals(this.status);
    }

    /**
     * 判断用户是否禁用
     */
    public boolean isDisabled() {
        return UserStatus.DISABLED.getCode().equals(this.status);
    }

    /**
     * 获取显示名称（优先使用昵称，其次用户名）
     */
    public String getDisplayName() {
        return nickname != null && !nickname.trim().isEmpty() ? nickname : username;
    }

    /**
     * 判断是否有邮箱
     */
    public boolean hasEmail() {
        return email != null && !email.trim().isEmpty();
    }

    /**
     * 判断是否有手机号
     */
    public boolean hasPhone() {
        return phone != null && !phone.trim().isEmpty();
    }

    /**
     * 判断是否有头像
     */
    public boolean hasAvatar() {
        return avatar != null && !avatar.trim().isEmpty();
    }
} 