package com.common.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 基础查询对象
 * 包含分页和排序参数
 * 
 * @author common-admin
 * @since 2024-01-01
 */
@Data
@Schema(description = "基础查询对象")
public class BaseQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    @Schema(description = "当前页码", example = "1", defaultValue = "1")
    private Long current = 1L;

    /**
     * 每页显示条数
     */
    @Schema(description = "每页显示条数", example = "10", defaultValue = "10")
    private Long size = 10L;

    /**
     * 排序字段
     */
    @Schema(description = "排序字段", example = "create_time")
    private String orderBy;

    /**
     * 排序方式：asc升序，desc降序
     */
    @Schema(description = "排序方式", example = "desc", defaultValue = "desc", allowableValues = {"asc", "desc"})
    private String orderType = "desc";

    /**
     * 关键词搜索
     */
    @Schema(description = "关键词搜索", example = "用户")
    private String keyword;

    /**
     * 开始时间（用于时间范围查询）
     */
    @Schema(description = "开始时间", example = "2024-01-01 00:00:00")
    private String startTime;

    /**
     * 结束时间（用于时间范围查询）
     */
    @Schema(description = "结束时间", example = "2024-01-31 23:59:59")
    private String endTime;

    /**
     * 获取排序字符串
     * 格式：字段名 排序方式
     */
    public String getOrderString() {
        if (orderBy == null || orderBy.trim().isEmpty()) {
            return "create_time desc";
        }
        return orderBy + " " + (orderType == null ? "desc" : orderType);
    }

    /**
     * 是否为升序
     */
    public boolean isAsc() {
        return "asc".equalsIgnoreCase(orderType);
    }

    /**
     * 是否为降序
     */
    public boolean isDesc() {
        return "desc".equalsIgnoreCase(orderType);
    }

    /**
     * 设置分页参数
     */
    public void setPage(Long current, Long size) {
        this.current = current == null || current < 1 ? 1L : current;
        this.size = size == null || size < 1 ? 10L : (size > 100 ? 100L : size);
    }

    /**
     * 校验并修正分页参数
     */
    public void validatePage() {
        if (this.current == null || this.current < 1) {
            this.current = 1L;
        }
        if (this.size == null || this.size < 1) {
            this.size = 10L;
        }
        // 限制最大每页条数
        if (this.size > 100) {
            this.size = 100L;
        }
    }
}