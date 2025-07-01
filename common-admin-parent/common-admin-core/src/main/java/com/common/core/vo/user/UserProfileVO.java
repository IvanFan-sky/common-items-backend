package com.common.core.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description 用户个人信息视图对象，返回给前端展示的用户个人信息
 * @Date 2025/1/7 15:30
 * @Author SparkFan
 */
@Data
@Schema(description = "用户个人信息视图对象")
public class UserProfileVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Schema(description = "用户ID", example = "1")
    private Long id;

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
     * 头像地址
     */
    @Schema(description = "头像地址", example = "https://example.com/avatar.jpg")
    private String avatar;

    /**
     * 性别
     */
    @Schema(description = "性别", example = "1", allowableValues = {"0", "1", "2"})
    private Integer gender;

    /**
     * 性别描述
     */
    @Schema(description = "性别描述", example = "男")
    private String genderText;

    /**
     * 生日
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Schema(description = "生日", example = "1990-01-01")
    private LocalDate birthday;

    /**
     * 年龄
     */
    @Schema(description = "年龄", example = "30")
    private Integer age;

    /**
     * 个人简介
     */
    @Schema(description = "个人简介", example = "这是个人简介")
    private String remark;

    /**
     * 真实姓名
     */
    @Schema(description = "真实姓名", example = "张三")
    private String realName;

    /**
     * 个人标签
     */
    @Schema(description = "个人标签", example = "[\"技术\", \"管理\", \"创新\"]")
    private List<String> tags;

    /**
     * 个人主页背景图
     */
    @Schema(description = "个人主页背景图", example = "https://example.com/bg.jpg")
    private String profileBackground;

    /**
     * 职位信息
     */
    @Schema(description = "职位信息", example = "高级工程师")
    private String position;

    /**
     * 部门信息
     */
    @Schema(description = "部门信息", example = "技术部")
    private String department;

    /**
     * 地址信息
     */
    @Schema(description = "地址信息", example = "北京市海淀区")
    private String address;

    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "最后登录时间", example = "2024-01-01 12:00:00")
    private LocalDateTime lastLoginTime;

    /**
     * 最后登录IP
     */
    @Schema(description = "最后登录IP", example = "192.168.1.1")
    private String lastLoginIp;

    /**
     * 登录次数
     */
    @Schema(description = "登录次数", example = "10")
    private Integer loginCount;

    /**
     * 账户创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "账户创建时间", example = "2024-01-01 12:00:00")
    private LocalDateTime createTime;

    /**
     * 信息更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "信息更新时间", example = "2024-01-01 12:00:00")
    private LocalDateTime updateTime;

    /**
     * 密码最后修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "密码最后修改时间", example = "2024-01-01 12:00:00")
    private LocalDateTime passwordUpdateTime;

    /**
     * 邮箱是否已验证
     */
    @Schema(description = "邮箱是否已验证", example = "true")
    private Boolean emailVerified;

    /**
     * 手机号是否已验证
     */
    @Schema(description = "手机号是否已验证", example = "true")
    private Boolean phoneVerified;

    /**
     * 是否启用双因子认证
     */
    @Schema(description = "是否启用双因子认证", example = "false")
    private Boolean twoFactorEnabled;

    /**
     * 用户角色列表
     */
    @Schema(description = "用户角色列表")
    private List<String> roles;

    /**
     * 用户权限列表
     */
    @Schema(description = "用户权限列表")
    private List<String> permissions;
} 