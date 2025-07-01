package com.common.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description 角色权限关联实体类，管理角色与权限的多对多关系
 * @Date 2025/1/7 18:16
 * @Author SparkFan
 */
@Data
@TableName("sys_role_permission")
@Schema(description = "角色权限关联实体")
public class SysRolePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID", example = "123456789")
    private Long id;

    /**
     * 角色ID
     */
    @TableField("role_id")
    @Schema(description = "角色ID", example = "1")
    private Long roleId;

    /**
     * 权限ID
     */
    @TableField("permission_id")
    @Schema(description = "权限ID", example = "1")
    private Long permissionId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "2024-01-01 12:00:00")
    private LocalDateTime createTime;

    /**
     * 创建者ID
     */
    @TableField("create_by")
    @Schema(description = "创建者ID", example = "1")
    private Long createBy;
} 