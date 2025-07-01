package com.common.core.dto.role;

import com.common.core.dto.BaseQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 角色查询DTO，用于角色列表查询的请求参数
 * @Date 2025/1/7 18:16
 * @Author SparkFan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "角色查询DTO")
public class RoleQueryDTO extends BaseQuery {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称（模糊查询）
     */
    @Schema(description = "角色名称", example = "管理员")
    private String roleName;

    /**
     * 角色编码（模糊查询）
     */
    @Schema(description = "角色编码", example = "ADMIN")
    private String roleCode;

    /**
     * 状态（0-禁用，1-启用）
     */
    @Schema(description = "状态", example = "1", allowableValues = {"0", "1"})
    private Integer status;

    /**
     * 数据范围
     */
    @Schema(description = "数据范围", example = "1", allowableValues = {"1", "2", "3", "4", "5"})
    private Integer dataScope;
} 