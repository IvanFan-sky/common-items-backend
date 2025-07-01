package com.common.core.dto.permission;

import com.common.core.dto.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 权限查询DTO，用于权限列表查询的请求参数
 * @Date 2025/1/7 18:16
 * @Author SparkFan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "权限查询DTO")
public class PermissionQueryDTO extends BaseQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 权限名称（模糊查询）
     */
    @Schema(description = "权限名称", example = "用户管理")
    private String permissionName;

    /**
     * 权限编码（模糊查询）
     */
    @Schema(description = "权限编码", example = "system:user")
    private String permissionCode;

    /**
     * 权限类型（1-目录，2-菜单，3-按钮，4-接口）
     */
    @Schema(description = "权限类型", example = "2", allowableValues = {"1", "2", "3", "4"})
    private Integer permissionType;

    /**
     * 状态（0-禁用，1-启用）
     */
    @Schema(description = "状态", example = "1", allowableValues = {"0", "1"})
    private Integer status;

    /**
     * 是否显示（0-隐藏，1-显示）
     */
    @Schema(description = "是否显示", example = "1", allowableValues = {"0", "1"})
    private Integer visible;

    /**
     * 父权限ID
     */
    @Schema(description = "父权限ID", example = "0")
    private Long parentId;
} 