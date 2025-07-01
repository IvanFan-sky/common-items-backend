package com.common.core.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description 权限信息VO，用于返回权限基本信息
 * @Date 2025/1/7 18:16
 * @Author SparkFan
 */
@Data
@Schema(description = "权限信息VO")
public class PermissionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限ID
     */
    @Schema(description = "权限ID", example = "1")
    private Long id;

    /**
     * 父权限ID
     */
    @Schema(description = "父权限ID", example = "0")
    private Long parentId;

    /**
     * 权限名称
     */
    @Schema(description = "权限名称", example = "用户管理")
    private String permissionName;

    /**
     * 权限编码
     */
    @Schema(description = "权限编码", example = "system:user:list")
    private String permissionCode;

    /**
     * 权限类型（1-目录，2-菜单，3-按钮，4-接口）
     */
    @Schema(description = "权限类型", example = "2")
    private Integer permissionType;

    /**
     * 权限类型描述
     */
    @Schema(description = "权限类型描述", example = "菜单")
    private String permissionTypeText;

    /**
     * 路由路径
     */
    @Schema(description = "路由路径", example = "/system/user")
    private String path;

    /**
     * 组件路径
     */
    @Schema(description = "组件路径", example = "system/user/index")
    private String component;

    /**
     * 图标
     */
    @Schema(description = "图标", example = "user")
    private String icon;

    /**
     * 显示顺序
     */
    @Schema(description = "显示顺序", example = "1")
    private Integer orderNum;

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
     * 是否显示（0-隐藏，1-显示）
     */
    @Schema(description = "是否显示", example = "1")
    private Integer visible;

    /**
     * 是否外链（0-否，1-是）
     */
    @Schema(description = "是否外链", example = "0")
    private Integer isFrame;

    /**
     * 是否缓存（0-否，1-是）
     */
    @Schema(description = "是否缓存", example = "0")
    private Integer isCache;

    /**
     * 备注
     */
    @Schema(description = "备注", example = "用户管理菜单")
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
     * 版本号
     */
    @Schema(description = "版本号", example = "1")
    private Integer version;
} 