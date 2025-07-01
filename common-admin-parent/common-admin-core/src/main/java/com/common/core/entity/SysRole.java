package com.common.core.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 系统角色实体类，定义角色基本信息和数据权限范围
 * @Date 2025/1/7 18:16
 * @Author SparkFan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
@Schema(description = "系统角色实体")
public class SysRole extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    @TableField("role_name")
    @Schema(description = "角色名称", example = "超级管理员")
    private String roleName;

    /**
     * 角色编码
     */
    @TableField("role_code")
    @Schema(description = "角色编码", example = "SUPER_ADMIN")
    private String roleCode;

    /**
     * 显示顺序
     */
    @TableField("role_sort")
    @Schema(description = "显示顺序", example = "1")
    private Integer roleSort;

    /**
     * 数据范围（1-全部数据权限 2-自定数据权限 3-本部门数据权限 4-本部门及以下数据权限 5-仅本人数据权限）
     */
    @TableField("data_scope")
    @Schema(description = "数据范围", example = "1", allowableValues = {"1", "2", "3", "4", "5"})
    private Integer dataScope;

    /**
     * 状态（0-禁用，1-启用）
     */
    @TableField("status")
    @Schema(description = "状态", example = "1", allowableValues = {"0", "1"})
    private Integer status;
} 