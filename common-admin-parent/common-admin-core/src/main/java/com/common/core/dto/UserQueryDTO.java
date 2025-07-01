package com.common.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 用户查询DTO，继承BaseQuery，用于用户列表查询和筛选功能
 * @Date 2025/1/7 15:30
 * @Author SparkFan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "用户查询DTO")
public class UserQueryDTO extends BaseQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名（模糊查询）
     */
    @Schema(description = "用户名", example = "admin")
    private String username;

    /**
     * 用户昵称（模糊查询）
     */
    @Schema(description = "用户昵称", example = "管理员")
    private String nickname;

    /**
     * 邮箱地址（模糊查询）
     */
    @Schema(description = "邮箱地址", example = "admin@example.com")
    private String email;

    /**
     * 手机号码（模糊查询）
     */
    @Schema(description = "手机号码", example = "138")
    private String phone;

    /**
     * 性别（0-未知，1-男，2-女）
     */
    @Schema(description = "性别", example = "1", allowableValues = {"0", "1", "2"})
    private Integer gender;

    /**
     * 用户状态（0-禁用，1-启用）
     */
    @Schema(description = "用户状态", example = "1", allowableValues = {"0", "1"})
    private Integer status;

    /**
     * 角色ID（精确查询）
     */
    @Schema(description = "角色ID", example = "1")
    private Long roleId;

    /**
     * 角色名称（模糊查询）
     */
    @Schema(description = "角色名称", example = "管理员")
    private String roleName;

    /**
     * 创建人ID
     */
    @Schema(description = "创建人ID", example = "1")
    private Long createBy;

    /**
     * 登录IP（模糊查询）
     */
    @Schema(description = "登录IP", example = "192.168")
    private String loginIp;

    /**
     * 最后登录开始时间
     */
    @Schema(description = "最后登录开始时间", example = "2024-01-01 00:00:00")
    private String lastLoginStartTime;

    /**
     * 最后登录结束时间
     */
    @Schema(description = "最后登录结束时间", example = "2024-01-31 23:59:59")
    private String lastLoginEndTime;

    /**
     * 生日开始时间
     */
    @Schema(description = "生日开始时间", example = "1990-01-01")
    private String birthdayStart;

    /**
     * 生日结束时间
     */
    @Schema(description = "生日结束时间", example = "2000-12-31")
    private String birthdayEnd;

    /**
     * 是否包含已删除的用户
     */
    @Schema(description = "是否包含已删除的用户", example = "false")
    private Boolean includeDeleted = false;

    /**
     * 是否只查询在线用户
     */
    @Schema(description = "是否只查询在线用户", example = "false")
    private Boolean onlineOnly = false;

    /**
     * 部门ID列表（如果有部门功能）
     */
    @Schema(description = "部门ID列表", example = "[1, 2]")
    private Long[] deptIds;

    /**
     * 排除的用户ID列表
     */
    @Schema(description = "排除的用户ID列表", example = "[1, 2]")
    private Long[] excludeUserIds;

    /**
     * 是否查询社交登录用户
     */
    @Schema(description = "是否查询社交登录用户", example = "false")
    private Boolean socialOnly = false;

    /**
     * 社交平台类型
     */
    @Schema(description = "社交平台类型", example = "wechat")
    private String socialType;
} 