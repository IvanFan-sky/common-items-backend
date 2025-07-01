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
 * @Description 用户角色关联实体类，管理用户与角色的多对多关系
 * @Date 2025/1/7 18:16
 * @Author SparkFan
 */
@Data
@TableName("sys_user_role")
@Schema(description = "用户角色关联实体")
public class SysUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID", example = "123456789")
    private Long id;

    /**
     * 用户ID
     */
    @TableField("user_id")
    @Schema(description = "用户ID", example = "1")
    private Long userId;

    /**
     * 角色ID
     */
    @TableField("role_id")
    @Schema(description = "角色ID", example = "1")
    private Long roleId;

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