package com.common.core.constant;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 通用常量
 * 
 * @author common-admin
 * @since 2024-01-01
 */
@Schema(description = "通用常量定义")
public interface CommonConstants {

    /**
     * 成功标记
     */
    Integer SUCCESS = 200;

    /**
     * 失败标记
     */
    Integer FAIL = 500;

    /**
     * 参数错误
     */
    Integer BAD_REQUEST = 400;

    /**
     * 未授权
     */
    Integer UNAUTHORIZED = 401;

    /**
     * 禁止访问
     */
    Integer FORBIDDEN = 403;

    /**
     * 资源不存在
     */
    Integer NOT_FOUND = 404;

    /**
     * 编码
     */
    String UTF8 = "UTF-8";

    /**
     * JSON 资源
     */
    String CONTENT_TYPE = "application/json; charset=utf-8";

    /**
     * 默认为空消息
     */
    String DEFAULT_NULL_MESSAGE = "数据不能为空";

    /**
     * 默认成功消息
     */
    String DEFAULT_SUCCESS_MESSAGE = "操作成功";

    /**
     * 默认失败消息
     */
    String DEFAULT_FAILURE_MESSAGE = "操作失败";

    /**
     * 树根节点id
     */
    Long TREE_ROOT = 0L;

    /**
     * 菜单根节点id
     */
    Long MENU_ROOT = 0L;

    /**
     * 部门根节点id
     */
    Long DEPT_ROOT = 0L;

    /**
     * 默认密码
     */
    String DEFAULT_PASSWORD = "123456";

    /**
     * 超级管理员ID
     */
    Long SUPER_ADMIN_ID = 1L;

    /**
     * 超级管理员角色编码
     */
    String SUPER_ADMIN_ROLE_CODE = "ROLE_SUPER_ADMIN";

    /**
     * 管理员角色编码
     */
    String ADMIN_ROLE_CODE = "ROLE_ADMIN";

    /**
     * 普通用户角色编码
     */
    String USER_ROLE_CODE = "ROLE_USER";

    /**
     * 启用状态
     */
    Integer STATUS_ENABLE = 1;

    /**
     * 禁用状态
     */
    Integer STATUS_DISABLE = 0;

    /**
     * 删除标识 - 已删除
     */
    Integer DELETED_YES = 1;

    /**
     * 删除标识 - 未删除
     */
    Integer DELETED_NO = 0;

    /**
     * 是否标识 - 是
     */
    Integer YES = 1;

    /**
     * 是否标识 - 否
     */
    Integer NO = 0;

    /**
     * 性别 - 男
     */
    Integer GENDER_MALE = 1;

    /**
     * 性别 - 女
     */
    Integer GENDER_FEMALE = 2;

    /**
     * 性别 - 未知
     */
    Integer GENDER_UNKNOWN = 0;
}