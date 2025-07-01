package com.common.core.vo.role;

import com.common.core.vo.permission.PermissionVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description 角色详细信息VO，包含角色完整信息和关联权限
 * @Date 2025/1/7 18:16
 * @Author SparkFan
 */
@Data
@Schema(description = "角色详细信息VO")
public class RoleDetailVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @Schema(description = "角色ID", example = "1")
    private Long id;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称", example = "超级管理员")
    private String roleName;

    /**
     * 角色编码
     */
    @Schema(description = "角色编码", example = "SUPER_ADMIN")
    private String roleCode;

    /**
     * 显示顺序
     */
    @Schema(description = "显示顺序", example = "1")
    private Integer roleSort;

    /**
     * 数据范围
     */
    @Schema(description = "数据范围", example = "1")
    private Integer dataScope;

    /**
     * 数据范围描述
     */
    @Schema(description = "数据范围描述", example = "全部数据权限")
    private String dataScopeText;

    /**
     * 状态（0-禁用，1-启用）
     */
    @Schema(description = "状态", example = "1")
    private Integer status;

    /**
     * 状态描述
     */
    @Schema(description = "状态描述", example = "启用")
    private String statusText;

    /**
     * 权限列表
     */
    @Schema(description = "权限列表")
    private List<PermissionVO> permissions;

    /**
     * 权限ID列表
     */
    @Schema(description = "权限ID列表", example = "[1, 2, 3]")
    private List<Long> permissionIds;

    /**
     * 用户数量
     */
    @Schema(description = "用户数量", example = "5")
    private Integer userCount;

    /**
     * 备注
     */
    @Schema(description = "备注", example = "系统管理员角色")
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
    }
} 