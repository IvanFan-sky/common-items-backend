package com.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 用户状态枚举，定义用户账号的启用和禁用状态
 * @Date 2025/1/7 15:30
 * @Author SparkFan
 */
@Getter
@AllArgsConstructor
public enum UserStatus {

    /**
     * 禁用状态
     */
    DISABLED(0, "禁用"),

    /**
     * 启用状态
     */
    ENABLED(1, "启用");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 状态描述
     */
    private final String description;

    /**
     * 根据状态码获取枚举
     */
    public static UserStatus getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (UserStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    /**
     * 判断是否为启用状态
     */
    public boolean isEnabled() {
        return this == ENABLED;
    }

    /**
     * 判断是否为禁用状态
     */
    public boolean isDisabled() {
        return this == DISABLED;
    }
} 