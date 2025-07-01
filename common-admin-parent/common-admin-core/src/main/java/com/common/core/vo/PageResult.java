package com.common.core.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @Description 分页响应结果，用于包装分页查询返回的数据
 * @Date 2025/1/7 12:16
 * @Author SparkFan
 */
@Data
@Schema(description = "分页响应结果")
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当前页码
     */
    @Schema(description = "当前页码", example = "1")
    private Long current;

    /**
     * 每页显示条数
     */
    @Schema(description = "每页显示条数", example = "10")
    private Long size;

    /**
     * 总条数
     */
    @Schema(description = "总条数", example = "100")
    private Long total;

    /**
     * 总页数
     */
    @Schema(description = "总页数", example = "10")
    private Long pages;

    /**
     * 数据列表
     */
    @Schema(description = "数据列表")
    private List<T> records;

    public PageResult() {
        this.records = Collections.emptyList();
    }

    public PageResult(Long current, Long size, Long total, List<T> records) {
        this.current = current;
        this.size = size;
        this.total = total;
        this.records = records == null ? Collections.emptyList() : records;
        this.pages = this.size > 0 ? (this.total + this.size - 1) / this.size : 0;
    }

    /**
     * 根据MyBatis Plus的IPage构建分页结果
     */
    public static <T> PageResult<T> of(IPage<T> page) {
        return new PageResult<>(page.getCurrent(), page.getSize(), page.getTotal(), page.getRecords());
    }

    /**
     * 构建空的分页结果
     */
    public static <T> PageResult<T> empty() {
        return new PageResult<>(1L, 10L, 0L, Collections.emptyList());
    }

    /**
     * 构建空的分页结果
     */
    public static <T> PageResult<T> empty(Long current, Long size) {
        return new PageResult<>(current, size, 0L, Collections.emptyList());
    }

    /**
     * 判断是否有数据
     */
    public boolean hasRecords() {
        return this.records != null && !this.records.isEmpty();
    }

    /**
     * 判断是否为空
     */
    public boolean isEmpty() {
        return !hasRecords();
    }

    /**
     * 获取记录数量
     */
    public int getRecordCount() {
        return this.records == null ? 0 : this.records.size();
    }

    /**
     * 是否有下一页
     */
    public boolean hasNext() {
        return this.current != null && this.pages != null && this.current < this.pages;
    }

    /**
     * 是否有上一页
     */
    public boolean hasPrevious() {
        return this.current != null && this.current > 1;
    }
}