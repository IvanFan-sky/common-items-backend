# 数据库表结构设计文档

## 📋 表结构总览

### Stage 1: 核心基础功能表（10张）

| 表名 | 中文名称 | 功能描述 | 状态 |
|-----|---------|---------|------|
| sys_user | 用户表 | 用户基本信息管理 | ✅ |
| sys_role | 角色表 | 系统角色定义 | ✅ |
| sys_permission | 权限表 | 系统权限定义 | ✅ |
| sys_user_role | 用户角色关联表 | 用户与角色的多对多关系 | ✅ |
| sys_role_permission | 角色权限关联表 | 角色与权限的多对多关系 | ✅ |
| sys_dict_type | 字典类型表 | 字典数据类型管理 | ✅ |
| sys_dict_data | 字典数据表 | 字典数据值管理 | ✅ |
| sys_config | 系统配置表 | 系统参数配置 | ✅ |
| sys_menu | 菜单表 | 系统菜单管理 | ✅ |
| sys_dept | 部门表 | 组织架构管理 | ✅ |

---

## 📊 Stage 1 表结构详细设计

### 1. sys_user (用户表)

**表说明**: 存储系统用户的基本信息

| 字段名 | 数据类型 | 长度 | 是否为空 | 默认值 | 注释 |
|--------|----------|------|----------|---------|------|
| id | BIGINT | - | NOT NULL | - | 主键ID |
| username | VARCHAR | 50 | NOT NULL | - | 用户名 |
| password | VARCHAR | 100 | NOT NULL | - | 密码(BCrypt加密) |
| nickname | VARCHAR | 50 | NULL | - | 用户昵称 |
| email | VARCHAR | 100 | NULL | - | 邮箱地址 |
| phone | VARCHAR | 20 | NULL | - | 手机号码 |
| avatar | VARCHAR | 200 | NULL | - | 头像地址 |
| gender | TINYINT | - | NULL | 0 | 性别(0-未知,1-男,2-女) |
| birthday | DATE | - | NULL | - | 生日 |
| status | TINYINT | - | NOT NULL | 1 | 状态(0-禁用,1-启用) |
| dept_id | BIGINT | - | NULL | - | 所属部门ID |
| login_ip | VARCHAR | 50 | NULL | - | 最近登录IP |
| login_time | DATETIME | - | NULL | - | 最近登录时间 |
| remark | VARCHAR | 500 | NULL | - | 备注 |
| create_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 更新时间 |
| create_by | BIGINT | - | NULL | - | 创建者ID |
| update_by | BIGINT | - | NULL | - | 更新者ID |
| deleted | TINYINT | - | NOT NULL | 0 | 逻辑删除(0-未删除,1-已删除) |
| version | INT | - | NOT NULL | 1 | 版本号 |

**索引设计**:
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_sys_user_username` (`username`)
- UNIQUE KEY `uk_sys_user_email` (`email`)
- UNIQUE KEY `uk_sys_user_phone` (`phone`)
- KEY `idx_sys_user_dept_id` (`dept_id`)
- KEY `idx_sys_user_status` (`status`)
- KEY `idx_sys_user_create_time` (`create_time`)

### 2. sys_role (角色表)

**表说明**: 系统角色定义，支持角色层级

| 字段名 | 数据类型 | 长度 | 是否为空 | 默认值 | 注释 |
|--------|----------|------|----------|---------|------|
| id | BIGINT | - | NOT NULL | - | 主键ID |
| role_name | VARCHAR | 50 | NOT NULL | - | 角色名称 |
| role_code | VARCHAR | 50 | NOT NULL | - | 角色编码 |
| role_sort | INT | - | NOT NULL | 0 | 显示顺序 |
| data_scope | TINYINT | - | NOT NULL | 1 | 数据范围(1-全部,2-自定义,3-本部门,4-本部门及以下,5-仅本人) |
| status | TINYINT | - | NOT NULL | 1 | 状态(0-禁用,1-启用) |
| remark | VARCHAR | 500 | NULL | - | 备注 |
| create_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 更新时间 |
| create_by | BIGINT | - | NULL | - | 创建者ID |
| update_by | BIGINT | - | NULL | - | 更新者ID |
| deleted | TINYINT | - | NOT NULL | 0 | 逻辑删除(0-未删除,1-已删除) |
| version | INT | - | NOT NULL | 1 | 版本号 |

**索引设计**:
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_sys_role_code` (`role_code`)
- KEY `idx_sys_role_status` (`status`)
- KEY `idx_sys_role_sort` (`role_sort`)

### 3. sys_permission (权限表)

**表说明**: 系统权限定义，支持菜单权限和按钮权限

| 字段名 | 数据类型 | 长度 | 是否为空 | 默认值 | 注释 |
|--------|----------|------|----------|---------|------|
| id | BIGINT | - | NOT NULL | - | 主键ID |
| parent_id | BIGINT | - | NOT NULL | 0 | 父权限ID |
| permission_name | VARCHAR | 50 | NOT NULL | - | 权限名称 |
| permission_code | VARCHAR | 100 | NOT NULL | - | 权限编码 |
| permission_type | TINYINT | - | NOT NULL | 1 | 权限类型(1-菜单,2-按钮,3-接口) |
| path | VARCHAR | 200 | NULL | - | 路由路径 |
| component | VARCHAR | 200 | NULL | - | 组件路径 |
| icon | VARCHAR | 100 | NULL | - | 图标 |
| order_num | INT | - | NOT NULL | 0 | 显示顺序 |
| status | TINYINT | - | NOT NULL | 1 | 状态(0-禁用,1-启用) |
| visible | TINYINT | - | NOT NULL | 1 | 是否显示(0-隐藏,1-显示) |
| remark | VARCHAR | 500 | NULL | - | 备注 |
| create_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 更新时间 |
| create_by | BIGINT | - | NULL | - | 创建者ID |
| update_by | BIGINT | - | NULL | - | 更新者ID |
| deleted | TINYINT | - | NOT NULL | 0 | 逻辑删除(0-未删除,1-已删除) |
| version | INT | - | NOT NULL | 1 | 版本号 |

**索引设计**:
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_sys_permission_code` (`permission_code`)
- KEY `idx_sys_permission_parent_id` (`parent_id`)
- KEY `idx_sys_permission_type` (`permission_type`)
- KEY `idx_sys_permission_status` (`status`)

### 4. sys_user_role (用户角色关联表)

**表说明**: 用户与角色的多对多关联关系

| 字段名 | 数据类型 | 长度 | 是否为空 | 默认值 | 注释 |
|--------|----------|------|----------|---------|------|
| id | BIGINT | - | NOT NULL | - | 主键ID |
| user_id | BIGINT | - | NOT NULL | - | 用户ID |
| role_id | BIGINT | - | NOT NULL | - | 角色ID |
| create_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| create_by | BIGINT | - | NULL | - | 创建者ID |

**索引设计**:
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_sys_user_role` (`user_id`, `role_id`)
- KEY `idx_sys_user_role_user_id` (`user_id`)
- KEY `idx_sys_user_role_role_id` (`role_id`)

### 5. sys_role_permission (角色权限关联表)

**表说明**: 角色与权限的多对多关联关系

| 字段名 | 数据类型 | 长度 | 是否为空 | 默认值 | 注释 |
|--------|----------|------|----------|---------|------|
| id | BIGINT | - | NOT NULL | - | 主键ID |
| role_id | BIGINT | - | NOT NULL | - | 角色ID |
| permission_id | BIGINT | - | NOT NULL | - | 权限ID |
| create_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| create_by | BIGINT | - | NULL | - | 创建者ID |

**索引设计**:
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_sys_role_permission` (`role_id`, `permission_id`)
- KEY `idx_sys_role_permission_role_id` (`role_id`)
- KEY `idx_sys_role_permission_permission_id` (`permission_id`)

### 6. sys_dict_type (字典类型表)

**表说明**: 字典数据类型管理

| 字段名 | 数据类型 | 长度 | 是否为空 | 默认值 | 注释 |
|--------|----------|------|----------|---------|------|
| id | BIGINT | - | NOT NULL | - | 主键ID |
| dict_name | VARCHAR | 100 | NOT NULL | - | 字典名称 |
| dict_type | VARCHAR | 100 | NOT NULL | - | 字典类型 |
| status | TINYINT | - | NOT NULL | 1 | 状态(0-禁用,1-启用) |
| remark | VARCHAR | 500 | NULL | - | 备注 |
| create_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 更新时间 |
| create_by | BIGINT | - | NULL | - | 创建者ID |
| update_by | BIGINT | - | NULL | - | 更新者ID |
| deleted | TINYINT | - | NOT NULL | 0 | 逻辑删除(0-未删除,1-已删除) |
| version | INT | - | NOT NULL | 1 | 版本号 |

**索引设计**:
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_sys_dict_type` (`dict_type`)
- KEY `idx_sys_dict_type_status` (`status`)

### 7. sys_dict_data (字典数据表)

**表说明**: 字典数据值管理

| 字段名 | 数据类型 | 长度 | 是否为空 | 默认值 | 注释 |
|--------|----------|------|----------|---------|------|
| id | BIGINT | - | NOT NULL | - | 主键ID |
| dict_sort | INT | - | NOT NULL | 0 | 字典排序 |
| dict_label | VARCHAR | 100 | NOT NULL | - | 字典标签 |
| dict_value | VARCHAR | 100 | NOT NULL | - | 字典键值 |
| dict_type | VARCHAR | 100 | NOT NULL | - | 字典类型 |
| css_class | VARCHAR | 100 | NULL | - | 样式属性 |
| list_class | VARCHAR | 100 | NULL | - | 表格样式 |
| is_default | TINYINT | - | NOT NULL | 0 | 是否默认(0-否,1-是) |
| status | TINYINT | - | NOT NULL | 1 | 状态(0-禁用,1-启用) |
| remark | VARCHAR | 500 | NULL | - | 备注 |
| create_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 更新时间 |
| create_by | BIGINT | - | NULL | - | 创建者ID |
| update_by | BIGINT | - | NULL | - | 更新者ID |
| deleted | TINYINT | - | NOT NULL | 0 | 逻辑删除(0-未删除,1-已删除) |
| version | INT | - | NOT NULL | 1 | 版本号 |

**索引设计**:
- PRIMARY KEY (`id`)
- KEY `idx_sys_dict_data_type` (`dict_type`)
- KEY `idx_sys_dict_data_status` (`status`)
- KEY `idx_sys_dict_data_sort` (`dict_sort`)

### 8. sys_config (系统配置表)

**表说明**: 系统参数配置管理

| 字段名 | 数据类型 | 长度 | 是否为空 | 默认值 | 注释 |
|--------|----------|------|----------|---------|------|
| id | BIGINT | - | NOT NULL | - | 主键ID |
| config_name | VARCHAR | 100 | NOT NULL | - | 参数名称 |
| config_key | VARCHAR | 100 | NOT NULL | - | 参数键名 |
| config_value | VARCHAR | 500 | NOT NULL | - | 参数键值 |
| config_type | TINYINT | - | NOT NULL | 0 | 系统内置(0-否,1-是) |
| remark | VARCHAR | 500 | NULL | - | 备注 |
| create_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 更新时间 |
| create_by | BIGINT | - | NULL | - | 创建者ID |
| update_by | BIGINT | - | NULL | - | 更新者ID |
| deleted | TINYINT | - | NOT NULL | 0 | 逻辑删除(0-未删除,1-已删除) |
| version | INT | - | NOT NULL | 1 | 版本号 |

**索引设计**:
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_sys_config_key` (`config_key`)
- KEY `idx_sys_config_type` (`config_type`)

### 9. sys_menu (菜单表)

**表说明**: 系统菜单管理，支持树形结构

| 字段名 | 数据类型 | 长度 | 是否为空 | 默认值 | 注释 |
|--------|----------|------|----------|---------|------|
| id | BIGINT | - | NOT NULL | - | 主键ID |
| parent_id | BIGINT | - | NOT NULL | 0 | 父菜单ID |
| menu_name | VARCHAR | 50 | NOT NULL | - | 菜单名称 |
| menu_type | TINYINT | - | NOT NULL | 1 | 菜单类型(1-目录,2-菜单,3-按钮) |
| path | VARCHAR | 200 | NULL | - | 路由路径 |
| component | VARCHAR | 200 | NULL | - | 组件路径 |
| permission | VARCHAR | 100 | NULL | - | 权限标识 |
| icon | VARCHAR | 100 | NULL | - | 菜单图标 |
| order_num | INT | - | NOT NULL | 0 | 显示顺序 |
| status | TINYINT | - | NOT NULL | 1 | 状态(0-禁用,1-启用) |
| visible | TINYINT | - | NOT NULL | 1 | 是否显示(0-隐藏,1-显示) |
| is_frame | TINYINT | - | NOT NULL | 0 | 是否外链(0-否,1-是) |
| is_cache | TINYINT | - | NOT NULL | 0 | 是否缓存(0-否,1-是) |
| remark | VARCHAR | 500 | NULL | - | 备注 |
| create_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 更新时间 |
| create_by | BIGINT | - | NULL | - | 创建者ID |
| update_by | BIGINT | - | NULL | - | 更新者ID |
| deleted | TINYINT | - | NOT NULL | 0 | 逻辑删除(0-未删除,1-已删除) |
| version | INT | - | NOT NULL | 1 | 版本号 |

**索引设计**:
- PRIMARY KEY (`id`)
- KEY `idx_sys_menu_parent_id` (`parent_id`)
- KEY `idx_sys_menu_type` (`menu_type`)
- KEY `idx_sys_menu_status` (`status`)
- KEY `idx_sys_menu_order` (`order_num`)

### 10. sys_dept (部门表)

**表说明**: 组织架构管理，支持部门层级结构

| 字段名 | 数据类型 | 长度 | 是否为空 | 默认值 | 注释 |
|--------|----------|------|----------|---------|------|
| id | BIGINT | - | NOT NULL | - | 主键ID |
| parent_id | BIGINT | - | NOT NULL | 0 | 父部门ID |
| ancestors | VARCHAR | 500 | NULL | - | 祖级列表 |
| dept_name | VARCHAR | 50 | NOT NULL | - | 部门名称 |
| dept_code | VARCHAR | 50 | NULL | - | 部门编码 |
| order_num | INT | - | NOT NULL | 0 | 显示顺序 |
| leader | VARCHAR | 50 | NULL | - | 负责人 |
| phone | VARCHAR | 20 | NULL | - | 联系电话 |
| email | VARCHAR | 100 | NULL | - | 邮箱 |
| status | TINYINT | - | NOT NULL | 1 | 状态(0-禁用,1-启用) |
| remark | VARCHAR | 500 | NULL | - | 备注 |
| create_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 更新时间 |
| create_by | BIGINT | - | NULL | - | 创建者ID |
| update_by | BIGINT | - | NULL | - | 更新者ID |
| deleted | TINYINT | - | NOT NULL | 0 | 逻辑删除(0-未删除,1-已删除) |
| version | INT | - | NOT NULL | 1 | 版本号 |

**索引设计**:
- PRIMARY KEY (`id`)
- KEY `idx_sys_dept_parent_id` (`parent_id`)
- KEY `idx_sys_dept_status` (`status`)
- KEY `idx_sys_dept_order` (`order_num`)

---

## 📋 Stage 2 表结构设计（预览）

### 文件管理模块

#### file_info (文件信息表)
- 文件基本信息：文件名、大小、类型、路径等
- 文件元数据：MD5、创建时间、访问权限等

#### file_storage (文件存储表)
- 存储策略：本地存储、OSS存储、CDN存储
- 存储配置：存储路径、访问URL、存储参数等

#### file_relation (文件关联表)
- 文件与业务实体的关联关系
- 支持一对多、多对多关联

#### file_version (文件版本表)
- 文件版本控制
- 版本历史记录

### 日志管理模块

#### log_operation (操作日志表)
- 用户操作记录
- 操作类型、操作对象、操作结果等

#### log_login (登录日志表)
- 用户登录登出记录
- 登录IP、登录时间、登录状态等

#### log_error (错误日志表)
- 系统异常日志
- 错误类型、错误信息、堆栈信息等

---

## 🔧 数据库约束说明

### 外键约束
- sys_user.dept_id → sys_dept.id
- sys_user_role.user_id → sys_user.id
- sys_user_role.role_id → sys_role.id
- sys_role_permission.role_id → sys_role.id
- sys_role_permission.permission_id → sys_permission.id
- sys_permission.parent_id → sys_permission.id
- sys_menu.parent_id → sys_menu.id
- sys_dept.parent_id → sys_dept.id

### 唯一约束
- 用户名、邮箱、手机号全局唯一
- 角色编码、权限编码、字典类型全局唯一
- 用户角色关联、角色权限关联组合唯一

### 检查约束
- 状态字段值范围检查
- 性别字段值范围检查
- 权限类型字段值范围检查

---

**文档版本**: v1.0  
**创建时间**: 2025-01-07  
**维护人员**: SparkFan  
**更新记录**: 初始版本创建，完成Stage 1表结构设计 