-- ==========================================
-- 通用管理系统数据库初始化脚本 - DDL
-- 版本: v1.0
-- 创建时间: 2025-01-07
-- 说明: Stage 1 核心基础功能表结构
-- ==========================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `common_items_db` 
DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `common_items_db`;

-- ==========================================
-- 1. 用户表 (sys_user)
-- ==========================================
CREATE TABLE `sys_user` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `password` VARCHAR(100) NOT NULL COMMENT '密码(BCrypt加密)',
    `nickname` VARCHAR(50) NULL DEFAULT NULL COMMENT '用户昵称',
    `email` VARCHAR(100) NULL DEFAULT NULL COMMENT '邮箱地址',
    `phone` VARCHAR(20) NULL DEFAULT NULL COMMENT '手机号码',
    `avatar` VARCHAR(200) NULL DEFAULT NULL COMMENT '头像地址',
    `gender` TINYINT NULL DEFAULT 0 COMMENT '性别(0-未知,1-男,2-女)',
    `birthday` DATE NULL DEFAULT NULL COMMENT '生日',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态(0-禁用,1-启用)',

    `login_ip` VARCHAR(50) NULL DEFAULT NULL COMMENT '最近登录IP',
    `login_time` DATETIME NULL DEFAULT NULL COMMENT '最近登录时间',
    `remark` VARCHAR(500) NULL DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT NULL DEFAULT NULL COMMENT '创建者ID',
    `update_by` BIGINT NULL DEFAULT NULL COMMENT '更新者ID',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除(0-未删除,1-已删除)',
    `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sys_user_username` (`username`),
    UNIQUE KEY `uk_sys_user_email` (`email`),
    UNIQUE KEY `uk_sys_user_phone` (`phone`),

    KEY `idx_sys_user_status` (`status`),
    KEY `idx_sys_user_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- ==========================================
-- 2. 角色表 (sys_role)
-- ==========================================
CREATE TABLE `sys_role` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
    `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码',
    `role_sort` INT NOT NULL DEFAULT 0 COMMENT '显示顺序',
    `data_scope` TINYINT NOT NULL DEFAULT 1 COMMENT '数据范围(1-全部,2-自定义,5-仅本人)',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态(0-禁用,1-启用)',
    `remark` VARCHAR(500) NULL DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT NULL DEFAULT NULL COMMENT '创建者ID',
    `update_by` BIGINT NULL DEFAULT NULL COMMENT '更新者ID',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除(0-未删除,1-已删除)',
    `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sys_role_code` (`role_code`),
    KEY `idx_sys_role_status` (`status`),
    KEY `idx_sys_role_sort` (`role_sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- ==========================================
-- 3. 权限表 (sys_permission)
-- ==========================================
CREATE TABLE `sys_permission` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父权限ID',
    `permission_name` VARCHAR(50) NOT NULL COMMENT '权限名称',
    `permission_code` VARCHAR(100) NOT NULL COMMENT '权限编码',
    `permission_type` TINYINT NOT NULL DEFAULT 1 COMMENT '权限类型(1-菜单,2-按钮,3-接口)',
    `path` VARCHAR(200) NULL DEFAULT NULL COMMENT '路由路径',
    `component` VARCHAR(200) NULL DEFAULT NULL COMMENT '组件路径',
    `icon` VARCHAR(100) NULL DEFAULT NULL COMMENT '图标',
    `order_num` INT NOT NULL DEFAULT 0 COMMENT '显示顺序',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态(0-禁用,1-启用)',
    `visible` TINYINT NOT NULL DEFAULT 1 COMMENT '是否显示(0-隐藏,1-显示)',
    `remark` VARCHAR(500) NULL DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT NULL DEFAULT NULL COMMENT '创建者ID',
    `update_by` BIGINT NULL DEFAULT NULL COMMENT '更新者ID',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除(0-未删除,1-已删除)',
    `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sys_permission_code` (`permission_code`),
    KEY `idx_sys_permission_parent_id` (`parent_id`),
    KEY `idx_sys_permission_type` (`permission_type`),
    KEY `idx_sys_permission_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- ==========================================
-- 4. 用户角色关联表 (sys_user_role)
-- ==========================================
CREATE TABLE `sys_user_role` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by` BIGINT NULL DEFAULT NULL COMMENT '创建者ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sys_user_role` (`user_id`, `role_id`),
    KEY `idx_sys_user_role_user_id` (`user_id`),
    KEY `idx_sys_user_role_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- ==========================================
-- 5. 角色权限关联表 (sys_role_permission)
-- ==========================================
CREATE TABLE `sys_role_permission` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `role_id` BIGINT NOT NULL COMMENT '角色ID',
    `permission_id` BIGINT NOT NULL COMMENT '权限ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `create_by` BIGINT NULL DEFAULT NULL COMMENT '创建者ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sys_role_permission` (`role_id`, `permission_id`),
    KEY `idx_sys_role_permission_role_id` (`role_id`),
    KEY `idx_sys_role_permission_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- ==========================================
-- 6. 字典类型表 (sys_dict_type)
-- ==========================================
CREATE TABLE `sys_dict_type` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `dict_name` VARCHAR(100) NOT NULL COMMENT '字典名称',
    `dict_type` VARCHAR(100) NOT NULL COMMENT '字典类型',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态(0-禁用,1-启用)',
    `remark` VARCHAR(500) NULL DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT NULL DEFAULT NULL COMMENT '创建者ID',
    `update_by` BIGINT NULL DEFAULT NULL COMMENT '更新者ID',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除(0-未删除,1-已删除)',
    `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sys_dict_type` (`dict_type`),
    KEY `idx_sys_dict_type_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典类型表';

-- ==========================================
-- 7. 字典数据表 (sys_dict_data)
-- ==========================================
CREATE TABLE `sys_dict_data` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `dict_sort` INT NOT NULL DEFAULT 0 COMMENT '字典排序',
    `dict_label` VARCHAR(100) NOT NULL COMMENT '字典标签',
    `dict_value` VARCHAR(100) NOT NULL COMMENT '字典键值',
    `dict_type` VARCHAR(100) NOT NULL COMMENT '字典类型',
    `css_class` VARCHAR(100) NULL DEFAULT NULL COMMENT '样式属性',
    `list_class` VARCHAR(100) NULL DEFAULT NULL COMMENT '表格样式',
    `is_default` TINYINT NOT NULL DEFAULT 0 COMMENT '是否默认(0-否,1-是)',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态(0-禁用,1-启用)',
    `remark` VARCHAR(500) NULL DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT NULL DEFAULT NULL COMMENT '创建者ID',
    `update_by` BIGINT NULL DEFAULT NULL COMMENT '更新者ID',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除(0-未删除,1-已删除)',
    `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
    PRIMARY KEY (`id`),
    KEY `idx_sys_dict_data_type` (`dict_type`),
    KEY `idx_sys_dict_data_status` (`status`),
    KEY `idx_sys_dict_data_sort` (`dict_sort`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典数据表';

-- ==========================================
-- 8. 系统配置表 (sys_config)
-- ==========================================
CREATE TABLE `sys_config` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `config_name` VARCHAR(100) NOT NULL COMMENT '参数名称',
    `config_key` VARCHAR(100) NOT NULL COMMENT '参数键名',
    `config_value` VARCHAR(500) NOT NULL COMMENT '参数键值',
    `config_type` TINYINT NOT NULL DEFAULT 0 COMMENT '系统内置(0-否,1-是)',
    `remark` VARCHAR(500) NULL DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT NULL DEFAULT NULL COMMENT '创建者ID',
    `update_by` BIGINT NULL DEFAULT NULL COMMENT '更新者ID',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除(0-未删除,1-已删除)',
    `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sys_config_key` (`config_key`),
    KEY `idx_sys_config_type` (`config_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- ==========================================
-- 9. 菜单表 (sys_menu)
-- ==========================================
CREATE TABLE `sys_menu` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `parent_id` BIGINT NOT NULL DEFAULT 0 COMMENT '父菜单ID',
    `menu_name` VARCHAR(50) NOT NULL COMMENT '菜单名称',
    `menu_type` TINYINT NOT NULL DEFAULT 1 COMMENT '菜单类型(1-目录,2-菜单,3-按钮)',
    `path` VARCHAR(200) NULL DEFAULT NULL COMMENT '路由路径',
    `component` VARCHAR(200) NULL DEFAULT NULL COMMENT '组件路径',
    `permission` VARCHAR(100) NULL DEFAULT NULL COMMENT '权限标识',
    `icon` VARCHAR(100) NULL DEFAULT NULL COMMENT '菜单图标',
    `order_num` INT NOT NULL DEFAULT 0 COMMENT '显示顺序',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态(0-禁用,1-启用)',
    `visible` TINYINT NOT NULL DEFAULT 1 COMMENT '是否显示(0-隐藏,1-显示)',
    `is_frame` TINYINT NOT NULL DEFAULT 0 COMMENT '是否外链(0-否,1-是)',
    `is_cache` TINYINT NOT NULL DEFAULT 0 COMMENT '是否缓存(0-否,1-是)',
    `remark` VARCHAR(500) NULL DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT NULL DEFAULT NULL COMMENT '创建者ID',
    `update_by` BIGINT NULL DEFAULT NULL COMMENT '更新者ID',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除(0-未删除,1-已删除)',
    `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
    PRIMARY KEY (`id`),
    KEY `idx_sys_menu_parent_id` (`parent_id`),
    KEY `idx_sys_menu_type` (`menu_type`),
    KEY `idx_sys_menu_status` (`status`),
    KEY `idx_sys_menu_order` (`order_num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='菜单表';



-- ==========================================
-- 外键约束 (可选，根据需要启用)
-- ==========================================

-- ALTER TABLE `sys_user_role` ADD CONSTRAINT `fk_sys_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE;
-- ALTER TABLE `sys_user_role` ADD CONSTRAINT `fk_sys_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE;
-- ALTER TABLE `sys_role_permission` ADD CONSTRAINT `fk_sys_role_permission_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE;
-- ALTER TABLE `sys_role_permission` ADD CONSTRAINT `fk_sys_role_permission_permission` FOREIGN KEY (`permission_id`) REFERENCES `sys_permission` (`id`) ON DELETE CASCADE;

-- ==========================================
-- 脚本执行完成提示
-- ==========================================
SELECT 'Database schema initialization completed successfully!' AS status; 