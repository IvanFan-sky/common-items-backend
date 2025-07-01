# 数据库表结构设计文档

## 📋 表结构总览

### Stage 1: 核心基础功能表（9张）

| 表名 | 中文名称 | 功能描述 | 状态 |
|-----|---------|---------|------|
| sys_user | 用户表 | 用户基本信息管理 | ✅ |
| sys_role | 角色表 | 系统角色定义 | ✅ |
| sys_permission | 权限菜单表 | 系统权限和菜单统一管理 | ✅ |
| sys_user_role | 用户角色关联表 | 用户与角色的多对多关系 | ✅ |
| sys_role_permission | 角色权限关联表 | 角色与权限的多对多关系 | ✅ |
| sys_dict_type | 字典类型表 | 字典数据类型管理 | ✅ |
| sys_dict_data | 字典数据表 | 字典数据值管理 | ✅ |
| sys_config | 系统配置表 | 系统参数配置 | ✅ |
| sys_user_social | 社交登录表 | 第三方社交平台登录信息 | ✅ |

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
| data_scope | TINYINT | - | NOT NULL | 1 | 数据范围(1-全部,2-自定义,5-仅本人) |
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

### 3. sys_permission (权限菜单表)

**表说明**: 系统权限和菜单统一管理，支持目录、菜单、按钮和接口权限

| 字段名 | 数据类型 | 长度 | 是否为空 | 默认值 | 注释 |
|--------|----------|------|----------|---------|------|
| id | BIGINT | - | NOT NULL | - | 主键ID |
| parent_id | BIGINT | - | NOT NULL | 0 | 父权限ID |
| permission_name | VARCHAR | 50 | NOT NULL | - | 权限名称 |
| permission_code | VARCHAR | 100 | NOT NULL | - | 权限编码 |
| permission_type | TINYINT | - | NOT NULL | 1 | 权限类型(1-目录,2-菜单,3-按钮,4-接口) |
| path | VARCHAR | 200 | NULL | - | 路由路径 |
| component | VARCHAR | 200 | NULL | - | 组件路径 |
| icon | VARCHAR | 100 | NULL | - | 图标 |
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



### 9. sys_user_social (社交登录表)

**表说明**: 第三方社交平台登录信息管理，支持多平台绑定

| 字段名 | 数据类型 | 长度 | 是否为空 | 默认值 | 注释 |
|--------|----------|------|----------|---------|------|
| id | BIGINT | - | NOT NULL | - | 主键ID |
| user_id | BIGINT | - | NOT NULL | - | 用户ID |
| social_type | VARCHAR | 20 | NOT NULL | - | 社交平台类型(wechat,qq,weibo,alipay,github等) |
| social_id | VARCHAR | 100 | NOT NULL | - | 社交平台用户标识 |
| social_nickname | VARCHAR | 100 | NULL | - | 社交平台昵称 |
| social_avatar | VARCHAR | 200 | NULL | - | 社交平台头像 |
| social_email | VARCHAR | 100 | NULL | - | 社交平台邮箱 |
| social_phone | VARCHAR | 20 | NULL | - | 社交平台手机号 |
| social_gender | TINYINT | - | NULL | 0 | 社交平台性别(0-未知,1-男,2-女) |
| union_id | VARCHAR | 100 | NULL | - | 微信UnionID(用于多应用统一身份) |
| open_id | VARCHAR | 100 | NULL | - | 微信OpenID |
| access_token | VARCHAR | 500 | NULL | - | 访问令牌 |
| refresh_token | VARCHAR | 500 | NULL | - | 刷新令牌 |
| expires_in | BIGINT | - | NULL | - | 令牌过期时间戳 |
| scope | VARCHAR | 200 | NULL | - | 授权范围 |
| raw_user_info | TEXT | - | NULL | - | 原始用户信息JSON |
| bind_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 绑定时间 |
| last_login_time | DATETIME | - | NULL | - | 最后登录时间 |
| login_count | INT | - | NOT NULL | 0 | 登录次数 |
| status | TINYINT | - | NOT NULL | 1 | 状态(0-解绑,1-已绑定) |
| remark | VARCHAR | 500 | NULL | - | 备注 |
| create_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 更新时间 |
| create_by | BIGINT | - | NULL | - | 创建者ID |
| update_by | BIGINT | - | NULL | - | 更新者ID |
| deleted | TINYINT | - | NOT NULL | 0 | 逻辑删除(0-未删除,1-已删除) |
| version | INT | - | NOT NULL | 1 | 版本号 |

**索引设计**:
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_sys_user_social_platform` (`social_type`, `social_id`)
- UNIQUE KEY `uk_sys_user_social_union` (`social_type`, `union_id`)
- KEY `idx_sys_user_social_user_id` (`user_id`)
- KEY `idx_sys_user_social_type` (`social_type`)
- KEY `idx_sys_user_social_status` (`status`)
- KEY `idx_sys_user_social_bind_time` (`bind_time`)

**业务规则**:
- 同一社交平台的社交ID全局唯一
- 同一用户可绑定多个不同社交平台
- 微信UnionID在同一开放平台下唯一
- 令牌信息支持自动刷新机制
- 支持社交账号的绑定与解绑操作

**社交平台类型枚举**:
- `wechat`: 微信登录
- `qq`: QQ登录  
- `weibo`: 微博登录
- `alipay`: 支付宝登录
- `github`: GitHub登录
- `gitee`: Gitee登录
- `dingtalk`: 钉钉登录

---

## 📋 Stage 2 表结构详细设计

### Stage 2: 基本业务功能表（7张）

| 表名 | 中文名称 | 功能描述 | 状态 |
|-----|---------|---------|------|
| file_info | 文件信息表 | 文件基本信息和元数据管理 | ✅ |
| file_storage | 文件存储表 | 文件存储策略和配置管理 | ✅ |
| file_relation | 文件关联表 | 文件与业务实体关联关系 | ✅ |
| file_version | 文件版本表 | 文件版本控制和历史记录 | ✅ |
| log_operation | 操作日志表 | 用户操作行为记录 | ✅ |
| log_login | 登录日志表 | 用户登录登出记录 | ✅ |
| log_error | 错误日志表 | 系统异常和错误记录 | ✅ |

---

## 📊 Stage 2 表结构详细设计

### 文件管理模块 (file_*)

### 1. file_info (文件信息表)

**表说明**: 文件基本信息和元数据管理，支持多种文件类型

| 字段名 | 数据类型 | 长度 | 是否为空 | 默认值 | 注释 |
|--------|----------|------|----------|---------|------|
| id | BIGINT | - | NOT NULL | - | 主键ID |
| file_name | VARCHAR | 255 | NOT NULL | - | 原始文件名 |
| file_path | VARCHAR | 500 | NOT NULL | - | 文件存储路径 |
| file_url | VARCHAR | 500 | NULL | - | 文件访问URL |
| file_size | BIGINT | - | NOT NULL | 0 | 文件大小(字节) |
| file_type | VARCHAR | 50 | NOT NULL | - | 文件类型(image,document,video,audio,other) |
| file_extension | VARCHAR | 20 | NOT NULL | - | 文件扩展名 |
| mime_type | VARCHAR | 100 | NOT NULL | - | MIME类型 |
| file_md5 | VARCHAR | 32 | NOT NULL | - | 文件MD5值 |
| file_sha1 | VARCHAR | 40 | NULL | - | 文件SHA1值 |
| storage_type | VARCHAR | 20 | NOT NULL | 'local' | 存储类型(local,oss,cos,qiniu,minio) |
| storage_id | BIGINT | - | NULL | - | 存储配置ID |
| bucket_name | VARCHAR | 100 | NULL | - | 存储桶名称 |
| object_key | VARCHAR | 500 | NULL | - | 对象存储Key |
| upload_ip | VARCHAR | 50 | NULL | - | 上传IP地址 |
| upload_user_agent | VARCHAR | 500 | NULL | - | 上传用户代理 |
| thumbnail_path | VARCHAR | 500 | NULL | - | 缩略图路径 |
| width | INT | - | NULL | - | 图片宽度(像素) |
| height | INT | - | NULL | - | 图片高度(像素) |
| duration | INT | - | NULL | - | 音视频时长(秒) |
| download_count | INT | - | NOT NULL | 0 | 下载次数 |
| view_count | INT | - | NOT NULL | 0 | 查看次数 |
| is_public | TINYINT | - | NOT NULL | 0 | 是否公开(0-私有,1-公开) |
| access_level | TINYINT | - | NOT NULL | 1 | 访问级别(1-所有人,2-登录用户,3-指定用户) |
| expire_time | DATETIME | - | NULL | - | 过期时间 |
| status | TINYINT | - | NOT NULL | 1 | 状态(0-禁用,1-正常,2-处理中,3-失败) |
| remark | VARCHAR | 500 | NULL | - | 备注 |
| create_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 更新时间 |
| create_by | BIGINT | - | NULL | - | 创建者ID |
| update_by | BIGINT | - | NULL | - | 更新者ID |
| deleted | TINYINT | - | NOT NULL | 0 | 逻辑删除(0-未删除,1-已删除) |
| version | INT | - | NOT NULL | 1 | 版本号 |

**索引设计**:
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_file_info_md5` (`file_md5`)
- KEY `idx_file_info_type` (`file_type`)
- KEY `idx_file_info_storage` (`storage_type`)
- KEY `idx_file_info_create_by` (`create_by`)
- KEY `idx_file_info_create_time` (`create_time`)
- KEY `idx_file_info_status` (`status`)
- KEY `idx_file_info_expire` (`expire_time`)

### 2. file_storage (文件存储表)

**表说明**: 文件存储策略和配置管理，支持多种存储方式

| 字段名 | 数据类型 | 长度 | 是否为空 | 默认值 | 注释 |
|--------|----------|------|----------|---------|------|
| id | BIGINT | - | NOT NULL | - | 主键ID |
| storage_name | VARCHAR | 100 | NOT NULL | - | 存储名称 |
| storage_code | VARCHAR | 50 | NOT NULL | - | 存储编码 |
| storage_type | VARCHAR | 20 | NOT NULL | - | 存储类型(local,oss,cos,qiniu,minio) |
| access_key | VARCHAR | 200 | NULL | - | 访问密钥 |
| secret_key | VARCHAR | 200 | NULL | - | 私有密钥 |
| endpoint | VARCHAR | 200 | NULL | - | 服务端点 |
| region | VARCHAR | 50 | NULL | - | 存储区域 |
| bucket_name | VARCHAR | 100 | NULL | - | 存储桶名称 |
| domain | VARCHAR | 200 | NULL | - | 自定义域名 |
| base_path | VARCHAR | 200 | NULL | '/' | 基础路径 |
| path_style | TINYINT | - | NOT NULL | 0 | 路径样式(0-虚拟主机,1-路径) |
| is_https | TINYINT | - | NOT NULL | 1 | 是否HTTPS(0-HTTP,1-HTTPS) |
| is_default | TINYINT | - | NOT NULL | 0 | 是否默认(0-否,1-是) |
| max_file_size | BIGINT | - | NOT NULL | 52428800 | 最大文件大小(字节,默认50MB) |
| allowed_types | VARCHAR | 500 | NULL | - | 允许的文件类型(逗号分隔) |
| sort_order | INT | - | NOT NULL | 0 | 排序序号 |
| config_json | TEXT | - | NULL | - | 扩展配置JSON |
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
- UNIQUE KEY `uk_file_storage_code` (`storage_code`)
- KEY `idx_file_storage_type` (`storage_type`)
- KEY `idx_file_storage_default` (`is_default`)
- KEY `idx_file_storage_status` (`status`)
- KEY `idx_file_storage_sort` (`sort_order`)

### 3. file_relation (文件关联表)

**表说明**: 文件与业务实体关联关系，支持多对多关联

| 字段名 | 数据类型 | 长度 | 是否为空 | 默认值 | 注释 |
|--------|----------|------|----------|---------|------|
| id | BIGINT | - | NOT NULL | - | 主键ID |
| file_id | BIGINT | - | NOT NULL | - | 文件ID |
| relation_type | VARCHAR | 50 | NOT NULL | - | 关联类型(user_avatar,post_image,document等) |
| relation_id | BIGINT | - | NOT NULL | - | 关联实体ID |
| relation_field | VARCHAR | 50 | NULL | - | 关联字段名 |
| sort_order | INT | - | NOT NULL | 0 | 排序序号 |
| is_main | TINYINT | - | NOT NULL | 0 | 是否主要文件(0-否,1-是) |
| usage_type | VARCHAR | 30 | NOT NULL | 'normal' | 使用类型(normal,thumbnail,preview) |
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
- UNIQUE KEY `uk_file_relation` (`file_id`, `relation_type`, `relation_id`, `usage_type`)
- KEY `idx_file_relation_file` (`file_id`)
- KEY `idx_file_relation_entity` (`relation_type`, `relation_id`)
- KEY `idx_file_relation_main` (`is_main`)
- KEY `idx_file_relation_sort` (`sort_order`)

### 4. file_version (文件版本表)

**表说明**: 文件版本控制和历史记录，支持文件版本管理

| 字段名 | 数据类型 | 长度 | 是否为空 | 默认值 | 注释 |
|--------|----------|------|----------|---------|------|
| id | BIGINT | - | NOT NULL | - | 主键ID |
| file_id | BIGINT | - | NOT NULL | - | 文件ID |
| version_num | VARCHAR | 20 | NOT NULL | - | 版本号(如v1.0, v1.1) |
| version_name | VARCHAR | 100 | NULL | - | 版本名称 |
| file_name | VARCHAR | 255 | NOT NULL | - | 文件名 |
| file_path | VARCHAR | 500 | NOT NULL | - | 文件路径 |
| file_size | BIGINT | - | NOT NULL | 0 | 文件大小 |
| file_md5 | VARCHAR | 32 | NOT NULL | - | 文件MD5值 |
| change_type | VARCHAR | 20 | NOT NULL | 'update' | 变更类型(create,update,replace,delete) |
| change_log | TEXT | - | NULL | - | 变更日志 |
| is_current | TINYINT | - | NOT NULL | 0 | 是否当前版本(0-否,1-是) |
| download_count | INT | - | NOT NULL | 0 | 下载次数 |
| status | TINYINT | - | NOT NULL | 1 | 状态(0-禁用,1-启用,2-已删除) |
| remark | VARCHAR | 500 | NULL | - | 备注 |
| create_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 更新时间 |
| create_by | BIGINT | - | NULL | - | 创建者ID |
| update_by | BIGINT | - | NULL | - | 更新者ID |
| deleted | TINYINT | - | NOT NULL | 0 | 逻辑删除(0-未删除,1-已删除) |
| version | INT | - | NOT NULL | 1 | 版本号 |

**索引设计**:
- PRIMARY KEY (`id`)
- UNIQUE KEY `uk_file_version` (`file_id`, `version_num`)
- KEY `idx_file_version_file` (`file_id`)
- KEY `idx_file_version_current` (`is_current`)
- KEY `idx_file_version_create_time` (`create_time`)
- KEY `idx_file_version_status` (`status`)

### 日志管理模块 (log_*)

### 5. log_operation (操作日志表)

**表说明**: 用户操作行为记录，支持操作审计和追踪

| 字段名 | 数据类型 | 长度 | 是否为空 | 默认值 | 注释 |
|--------|----------|------|----------|---------|------|
| id | BIGINT | - | NOT NULL | - | 主键ID |
| trace_id | VARCHAR | 32 | NULL | - | 请求追踪ID |
| user_id | BIGINT | - | NULL | - | 操作用户ID |
| username | VARCHAR | 50 | NULL | - | 用户名 |
| operation_name | VARCHAR | 100 | NOT NULL | - | 操作名称 |
| operation_type | VARCHAR | 20 | NOT NULL | - | 操作类型(CREATE,UPDATE,DELETE,QUERY,LOGIN,LOGOUT,EXPORT,IMPORT) |
| business_type | VARCHAR | 50 | NULL | - | 业务类型(USER,ROLE,PERMISSION,FILE等) |
| method | VARCHAR | 10 | NOT NULL | - | 请求方法(GET,POST,PUT,DELETE) |
| request_url | VARCHAR | 500 | NOT NULL | - | 请求URL |
| request_ip | VARCHAR | 50 | NOT NULL | - | 请求IP |
| request_location | VARCHAR | 100 | NULL | - | 请求地址 |
| request_params | TEXT | - | NULL | - | 请求参数 |
| request_body | TEXT | - | NULL | - | 请求体 |
| response_result | TEXT | - | NULL | - | 响应结果 |
| response_status | INT | - | NULL | - | 响应状态码 |
| error_msg | TEXT | - | NULL | - | 错误信息 |
| execute_time | BIGINT | - | NULL | - | 执行时间(毫秒) |
| user_agent | VARCHAR | 500 | NULL | - | 用户代理 |
| browser_type | VARCHAR | 50 | NULL | - | 浏览器类型 |
| os_type | VARCHAR | 50 | NULL | - | 操作系统 |
| device_type | VARCHAR | 20 | NULL | - | 设备类型(PC,MOBILE,TABLET) |
| module_name | VARCHAR | 50 | NULL | - | 模块名称 |
| class_name | VARCHAR | 200 | NULL | - | 类名 |
| method_name | VARCHAR | 100 | NULL | - | 方法名 |
| operation_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 操作时间 |
| status | TINYINT | - | NOT NULL | 1 | 状态(0-失败,1-成功,2-异常) |
| create_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |

**索引设计**:
- PRIMARY KEY (`id`)
- KEY `idx_log_operation_user` (`user_id`)
- KEY `idx_log_operation_type` (`operation_type`)
- KEY `idx_log_operation_time` (`operation_time`)
- KEY `idx_log_operation_ip` (`request_ip`)
- KEY `idx_log_operation_status` (`status`)
- KEY `idx_log_operation_trace` (`trace_id`)

### 6. log_login (登录日志表)

**表说明**: 用户登录登出记录，支持安全审计和异常检测

| 字段名 | 数据类型 | 长度 | 是否为空 | 默认值 | 注释 |
|--------|----------|------|----------|---------|------|
| id | BIGINT | - | NOT NULL | - | 主键ID |
| session_id | VARCHAR | 100 | NULL | - | 会话ID |
| user_id | BIGINT | - | NULL | - | 用户ID |
| username | VARCHAR | 50 | NOT NULL | - | 用户名 |
| login_type | VARCHAR | 20 | NOT NULL | 'password' | 登录类型(password,wechat,qq,sms,email) |
| login_method | VARCHAR | 20 | NOT NULL | 'web' | 登录方式(web,app,api) |
| login_ip | VARCHAR | 50 | NOT NULL | - | 登录IP |
| login_location | VARCHAR | 100 | NULL | - | 登录地点 |
| user_agent | VARCHAR | 500 | NULL | - | 用户代理 |
| browser_type | VARCHAR | 50 | NULL | - | 浏览器类型 |
| os_type | VARCHAR | 50 | NULL | - | 操作系统 |
| device_type | VARCHAR | 20 | NULL | - | 设备类型(PC,MOBILE,TABLET) |
| device_id | VARCHAR | 100 | NULL | - | 设备标识 |
| login_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 登录时间 |
| logout_time | DATETIME | - | NULL | - | 登出时间 |
| online_duration | BIGINT | - | NULL | - | 在线时长(秒) |
| login_result | TINYINT | - | NOT NULL | 1 | 登录结果(0-失败,1-成功) |
| fail_reason | VARCHAR | 200 | NULL | - | 失败原因 |
| token | VARCHAR | 500 | NULL | - | 登录令牌(脱敏) |
| refresh_count | INT | - | NOT NULL | 0 | 令牌刷新次数 |
| is_forced_logout | TINYINT | - | NOT NULL | 0 | 是否强制登出(0-否,1-是) |
| risk_level | TINYINT | - | NOT NULL | 0 | 风险级别(0-正常,1-低风险,2-中风险,3-高风险) |
| remark | VARCHAR | 500 | NULL | - | 备注 |
| create_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 更新时间 |

**索引设计**:
- PRIMARY KEY (`id`)
- KEY `idx_log_login_user` (`user_id`)
- KEY `idx_log_login_username` (`username`)
- KEY `idx_log_login_ip` (`login_ip`)
- KEY `idx_log_login_time` (`login_time`)
- KEY `idx_log_login_result` (`login_result`)
- KEY `idx_log_login_risk` (`risk_level`)
- KEY `idx_log_login_session` (`session_id`)

### 7. log_error (错误日志表)

**表说明**: 系统异常和错误记录，支持问题定位和系统监控

| 字段名 | 数据类型 | 长度 | 是否为空 | 默认值 | 注释 |
|--------|----------|------|----------|---------|------|
| id | BIGINT | - | NOT NULL | - | 主键ID |
| trace_id | VARCHAR | 32 | NULL | - | 请求追踪ID |
| error_id | VARCHAR | 50 | NULL | - | 错误ID |
| error_type | VARCHAR | 50 | NOT NULL | - | 错误类型(SYSTEM,BUSINESS,NETWORK,DATABASE,THIRD_PARTY) |
| error_level | VARCHAR | 20 | NOT NULL | 'ERROR' | 错误级别(DEBUG,INFO,WARN,ERROR,FATAL) |
| error_code | VARCHAR | 50 | NULL | - | 错误代码 |
| error_message | TEXT | - | NOT NULL | - | 错误信息 |
| exception_class | VARCHAR | 200 | NULL | - | 异常类名 |
| stack_trace | LONGTEXT | - | NULL | - | 堆栈信息 |
| user_id | BIGINT | - | NULL | - | 用户ID |
| username | VARCHAR | 50 | NULL | - | 用户名 |
| request_method | VARCHAR | 10 | NULL | - | 请求方法 |
| request_url | VARCHAR | 500 | NULL | - | 请求URL |
| request_params | TEXT | - | NULL | - | 请求参数 |
| request_ip | VARCHAR | 50 | NULL | - | 请求IP |
| user_agent | VARCHAR | 500 | NULL | - | 用户代理 |
| module_name | VARCHAR | 50 | NULL | - | 模块名称 |
| class_name | VARCHAR | 200 | NULL | - | 类名 |
| method_name | VARCHAR | 100 | NULL | - | 方法名 |
| line_number | INT | - | NULL | - | 错误行号 |
| server_name | VARCHAR | 100 | NULL | - | 服务器名称 |
| server_ip | VARCHAR | 50 | NULL | - | 服务器IP |
| thread_name | VARCHAR | 100 | NULL | - | 线程名称 |
| error_count | INT | - | NOT NULL | 1 | 错误次数 |
| first_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 首次发生时间 |
| last_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 最后发生时间 |
| is_resolved | TINYINT | - | NOT NULL | 0 | 是否已解决(0-未解决,1-已解决) |
| resolve_time | DATETIME | - | NULL | - | 解决时间 |
| resolve_by | BIGINT | - | NULL | - | 解决人ID |
| resolve_note | TEXT | - | NULL | - | 解决说明 |
| notify_status | TINYINT | - | NOT NULL | 0 | 通知状态(0-未通知,1-已通知,2-通知失败) |
| status | TINYINT | - | NOT NULL | 1 | 状态(0-忽略,1-正常,2-关注) |
| create_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 创建时间 |
| update_time | DATETIME | - | NOT NULL | CURRENT_TIMESTAMP | 更新时间 |

**索引设计**:
- PRIMARY KEY (`id`)
- KEY `idx_log_error_type` (`error_type`)
- KEY `idx_log_error_level` (`error_level`)
- KEY `idx_log_error_code` (`error_code`)
- KEY `idx_log_error_user` (`user_id`)
- KEY `idx_log_error_time` (`create_time`)
- KEY `idx_log_error_resolved` (`is_resolved`)
- KEY `idx_log_error_trace` (`trace_id`)
- KEY `idx_log_error_count` (`error_count`)
- KEY `idx_log_error_first_time` (`first_time`)

---

## 🔧 数据库约束说明

### 外键约束

#### Stage 1 外键约束
- sys_user_role.user_id → sys_user.id
- sys_user_role.role_id → sys_role.id
- sys_role_permission.role_id → sys_role.id
- sys_role_permission.permission_id → sys_permission.id
- sys_permission.parent_id → sys_permission.id
- sys_user_social.user_id → sys_user.id

#### Stage 2 外键约束
- file_info.storage_id → file_storage.id
- file_relation.file_id → file_info.id
- file_version.file_id → file_info.id
- log_operation.user_id → sys_user.id
- log_login.user_id → sys_user.id
- log_error.user_id → sys_user.id
- log_error.resolve_by → sys_user.id

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
**更新记录**: 初始版本创建，完成Stage 1、Stage 2表结构设计 