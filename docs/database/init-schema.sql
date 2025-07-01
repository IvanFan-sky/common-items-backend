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
-- 10. 社交登录表 (sys_user_social)
-- ==========================================
CREATE TABLE `sys_user_social` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `social_type` VARCHAR(20) NOT NULL COMMENT '社交平台类型(wechat,qq,weibo,alipay,github等)',
    `social_id` VARCHAR(100) NOT NULL COMMENT '社交平台用户标识',
    `social_nickname` VARCHAR(100) NULL DEFAULT NULL COMMENT '社交平台昵称',
    `social_avatar` VARCHAR(200) NULL DEFAULT NULL COMMENT '社交平台头像',
    `social_email` VARCHAR(100) NULL DEFAULT NULL COMMENT '社交平台邮箱',
    `social_phone` VARCHAR(20) NULL DEFAULT NULL COMMENT '社交平台手机号',
    `social_gender` TINYINT NULL DEFAULT 0 COMMENT '社交平台性别(0-未知,1-男,2-女)',
    `union_id` VARCHAR(100) NULL DEFAULT NULL COMMENT '微信UnionID(用于多应用统一身份)',
    `open_id` VARCHAR(100) NULL DEFAULT NULL COMMENT '微信OpenID',
    `access_token` VARCHAR(500) NULL DEFAULT NULL COMMENT '访问令牌',
    `refresh_token` VARCHAR(500) NULL DEFAULT NULL COMMENT '刷新令牌',
    `expires_in` BIGINT NULL DEFAULT NULL COMMENT '令牌过期时间戳',
    `scope` VARCHAR(200) NULL DEFAULT NULL COMMENT '授权范围',
    `raw_user_info` TEXT NULL DEFAULT NULL COMMENT '原始用户信息JSON',
    `bind_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '绑定时间',
    `last_login_time` DATETIME NULL DEFAULT NULL COMMENT '最后登录时间',
    `login_count` INT NOT NULL DEFAULT 0 COMMENT '登录次数',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态(0-解绑,1-已绑定)',
    `remark` VARCHAR(500) NULL DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT NULL DEFAULT NULL COMMENT '创建者ID',
    `update_by` BIGINT NULL DEFAULT NULL COMMENT '更新者ID',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除(0-未删除,1-已删除)',
    `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sys_user_social_platform` (`social_type`, `social_id`),
    UNIQUE KEY `uk_sys_user_social_union` (`social_type`, `union_id`),
    KEY `idx_sys_user_social_user_id` (`user_id`),
    KEY `idx_sys_user_social_type` (`social_type`),
    KEY `idx_sys_user_social_status` (`status`),
    KEY `idx_sys_user_social_bind_time` (`bind_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='社交登录表';



-- ==========================================
-- 外键约束 (可选，根据需要启用)
-- ==========================================

-- ==========================================
-- Stage 1 外键约束 (可选，根据需要启用)
-- ==========================================
-- ALTER TABLE `sys_user_role` ADD CONSTRAINT `fk_sys_user_role_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE;
-- ALTER TABLE `sys_user_role` ADD CONSTRAINT `fk_sys_user_role_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE;
-- ALTER TABLE `sys_role_permission` ADD CONSTRAINT `fk_sys_role_permission_role` FOREIGN KEY (`role_id`) REFERENCES `sys_role` (`id`) ON DELETE CASCADE;
-- ALTER TABLE `sys_role_permission` ADD CONSTRAINT `fk_sys_role_permission_permission` FOREIGN KEY (`permission_id`) REFERENCES `sys_permission` (`id`) ON DELETE CASCADE;
-- ALTER TABLE `sys_user_social` ADD CONSTRAINT `fk_sys_user_social_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE CASCADE;

-- ==========================================
-- Stage 2 表结构
-- ==========================================

-- ==========================================
-- 1. 文件信息表 (file_info)
-- ==========================================
CREATE TABLE `file_info` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `file_name` VARCHAR(255) NOT NULL COMMENT '原始文件名',
    `file_path` VARCHAR(500) NOT NULL COMMENT '文件存储路径',
    `file_url` VARCHAR(500) NULL DEFAULT NULL COMMENT '文件访问URL',
    `file_size` BIGINT NOT NULL DEFAULT 0 COMMENT '文件大小(字节)',
    `file_type` VARCHAR(50) NOT NULL COMMENT '文件类型(image,document,video,audio,other)',
    `file_extension` VARCHAR(20) NOT NULL COMMENT '文件扩展名',
    `mime_type` VARCHAR(100) NOT NULL COMMENT 'MIME类型',
    `file_md5` VARCHAR(32) NOT NULL COMMENT '文件MD5值',
    `file_sha1` VARCHAR(40) NULL DEFAULT NULL COMMENT '文件SHA1值',
    `storage_type` VARCHAR(20) NOT NULL DEFAULT 'local' COMMENT '存储类型(local,oss,cos,qiniu,minio)',
    `storage_id` BIGINT NULL DEFAULT NULL COMMENT '存储配置ID',
    `bucket_name` VARCHAR(100) NULL DEFAULT NULL COMMENT '存储桶名称',
    `object_key` VARCHAR(500) NULL DEFAULT NULL COMMENT '对象存储Key',
    `upload_ip` VARCHAR(50) NULL DEFAULT NULL COMMENT '上传IP地址',
    `upload_user_agent` VARCHAR(500) NULL DEFAULT NULL COMMENT '上传用户代理',
    `thumbnail_path` VARCHAR(500) NULL DEFAULT NULL COMMENT '缩略图路径',
    `width` INT NULL DEFAULT NULL COMMENT '图片宽度(像素)',
    `height` INT NULL DEFAULT NULL COMMENT '图片高度(像素)',
    `duration` INT NULL DEFAULT NULL COMMENT '音视频时长(秒)',
    `download_count` INT NOT NULL DEFAULT 0 COMMENT '下载次数',
    `view_count` INT NOT NULL DEFAULT 0 COMMENT '查看次数',
    `is_public` TINYINT NOT NULL DEFAULT 0 COMMENT '是否公开(0-私有,1-公开)',
    `access_level` TINYINT NOT NULL DEFAULT 1 COMMENT '访问级别(1-所有人,2-登录用户,3-指定用户)',
    `expire_time` DATETIME NULL DEFAULT NULL COMMENT '过期时间',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态(0-禁用,1-正常,2-处理中,3-失败)',
    `remark` VARCHAR(500) NULL DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT NULL DEFAULT NULL COMMENT '创建者ID',
    `update_by` BIGINT NULL DEFAULT NULL COMMENT '更新者ID',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除(0-未删除,1-已删除)',
    `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_file_info_md5` (`file_md5`),
    KEY `idx_file_info_type` (`file_type`),
    KEY `idx_file_info_storage` (`storage_type`),
    KEY `idx_file_info_create_by` (`create_by`),
    KEY `idx_file_info_create_time` (`create_time`),
    KEY `idx_file_info_status` (`status`),
    KEY `idx_file_info_expire` (`expire_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件信息表';

-- ==========================================
-- 2. 文件存储表 (file_storage)
-- ==========================================
CREATE TABLE `file_storage` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `storage_name` VARCHAR(100) NOT NULL COMMENT '存储名称',
    `storage_code` VARCHAR(50) NOT NULL COMMENT '存储编码',
    `storage_type` VARCHAR(20) NOT NULL COMMENT '存储类型(local,oss,cos,qiniu,minio)',
    `access_key` VARCHAR(200) NULL DEFAULT NULL COMMENT '访问密钥',
    `secret_key` VARCHAR(200) NULL DEFAULT NULL COMMENT '私有密钥',
    `endpoint` VARCHAR(200) NULL DEFAULT NULL COMMENT '服务端点',
    `region` VARCHAR(50) NULL DEFAULT NULL COMMENT '存储区域',
    `bucket_name` VARCHAR(100) NULL DEFAULT NULL COMMENT '存储桶名称',
    `domain` VARCHAR(200) NULL DEFAULT NULL COMMENT '自定义域名',
    `base_path` VARCHAR(200) NULL DEFAULT '/' COMMENT '基础路径',
    `path_style` TINYINT NOT NULL DEFAULT 0 COMMENT '路径样式(0-虚拟主机,1-路径)',
    `is_https` TINYINT NOT NULL DEFAULT 1 COMMENT '是否HTTPS(0-HTTP,1-HTTPS)',
    `is_default` TINYINT NOT NULL DEFAULT 0 COMMENT '是否默认(0-否,1-是)',
    `max_file_size` BIGINT NOT NULL DEFAULT 52428800 COMMENT '最大文件大小(字节,默认50MB)',
    `allowed_types` VARCHAR(500) NULL DEFAULT NULL COMMENT '允许的文件类型(逗号分隔)',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序序号',
    `config_json` TEXT NULL DEFAULT NULL COMMENT '扩展配置JSON',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态(0-禁用,1-启用)',
    `remark` VARCHAR(500) NULL DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT NULL DEFAULT NULL COMMENT '创建者ID',
    `update_by` BIGINT NULL DEFAULT NULL COMMENT '更新者ID',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除(0-未删除,1-已删除)',
    `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_file_storage_code` (`storage_code`),
    KEY `idx_file_storage_type` (`storage_type`),
    KEY `idx_file_storage_default` (`is_default`),
    KEY `idx_file_storage_status` (`status`),
    KEY `idx_file_storage_sort` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件存储表';

-- ==========================================
-- 3. 文件关联表 (file_relation)
-- ==========================================
CREATE TABLE `file_relation` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `file_id` BIGINT NOT NULL COMMENT '文件ID',
    `relation_type` VARCHAR(50) NOT NULL COMMENT '关联类型(user_avatar,post_image,document等)',
    `relation_id` BIGINT NOT NULL COMMENT '关联实体ID',
    `relation_field` VARCHAR(50) NULL DEFAULT NULL COMMENT '关联字段名',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序序号',
    `is_main` TINYINT NOT NULL DEFAULT 0 COMMENT '是否主要文件(0-否,1-是)',
    `usage_type` VARCHAR(30) NOT NULL DEFAULT 'normal' COMMENT '使用类型(normal,thumbnail,preview)',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态(0-禁用,1-启用)',
    `remark` VARCHAR(500) NULL DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT NULL DEFAULT NULL COMMENT '创建者ID',
    `update_by` BIGINT NULL DEFAULT NULL COMMENT '更新者ID',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除(0-未删除,1-已删除)',
    `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_file_relation` (`file_id`, `relation_type`, `relation_id`, `usage_type`),
    KEY `idx_file_relation_file` (`file_id`),
    KEY `idx_file_relation_entity` (`relation_type`, `relation_id`),
    KEY `idx_file_relation_main` (`is_main`),
    KEY `idx_file_relation_sort` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件关联表';

-- ==========================================
-- 4. 文件版本表 (file_version)
-- ==========================================
CREATE TABLE `file_version` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `file_id` BIGINT NOT NULL COMMENT '文件ID',
    `version_num` VARCHAR(20) NOT NULL COMMENT '版本号(如v1.0, v1.1)',
    `version_name` VARCHAR(100) NULL DEFAULT NULL COMMENT '版本名称',
    `file_name` VARCHAR(255) NOT NULL COMMENT '文件名',
    `file_path` VARCHAR(500) NOT NULL COMMENT '文件路径',
    `file_size` BIGINT NOT NULL DEFAULT 0 COMMENT '文件大小',
    `file_md5` VARCHAR(32) NOT NULL COMMENT '文件MD5值',
    `change_type` VARCHAR(20) NOT NULL DEFAULT 'update' COMMENT '变更类型(create,update,replace,delete)',
    `change_log` TEXT NULL DEFAULT NULL COMMENT '变更日志',
    `is_current` TINYINT NOT NULL DEFAULT 0 COMMENT '是否当前版本(0-否,1-是)',
    `download_count` INT NOT NULL DEFAULT 0 COMMENT '下载次数',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态(0-禁用,1-启用,2-已删除)',
    `remark` VARCHAR(500) NULL DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by` BIGINT NULL DEFAULT NULL COMMENT '创建者ID',
    `update_by` BIGINT NULL DEFAULT NULL COMMENT '更新者ID',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除(0-未删除,1-已删除)',
    `version` INT NOT NULL DEFAULT 1 COMMENT '版本号',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_file_version` (`file_id`, `version_num`),
    KEY `idx_file_version_file` (`file_id`),
    KEY `idx_file_version_current` (`is_current`),
    KEY `idx_file_version_create_time` (`create_time`),
    KEY `idx_file_version_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件版本表';

-- ==========================================
-- 5. 操作日志表 (log_operation)
-- ==========================================
CREATE TABLE `log_operation` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `trace_id` VARCHAR(32) NULL DEFAULT NULL COMMENT '请求追踪ID',
    `user_id` BIGINT NULL DEFAULT NULL COMMENT '操作用户ID',
    `username` VARCHAR(50) NULL DEFAULT NULL COMMENT '用户名',
    `operation_name` VARCHAR(100) NOT NULL COMMENT '操作名称',
    `operation_type` VARCHAR(20) NOT NULL COMMENT '操作类型(CREATE,UPDATE,DELETE,QUERY,LOGIN,LOGOUT,EXPORT,IMPORT)',
    `business_type` VARCHAR(50) NULL DEFAULT NULL COMMENT '业务类型(USER,ROLE,PERMISSION,FILE等)',
    `method` VARCHAR(10) NOT NULL COMMENT '请求方法(GET,POST,PUT,DELETE)',
    `request_url` VARCHAR(500) NOT NULL COMMENT '请求URL',
    `request_ip` VARCHAR(50) NOT NULL COMMENT '请求IP',
    `request_location` VARCHAR(100) NULL DEFAULT NULL COMMENT '请求地址',
    `request_params` TEXT NULL DEFAULT NULL COMMENT '请求参数',
    `request_body` TEXT NULL DEFAULT NULL COMMENT '请求体',
    `response_result` TEXT NULL DEFAULT NULL COMMENT '响应结果',
    `response_status` INT NULL DEFAULT NULL COMMENT '响应状态码',
    `error_msg` TEXT NULL DEFAULT NULL COMMENT '错误信息',
    `execute_time` BIGINT NULL DEFAULT NULL COMMENT '执行时间(毫秒)',
    `user_agent` VARCHAR(500) NULL DEFAULT NULL COMMENT '用户代理',
    `browser_type` VARCHAR(50) NULL DEFAULT NULL COMMENT '浏览器类型',
    `os_type` VARCHAR(50) NULL DEFAULT NULL COMMENT '操作系统',
    `device_type` VARCHAR(20) NULL DEFAULT NULL COMMENT '设备类型(PC,MOBILE,TABLET)',
    `module_name` VARCHAR(50) NULL DEFAULT NULL COMMENT '模块名称',
    `class_name` VARCHAR(200) NULL DEFAULT NULL COMMENT '类名',
    `method_name` VARCHAR(100) NULL DEFAULT NULL COMMENT '方法名',
    `operation_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态(0-失败,1-成功,2-异常)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_log_operation_user` (`user_id`),
    KEY `idx_log_operation_type` (`operation_type`),
    KEY `idx_log_operation_time` (`operation_time`),
    KEY `idx_log_operation_ip` (`request_ip`),
    KEY `idx_log_operation_status` (`status`),
    KEY `idx_log_operation_trace` (`trace_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- ==========================================
-- 6. 登录日志表 (log_login)
-- ==========================================
CREATE TABLE `log_login` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `session_id` VARCHAR(100) NULL DEFAULT NULL COMMENT '会话ID',
    `user_id` BIGINT NULL DEFAULT NULL COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `login_type` VARCHAR(20) NOT NULL DEFAULT 'password' COMMENT '登录类型(password,wechat,qq,sms,email)',
    `login_method` VARCHAR(20) NOT NULL DEFAULT 'web' COMMENT '登录方式(web,app,api)',
    `login_ip` VARCHAR(50) NOT NULL COMMENT '登录IP',
    `login_location` VARCHAR(100) NULL DEFAULT NULL COMMENT '登录地点',
    `user_agent` VARCHAR(500) NULL DEFAULT NULL COMMENT '用户代理',
    `browser_type` VARCHAR(50) NULL DEFAULT NULL COMMENT '浏览器类型',
    `os_type` VARCHAR(50) NULL DEFAULT NULL COMMENT '操作系统',
    `device_type` VARCHAR(20) NULL DEFAULT NULL COMMENT '设备类型(PC,MOBILE,TABLET)',
    `device_id` VARCHAR(100) NULL DEFAULT NULL COMMENT '设备标识',
    `login_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
    `logout_time` DATETIME NULL DEFAULT NULL COMMENT '登出时间',
    `online_duration` BIGINT NULL DEFAULT NULL COMMENT '在线时长(秒)',
    `login_result` TINYINT NOT NULL DEFAULT 1 COMMENT '登录结果(0-失败,1-成功)',
    `fail_reason` VARCHAR(200) NULL DEFAULT NULL COMMENT '失败原因',
    `token` VARCHAR(500) NULL DEFAULT NULL COMMENT '登录令牌(脱敏)',
    `refresh_count` INT NOT NULL DEFAULT 0 COMMENT '令牌刷新次数',
    `is_forced_logout` TINYINT NOT NULL DEFAULT 0 COMMENT '是否强制登出(0-否,1-是)',
    `risk_level` TINYINT NOT NULL DEFAULT 0 COMMENT '风险级别(0-正常,1-低风险,2-中风险,3-高风险)',
    `remark` VARCHAR(500) NULL DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_log_login_user` (`user_id`),
    KEY `idx_log_login_username` (`username`),
    KEY `idx_log_login_ip` (`login_ip`),
    KEY `idx_log_login_time` (`login_time`),
    KEY `idx_log_login_result` (`login_result`),
    KEY `idx_log_login_risk` (`risk_level`),
    KEY `idx_log_login_session` (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录日志表';

-- ==========================================
-- 7. 错误日志表 (log_error)
-- ==========================================
CREATE TABLE `log_error` (
    `id` BIGINT NOT NULL COMMENT '主键ID',
    `trace_id` VARCHAR(32) NULL DEFAULT NULL COMMENT '请求追踪ID',
    `error_id` VARCHAR(50) NULL DEFAULT NULL COMMENT '错误ID',
    `error_type` VARCHAR(50) NOT NULL COMMENT '错误类型(SYSTEM,BUSINESS,NETWORK,DATABASE,THIRD_PARTY)',
    `error_level` VARCHAR(20) NOT NULL DEFAULT 'ERROR' COMMENT '错误级别(DEBUG,INFO,WARN,ERROR,FATAL)',
    `error_code` VARCHAR(50) NULL DEFAULT NULL COMMENT '错误代码',
    `error_message` TEXT NOT NULL COMMENT '错误信息',
    `exception_class` VARCHAR(200) NULL DEFAULT NULL COMMENT '异常类名',
    `stack_trace` LONGTEXT NULL DEFAULT NULL COMMENT '堆栈信息',
    `user_id` BIGINT NULL DEFAULT NULL COMMENT '用户ID',
    `username` VARCHAR(50) NULL DEFAULT NULL COMMENT '用户名',
    `request_method` VARCHAR(10) NULL DEFAULT NULL COMMENT '请求方法',
    `request_url` VARCHAR(500) NULL DEFAULT NULL COMMENT '请求URL',
    `request_params` TEXT NULL DEFAULT NULL COMMENT '请求参数',
    `request_ip` VARCHAR(50) NULL DEFAULT NULL COMMENT '请求IP',
    `user_agent` VARCHAR(500) NULL DEFAULT NULL COMMENT '用户代理',
    `module_name` VARCHAR(50) NULL DEFAULT NULL COMMENT '模块名称',
    `class_name` VARCHAR(200) NULL DEFAULT NULL COMMENT '类名',
    `method_name` VARCHAR(100) NULL DEFAULT NULL COMMENT '方法名',
    `line_number` INT NULL DEFAULT NULL COMMENT '错误行号',
    `server_name` VARCHAR(100) NULL DEFAULT NULL COMMENT '服务器名称',
    `server_ip` VARCHAR(50) NULL DEFAULT NULL COMMENT '服务器IP',
    `thread_name` VARCHAR(100) NULL DEFAULT NULL COMMENT '线程名称',
    `error_count` INT NOT NULL DEFAULT 1 COMMENT '错误次数',
    `first_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '首次发生时间',
    `last_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后发生时间',
    `is_resolved` TINYINT NOT NULL DEFAULT 0 COMMENT '是否已解决(0-未解决,1-已解决)',
    `resolve_time` DATETIME NULL DEFAULT NULL COMMENT '解决时间',
    `resolve_by` BIGINT NULL DEFAULT NULL COMMENT '解决人ID',
    `resolve_note` TEXT NULL DEFAULT NULL COMMENT '解决说明',
    `notify_status` TINYINT NOT NULL DEFAULT 0 COMMENT '通知状态(0-未通知,1-已通知,2-通知失败)',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态(0-忽略,1-正常,2-关注)',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_log_error_type` (`error_type`),
    KEY `idx_log_error_level` (`error_level`),
    KEY `idx_log_error_code` (`error_code`),
    KEY `idx_log_error_user` (`user_id`),
    KEY `idx_log_error_time` (`create_time`),
    KEY `idx_log_error_resolved` (`is_resolved`),
    KEY `idx_log_error_trace` (`trace_id`),
    KEY `idx_log_error_count` (`error_count`),
    KEY `idx_log_error_first_time` (`first_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='错误日志表';

-- ==========================================
-- Stage 2 外键约束 (可选，根据需要启用)
-- ==========================================
-- ALTER TABLE `file_info` ADD CONSTRAINT `fk_file_info_storage` FOREIGN KEY (`storage_id`) REFERENCES `file_storage` (`id`) ON DELETE SET NULL;
-- ALTER TABLE `file_relation` ADD CONSTRAINT `fk_file_relation_file` FOREIGN KEY (`file_id`) REFERENCES `file_info` (`id`) ON DELETE CASCADE;
-- ALTER TABLE `file_version` ADD CONSTRAINT `fk_file_version_file` FOREIGN KEY (`file_id`) REFERENCES `file_info` (`id`) ON DELETE CASCADE;
-- ALTER TABLE `log_operation` ADD CONSTRAINT `fk_log_operation_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE SET NULL;
-- ALTER TABLE `log_login` ADD CONSTRAINT `fk_log_login_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE SET NULL;
-- ALTER TABLE `log_error` ADD CONSTRAINT `fk_log_error_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`) ON DELETE SET NULL;
-- ALTER TABLE `log_error` ADD CONSTRAINT `fk_log_error_resolve_by` FOREIGN KEY (`resolve_by`) REFERENCES `sys_user` (`id`) ON DELETE SET NULL;

-- ==========================================
-- 脚本执行完成提示
-- ==========================================
SELECT 'Database schema initialization completed successfully!' AS status; 