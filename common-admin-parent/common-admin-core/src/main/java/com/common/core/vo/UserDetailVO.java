package com.common.core.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @Description 用户详细信息视图对象，包含用户的完整信息和关联数据
 * @Date 2025/1/7 15:30
 * @Author SparkFan
 */
@Data
@Schema(description = "用户详细信息视图对象")
public class UserDetailVO implements Serializable {

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
     * 用户状态
     */
    @Schema(description = "用户状态", example = "1", allowableValues = {"0", "1"})
    private Integer status;

    /**
     * 用户状态描述
     */
    @Schema(description = "用户状态描述", example = "启用")
    private String statusText;

    /**
     * 最近登录IP
     */
    @Schema(description = "最近登录IP", example = "192.168.1.1")
    private String loginIp;

    /**
     * 最近登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "最近登录时间", example = "2024-01-01 12:00:00")
    private LocalDateTime loginTime;

    /**
     * 登录地址
     */
    @Schema(description = "登录地址", example = "北京市朝阳区")
    private String loginAddress;

    /**
     * 角色列表
     */
    @Schema(description = "角色列表")
    private List<RoleDetailVO> roles;

    /**
     * 权限列表
     */
    @Schema(description = "权限列表")
    private List<PermissionVO> permissions;

    /**
     * 菜单列表
     */
    @Schema(description = "菜单列表")
    private List<MenuVO> menus;

    /**
     * 社交登录账号列表
     */
    @Schema(description = "社交登录账号列表")
    private List<UserSocialVO> socialAccounts;

    /**
     * 用户统计信息
     */
    @Schema(description = "用户统计信息")
    private UserStatsVO stats;

    /**
     * 用户设置
     */
    @Schema(description = "用户设置")
    private Map<String, Object> settings;

    /**
     * 备注
     */
    @Schema(description = "备注", example = "这是一个备注")
    private String remark;

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
     * 创建者信息
     */
    @Schema(description = "创建者信息")
    private SimpleUserVO createBy;

    /**
     * 更新者信息
     */
    @Schema(description = "更新者信息")
    private SimpleUserVO updateBy;

    /**
     * 版本号
     */
    @Schema(description = "版本号", example = "1")
    private Integer version;

    /**
     * 显示名称
     */
    @Schema(description = "显示名称", example = "管理员")
    private String displayName;

    /**
     * 是否在线
     */
    @Schema(description = "是否在线", example = "true")
    private Boolean online = false;

    /**
     * 是否为超级管理员
     */
    @Schema(description = "是否为超级管理员", example = "false")
    private Boolean superAdmin = false;

    // ==================== 内部类定义 ====================

    /**
     * 角色详细信息
     */
    @Data
    @Schema(description = "角色详细信息")
    public static class RoleDetailVO implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = "角色ID", example = "1")
        private Long id;

        @Schema(description = "角色名称", example = "管理员")
        private String roleName;

        @Schema(description = "角色编码", example = "admin")
        private String roleCode;

        @Schema(description = "数据权限范围", example = "1")
        private Integer dataScope;

        @Schema(description = "数据权限范围描述", example = "全部数据")
        private String dataScopeText;
    }

    /**
     * 权限信息
     */
    @Data
    @Schema(description = "权限信息")
    public static class PermissionVO implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = "权限ID", example = "1")
        private Long id;

        @Schema(description = "权限名称", example = "用户管理")
        private String permissionName;

        @Schema(description = "权限编码", example = "user:list")
        private String permissionCode;

        @Schema(description = "权限类型", example = "1")
        private Integer permissionType;
    }

    /**
     * 菜单信息
     */
    @Data
    @Schema(description = "菜单信息")
    public static class MenuVO implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = "菜单ID", example = "1")
        private Long id;

        @Schema(description = "菜单名称", example = "系统管理")
        private String menuName;

        @Schema(description = "菜单路径", example = "/system")
        private String path;

        @Schema(description = "组件路径", example = "system/index")
        private String component;

        @Schema(description = "菜单图标", example = "system")
        private String icon;
    }

    /**
     * 用户统计信息
     */
    @Data
    @Schema(description = "用户统计信息")
    public static class UserStatsVO implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = "登录次数", example = "100")
        private Integer loginCount;

        @Schema(description = "今日登录次数", example = "5")
        private Integer todayLoginCount;

        @Schema(description = "连续登录天数", example = "30")
        private Integer consecutiveLoginDays;

        @Schema(description = "账号使用天数", example = "365")
        private Integer accountDays;

        @Schema(description = "社交账号绑定数量", example = "3")
        private Integer socialAccountCount;
    }

    /**
     * 简单用户信息
     */
    @Data
    @Schema(description = "简单用户信息")
    public static class SimpleUserVO implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = "用户ID", example = "1")
        private Long id;

        @Schema(description = "用户名", example = "admin")
        private String username;

        @Schema(description = "昵称", example = "管理员")
        private String nickname;

        @Schema(description = "头像", example = "https://example.com/avatar.jpg")
        private String avatar;
    }
} 