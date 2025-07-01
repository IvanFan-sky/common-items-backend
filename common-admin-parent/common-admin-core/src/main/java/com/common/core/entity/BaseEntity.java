package com.common.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description 基础实体类，包含所有实体类的公共字段如ID、创建时间、更新时间等
 * @Date 2025/1/7 12:16
 * @Author SparkFan
 */
@Data
@Schema(description = "基础实体类")
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @Schema(description = "主键ID", example = "123456789")
    private Long id;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间", example = "2024-01-01 12:00:00")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间", example = "2024-01-01 12:00:00")
    private LocalDateTime updateTime;

    /**
     * 创建者ID
     */
    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建者ID", example = "1")
    private Long createBy;

    /**
     * 更新者ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新者ID", example = "1")
    private Long updateBy;

    /**
     * 逻辑删除标识（0：未删除，1：已删除）
     */
    @TableLogic
    @Schema(description = "逻辑删除标识", example = "0", allowableValues = {"0", "1"})
    private Integer deleted;

    /**
     * 版本号（用于乐观锁）
     */
    @Schema(description = "版本号（用于乐观锁）", example = "1")
    private Integer version;

    /**
     * 备注
     */
    @Schema(description = "备注", example = "这是一个备注")
    private String remark;
}