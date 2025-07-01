package com.common.core.dto.permission;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;

/**
 * @Description 权限更新DTO，用于接收更新权限的请求参数
 * @Date 2025/1/7 18:16
 * @Author SparkFan
 */
@Data
@Schema(description = "权限更新DTO")
public class PermissionUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 权限ID
     */
    @NotNull(message = "权限ID不能为空")
    @Schema(description = "权限ID", example = "1", required = true)
    private Long id;

    /**
     * 父权限ID
     */
    @NotNull(message = "父权限ID不能为空")
    @Schema(description = "父权限ID", example = "0", required = true)
    private Long parentId;

    /**
     * 权限名称
     */
    @NotBlank(message = "权限名称不能为空")
    @Size(max = 50, message = "权限名称长度不能超过50字符")
    @Schema(description = "权限名称", example = "用户管理", required = true)
    private String permissionName;

    /**
     * 权限编码
     */
    @NotBlank(message = "权限编码不能为空")
    @Size(max = 100, message = "权限编码长度不能超过100字符")
    @Schema(description = "权限编码", example = "system:user:list", required = true)
    private String permissionCode;

    /**
     * 权限类型（1-目录，2-菜单，3-按钮，4-接口）
     */
    @NotNull(message = "权限类型不能为空")
    @Schema(description = "权限类型", example = "2", required = true, allowableValues = {"1", "2", "3", "4"})
    private Integer permissionType;

    /**
     * 路由路径
     */
    @Size(max = 200, message = "路由路径长度不能超过200字符")
    @Schema(description = "路由路径", example = "/system/user")
    private String path;

    /**
     * 组件路径
     */
    @Size(max = 200, message = "组件路径长度不能超过200字符")
    @Schema(description = "组件路径", example = "system/user/index")
    private String component;

    /**
     * 图标
     */
    @Size(max = 100, message = "图标长度不能超过100字符")
    @Schema(description = "图标", example = "user")
    private String icon;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空")
    @Schema(description = "显示顺序", example = "1", required = true)
    private Integer orderNum;

    /**
     * 状态（0-禁用，1-启用）
     */
    @NotNull(message = "状态不能为空")
    @Schema(description = "状态", example = "1", required = true, allowableValues = {"0", "1"})
    private Integer status;

    /**
     * 是否显示（0-隐藏，1-显示）
     */
    @NotNull(message = "是否显示不能为空")
    @Schema(description = "是否显示", example = "1", required = true, allowableValues = {"0", "1"})
    private Integer visible;

    /**
     * 是否外链（0-否，1-是）
     */
    @NotNull(message = "是否外链不能为空")
    @Schema(description = "是否外链", example = "0", required = true, allowableValues = {"0", "1"})
    private Integer isFrame;

    /**
     * 是否缓存（0-否，1-是）
     */
    @NotNull(message = "是否缓存不能为空")
    @Schema(description = "是否缓存", example = "0", required = true, allowableValues = {"0", "1"})
    private Integer isCache;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500字符")
    @Schema(description = "备注", example = "用户管理菜单")
    private String remark;

    /**
     * 版本号（用于乐观锁）
     */
    @NotNull(message = "版本号不能为空")
    @Schema(description = "版本号", example = "1", required = true)
    private Integer version;
} 