package com.common.core.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description 用户视图对象，用于返回给前端的用户基本信息（不包含敏感数据）
 * @Date 2025/1/7 15:30
 * @Author SparkFan
 */
@Data
@Schema(description = "用户视图对象")
public class UserVO implements Serializable {

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
     * 性别（0-未知，1-男，2-女）
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
     * 用户状态（0-禁用，1-启用）
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
     * 角色列表
     */
    @Schema(description = "角色列表")
    private List<RoleVO> roles;

    /**
     * 权限代码列表
     */
    @Schema(description = "权限代码列表", example = "['user:list', 'user:add']")
    private List<String> permissions;

    /**
     * 社交登录账号列表
     */
    @Schema(description = "社交登录账号列表")
    private List<UserSocialVO> socialAccounts;

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
     * 创建者名称
     */
    @Schema(description = "创建者名称", example = "系统管理员")
    private String createByName;

    /**
     * 更新者名称
     */
    @Schema(description = "更新者名称", example = "系统管理员")
    private String updateByName;

    /**
     * 版本号
     */
    @Schema(description = "版本号", example = "1")
    private Integer version;

    /**
     * 显示名称（优先昵称，其次用户名）
     */
    @Schema(description = "显示名称", example = "管理员")
    private String displayName;

    /**
     * 是否在线
     */
    @Schema(description = "是否在线", example = "true")
    private Boolean online = false;

    /**
     * 角色名称列表（逗号分隔）
     */
    @Schema(description = "角色名称列表", example = "管理员,用户")
    private String roleNames;

    // ==================== 内部类定义 ====================

    /**
     * 角色视图对象
     */
    @Data
    @Schema(description = "角色视图对象")
    public static class RoleVO implements Serializable {
        private static final long serialVersionUID = 1L;

        /**
         * 角色ID
         */
        @Schema(description = "角色ID", example = "1")
        private Long id;

        /**
         * 角色名称
         */
        @Schema(description = "角色名称", example = "管理员")
        private String roleName;

        /**
         * 角色编码
         */
        @Schema(description = "角色编码", example = "admin")
        private String roleCode;
    }
} 