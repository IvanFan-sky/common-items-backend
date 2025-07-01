package com.common.core.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @Description 角色更新DTO，用于接收更新角色的请求参数
 * @Date 2025/1/7 18:16
 * @Author SparkFan
 */
@Data
@Schema(description = "角色更新DTO")
public class RoleUpdateDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @NotNull(message = "角色ID不能为空")
    @Schema(description = "角色ID", example = "1", required = true)
    private Long id;

    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 50, message = "角色名称长度不能超过50字符")
    @Schema(description = "角色名称", example = "系统管理员", required = true)
    private String roleName;

    /**
     * 角色编码
     */
    @NotBlank(message = "角色编码不能为空")
    @Size(max = 50, message = "角色编码长度不能超过50字符")
    @Schema(description = "角色编码", example = "SYSTEM_ADMIN", required = true)
    private String roleCode;

    /**
     * 显示顺序
     */
    @NotNull(message = "显示顺序不能为空")
    @Schema(description = "显示顺序", example = "1", required = true)
    private Integer roleSort;

    /**
     * 数据范围（1-全部数据权限 2-自定数据权限 3-本部门数据权限 4-本部门及以下数据权限 5-仅本人数据权限）
     */
    @NotNull(message = "数据范围不能为空")
    @Schema(description = "数据范围", example = "1", required = true, allowableValues = {"1", "2", "3", "4", "5"})
    private Integer dataScope;

    /**
     * 状态（0-禁用，1-启用）
     */
    @NotNull(message = "状态不能为空")
    @Schema(description = "状态", example = "1", required = true, allowableValues = {"0", "1"})
    private Integer status;

    /**
     * 权限ID列表
     */
    @Schema(description = "权限ID列表", example = "[1, 2, 3]")
    private List<Long> permissionIds;

    /**
     * 备注
     */
    @Size(max = 500, message = "备注长度不能超过500字符")
    @Schema(description = "备注", example = "系统管理员角色")
    private String remark;

    /**
     * 版本号（用于乐观锁）
     */
    @NotNull(message = "版本号不能为空")
    @Schema(description = "版本号", example = "1", required = true)
    private Integer version;
} 