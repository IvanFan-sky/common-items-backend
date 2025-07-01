package com.common.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 系统权限菜单实体类，统一管理权限和菜单信息
 * @Date 2025/1/7 18:16
 * @Author SparkFan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_permission")
@Schema(description = "系统权限菜单实体")
public class SysPermission extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 父权限ID
     */
    @TableField("parent_id")
    @Schema(description = "父权限ID", example = "0")
    private Long parentId;

    /**
     * 权限名称
     */
    @TableField("permission_name")
    @Schema(description = "权限名称", example = "用户管理")
    private String permissionName;

    /**
     * 权限编码
     */
    @TableField("permission_code")
    @Schema(description = "权限编码", example = "system:user:list")
    private String permissionCode;

    /**
     * 权限类型（1-目录，2-菜单，3-按钮，4-接口）
     */
    @TableField("permission_type")
    @Schema(description = "权限类型", example = "2", allowableValues = {"1", "2", "3", "4"})
    private Integer permissionType;

    /**
     * 路由路径
     */
    @TableField("path")
    @Schema(description = "路由路径", example = "/system/user")
    private String path;

    /**
     * 组件路径
     */
    @TableField("component")
    @Schema(description = "组件路径", example = "system/user/index")
    private String component;

    /**
     * 图标
     */
    @TableField("icon")
    @Schema(description = "图标", example = "user")
    private String icon;

    /**
     * 显示顺序
     */
    @TableField("order_num")
    @Schema(description = "显示顺序", example = "1")
    private Integer orderNum;

    /**
     * 状态（0-禁用，1-启用）
     */
    @TableField("status")
    @Schema(description = "状态", example = "1", allowableValues = {"0", "1"})
    private Integer status;

    /**
     * 是否显示（0-隐藏，1-显示）
     */
    @TableField("visible")
    @Schema(description = "是否显示", example = "1", allowableValues = {"0", "1"})
    private Integer visible;

    /**
     * 是否外链（0-否，1-是）
     */
    @TableField("is_frame")
    @Schema(description = "是否外链", example = "0", allowableValues = {"0", "1"})
    private Integer isFrame;

    /**
     * 是否缓存（0-否，1-是）
     */
    @TableField("is_cache")
    @Schema(description = "是否缓存", example = "0", allowableValues = {"0", "1"})
    private Integer isCache;
} 