package com.common.core.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Description 角色信息VO，用于返回角色基本信息
 * @Date 2025/1/7 18:16
 * @Author SparkFan
 */
@Data
@Schema(description = "角色信息VO")
public class RoleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @Schema(description = "角色ID", example = "1")
    private Long id;

    /**
     * 角色名称
     */
    @Schema(description = "角色名称", example = "超级管理员")
    private String roleName;

    /**
     * 角色编码
     */
    @Schema(description = "角色编码", example = "SUPER_ADMIN")
    private String roleCode;

    /**
     * 显示顺序
     */
    @Schema(description = "显示顺序", example = "1")
    private Integer roleSort;

    /**
     * 数据范围
     */
    @Schema(description = "数据范围", example = "1")
    private Integer dataScope;

    /**
     * 数据范围描述
     */
    @Schema(description = "数据范围描述", example = "全部数据权限")
    private String dataScopeText;

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
     * 备注
     */
    @Schema(description = "备注", example = "系统管理员角色")
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