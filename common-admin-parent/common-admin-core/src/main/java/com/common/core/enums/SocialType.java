package com.common.core.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description 社交登录平台类型枚举，定义支持的第三方登录平台
 * @Date 2025/1/7 15:30
 * @Author SparkFan
 */
@Getter
@AllArgsConstructor
public enum SocialType {

    /**
     * 微信登录
     */
    WECHAT("wechat", "微信登录", "微信"),

    /**
     * QQ登录
     */
    QQ("qq", "QQ登录", "QQ"),

    /**
     * 微博登录
     */
    WEIBO("weibo", "微博登录", "微博"),

    /**
     * 支付宝登录
     */
    ALIPAY("alipay", "支付宝登录", "支付宝"),

    /**
     * GitHub登录
     */
    GITHUB("github", "GitHub登录", "GitHub"),

    /**
     * Gitee登录
     */
    GITEE("gitee", "Gitee登录", "Gitee"),

    /**
     * 钉钉登录
     */
    DINGTALK("dingtalk", "钉钉登录", "钉钉");

    /**
     * 平台代码
     */
    private final String code;

    /**
     * 平台描述
     */
    private final String description;

    /**
     * 平台名称
     */
    private final String name;

    /**
     * 根据代码获取枚举
     */
    public static SocialType getByCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            return null;
        }
        for (SocialType type : values()) {
            if (type.getCode().equalsIgnoreCase(code.trim())) {
                return type;
            }
        }
        return null;
    }

    /**
     * 判断是否为微信平台
     */
    public boolean isWechat() {
        return this == WECHAT;
    }

    /**
     * 判断是否为QQ平台
     */
    public boolean isQQ() {
        return this == QQ;
    }

    /**
     * 判断是否为微博平台
     */
    public boolean isWeibo() {
        return this == WEIBO;
    }

    /**
     * 判断是否为支付宝平台
     */
    public boolean isAlipay() {
        return this == ALIPAY;
    }

    /**
     * 判断是否为GitHub平台
     */
    public boolean isGithub() {
        return this == GITHUB;
    }

    /**
     * 判断是否为Gitee平台
     */
    public boolean isGitee() {
        return this == GITEE;
    }

    /**
     * 判断是否为钉钉平台
     */
    public boolean isDingtalk() {
        return this == DINGTALK;
    }
} 