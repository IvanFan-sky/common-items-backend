package com.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 性别枚举，定义用户性别类型
 * @Date 2025/1/7 15:30
 * @Author SparkFan
 */
@Getter
@AllArgsConstructor
public enum Gender {

    /**
     * 未知/保密
     */
    UNKNOWN(0, "未知"),

    /**
     * 男性
     */
    MALE(1, "男"),

    /**
     * 女性
     */
    FEMALE(2, "女");

    /**
     * 性别代码
     */
    private final Integer code;

    /**
     * 性别描述
     */
    private final String description;

    /**
     * 根据代码获取枚举
     */
    public static Gender getByCode(Integer code) {
        if (code == null) {
            return UNKNOWN;
        }
        for (Gender gender : values()) {
            if (gender.getCode().equals(code)) {
                return gender;
            }
        }
        return UNKNOWN;
    }

    /**
     * 判断是否为男性
     */
    public boolean isMale() {
        return this == MALE;
    }

    /**
     * 判断是否为女性
     */
    public boolean isFemale() {
        return this == FEMALE;
    }

    /**
     * 判断是否未知
     */
    public boolean isUnknown() {
        return this == UNKNOWN;
    }
} 