package com.common.core.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description 导入结果视图对象
 * @Date 2025/1/7 18:30
 * @Author SparkFan
 */
@Data
@Schema(description = "导入结果VO")
public class ImportResultVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总记录数
     */
    @Schema(description = "总记录数", example = "100")
    private Integer totalCount = 0;

    /**
     * 成功导入数
     */
    @Schema(description = "成功导入数", example = "95")
    private Integer successCount = 0;

    /**
     * 失败记录数
     */
    @Schema(description = "失败记录数", example = "5")
    private Integer failureCount = 0;

    /**
     * 跳过记录数
     */
    @Schema(description = "跳过记录数", example = "0")
    private Integer skipCount = 0;

    /**
     * 是否全部成功
     */
    @Schema(description = "是否全部成功", example = "false")
    private Boolean allSuccess = false;

    /**
     * 错误信息列表
     */
    @Schema(description = "错误信息列表")
    private List<String> errorMessages;

    /**
     * 失败详情列表
     */
    @Schema(description = "失败详情列表")
    private List<FailureDetail> failureDetails;

    /**
     * 导入耗时（毫秒）
     */
    @Schema(description = "导入耗时（毫秒）", example = "1500")
    private Long duration;

    /**
     * 失败详情
     */
    @Data
    @Schema(description = "失败详情")
    public static class FailureDetail implements Serializable {
        
        private static final long serialVersionUID = 1L;

        /**
         * 行号
         */
        @Schema(description = "行号", example = "3")
        private Integer rowIndex;

        /**
         * 失败原因
         */
        @Schema(description = "失败原因", example = "用户名已存在")
        private String reason;

        /**
         * 原始数据
         */
        @Schema(description = "原始数据")
        private Object originalData;
    }
} 