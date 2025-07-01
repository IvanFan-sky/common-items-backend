package com.common.core.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 业务异常类，用于处理系统业务逻辑相关的异常情况
 * @Date 2025/1/7 12:16
 * @Author SparkFan
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "业务异常")
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    @Schema(description = "错误码", example = "500")
    private Integer code;

    /**
     * 错误消息
     */
    @Schema(description = "错误消息", example = "操作失败")
    private String message;

    public BusinessException() {
        super();
    }

    public BusinessException(String message) {
        super(message);
        this.message = message;
        this.code = 500;
    }

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.code = 500;
    }

    public BusinessException(Integer code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    public BusinessException(Throwable cause) {
        super(cause);
        this.code = 500;
    }

    @Override
    public String getMessage() {
        return message;
    }

    /**
     * 抛出业务异常
     */
    public static void throwException(String message) {
        throw new BusinessException(message);
    }

    /**
     * 抛出业务异常
     */
    public static void throwException(Integer code, String message) {
        throw new BusinessException(code, message);
    }

    /**
     * 条件为true时抛出异常
     */
    public static void throwIf(boolean condition, String message) {
        if (condition) {
            throw new BusinessException(message);
        }
    }

    /**
     * 条件为true时抛出异常
     */
    public static void throwIf(boolean condition, Integer code, String message) {
        if (condition) {
            throw new BusinessException(code, message);
        }
    }

    /**
     * 对象为null时抛出异常
     */
    public static void throwIfNull(Object obj, String message) {
        if (obj == null) {
            throw new BusinessException(message);
        }
    }

    /**
     * 字符串为空时抛出异常
     */
    public static void throwIfEmpty(String str, String message) {
        if (str == null || str.trim().isEmpty()) {
            throw new BusinessException(message);
        }
    }
}