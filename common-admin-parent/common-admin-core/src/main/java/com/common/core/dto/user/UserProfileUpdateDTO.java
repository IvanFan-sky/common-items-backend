package com.common.core.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @Description 用户个人信息更新DTO，用于用户修改自己的个人信息
 * @Date 2025/1/7 15:30
 * @Author SparkFan
 */
@Data
@Schema(description = "用户个人信息更新DTO")
public class UserProfileUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 头像地址
     */
    @Size(max = 200, message = "头像地址长度不能超过200个字符")
    @Schema(description = "头像地址", example = "https://example.com/avatar.jpg")
    private String avatar;

    /**
     * 性别（0-未知，1-男，2-女）
     */
    @Schema(description = "性别", example = "1", allowableValues = {"0", "1", "2"})
    private Integer gender;

    /**
     * 生日
     */
    @Schema(description = "生日", example = "1990-01-01")
    private LocalDate birthday;

    /**
     * 个人简介
     */
    @Size(max = 500, message = "个人简介长度不能超过500个字符")
    @Schema(description = "个人简介", example = "这是个人简介")
    private String remark;

    /**
     * 真实姓名（可选，用户可以选择填写）
     */
    @Size(max = 50, message = "真实姓名长度不能超过50个字符")
    @Schema(description = "真实姓名", example = "张三")
    private String realName;

    /**
     * 个人标签（JSON格式存储）
     */
    @Schema(description = "个人标签", example = "[\"技术\", \"管理\", \"创新\"]")
    private String tags;

    /**
     * 个人主页背景图
     */
    @Size(max = 200, message = "背景图地址长度不能超过200个字符")
    @Schema(description = "个人主页背景图", example = "https://example.com/bg.jpg")
    private String profileBackground;

    /**
     * 职位信息
     */
    @Size(max = 100, message = "职位信息长度不能超过100个字符")
    @Schema(description = "职位信息", example = "高级工程师")
    private String position;

    /**
     * 部门信息
     */
    @Size(max = 100, message = "部门信息长度不能超过100个字符")
    @Schema(description = "部门信息", example = "技术部")
    private String department;

    /**
     * 地址信息
     */
    @Size(max = 200, message = "地址信息长度不能超过200个字符")
    @Schema(description = "地址信息", example = "北京市海淀区")
    private String address;
} 