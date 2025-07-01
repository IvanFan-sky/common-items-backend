# 数据库使用指南

## 📋 快速开始

### 1. 环境要求
- MySQL 8.0 或更高版本
- 建议内存：至少4GB
- 建议磁盘空间：至少20GB

### 2. 数据库初始化步骤

#### 步骤1：执行DDL脚本
```bash
# 使用MySQL命令行工具执行
mysql -u root -p < init-schema.sql

# 或者使用图形化工具（如Navicat、MySQL Workbench）导入并执行
```

#### 步骤2：执行DML脚本
```bash
# 插入初始化数据
mysql -u root -p < init-data.sql
```

#### 步骤3：验证初始化结果
```sql
-- 检查数据库和表是否创建成功
USE common_items_db;
SHOW TABLES;

-- 检查用户数据是否插入成功
SELECT * FROM sys_user;

-- 检查角色权限关联是否正确
SELECT u.username, r.role_name 
FROM sys_user u 
JOIN sys_user_role ur ON u.id = ur.user_id 
JOIN sys_role r ON ur.role_id = r.id;
```

## 🔐 默认账号信息

### 管理员账号
- **用户名**: admin
- **密码**: 123456
- **角色**: 超级管理员
- **权限**: 系统所有权限

### 开发者账号
- **用户名**: sparkfan
- **密码**: 123456
- **角色**: 开发人员
- **权限**: 系统查询权限

### 测试账号
- **用户名**: testuser
- **密码**: 123456
- **角色**: 普通用户
- **权限**: 基础查看权限

> ⚠️ **安全提醒**: 生产环境部署时请务必修改默认密码！

## 🛠️ 数据库连接配置

### Spring Boot配置示例
```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/common_items_db?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
    username: root
    password: your_password
    
  # 连接池配置
  hikari:
    minimum-idle: 5
    maximum-pool-size: 20
    auto-commit: true
    idle-timeout: 30000
    pool-name: SpringBootJPAHikariCP
    max-lifetime: 1800000
    connection-timeout: 30000
    connection-test-query: SELECT 1
```

### Docker Compose配置示例
```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    container_name: common-items-mysql
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: your_root_password
      MYSQL_DATABASE: common_items_db
      MYSQL_USER: app_user
      MYSQL_PASSWORD: app_password
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
      - ./docs/database/init-schema.sql:/docker-entrypoint-initdb.d/01-schema.sql
      - ./docs/database/init-data.sql:/docker-entrypoint-initdb.d/02-data.sql
    command: --default-authentication-plugin=mysql_native_password

volumes:
  mysql_data:
```

## 📊 数据库性能优化

### 1. 索引优化建议
```sql
-- 检查索引使用情况
SHOW INDEX FROM sys_user;

-- 分析慢查询
SHOW VARIABLES LIKE 'slow_query_log';
SET GLOBAL slow_query_log = 'ON';
SET GLOBAL long_query_time = 2;

-- 查看索引统计信息
SELECT 
    TABLE_NAME,
    INDEX_NAME,
    CARDINALITY,
    SUB_PART,
    PACKED,
    NULLABLE,
    INDEX_TYPE
FROM INFORMATION_SCHEMA.STATISTICS 
WHERE TABLE_SCHEMA = 'common_items_db';
```

### 2. 常用优化SQL
```sql
-- 优化用户查询（按部门）
EXPLAIN SELECT * FROM sys_user WHERE dept_id = 1 AND status = 1;

-- 优化权限查询
EXPLAIN SELECT p.* 
FROM sys_permission p
JOIN sys_role_permission rp ON p.id = rp.permission_id
JOIN sys_user_role ur ON rp.role_id = ur.role_id
WHERE ur.user_id = 1 AND p.status = 1;

-- 查看表大小
SELECT 
    table_name AS "表名",
    round(((data_length + index_length) / 1024 / 1024), 2) AS "大小(MB)"
FROM information_schema.tables
WHERE table_schema = "common_items_db"
ORDER BY (data_length + index_length) DESC;
```

## 🔄 数据库维护

### 1. 备份策略
```bash
# 全库备份
mysqldump -u root -p --single-transaction --routines --triggers common_items_db > backup_$(date +%Y%m%d_%H%M%S).sql

# 仅结构备份
mysqldump -u root -p --no-data common_items_db > schema_backup.sql

# 仅数据备份
mysqldump -u root -p --no-create-info common_items_db > data_backup.sql

# 压缩备份
mysqldump -u root -p common_items_db | gzip > backup_$(date +%Y%m%d_%H%M%S).sql.gz
```

### 2. 数据恢复
```bash
# 从备份文件恢复
mysql -u root -p common_items_db < backup_20250107_120000.sql

# 从压缩备份恢复
gunzip < backup_20250107_120000.sql.gz | mysql -u root -p common_items_db
```

### 3. 定期维护任务
```sql
-- 分析表
ANALYZE TABLE sys_user, sys_role, sys_permission;

-- 优化表
OPTIMIZE TABLE sys_user, sys_role, sys_permission;

-- 检查表
CHECK TABLE sys_user, sys_role, sys_permission;

-- 修复表（如有必要）
REPAIR TABLE table_name;
```

## 📈 监控与告警

### 1. 性能监控SQL
```sql
-- 查看连接数
SHOW STATUS LIKE 'Threads_connected';
SHOW STATUS LIKE 'Max_used_connections';

-- 查看慢查询数量
SHOW STATUS LIKE 'Slow_queries';

-- 查看查询缓存命中率
SHOW STATUS LIKE 'Qcache_hits';
SHOW STATUS LIKE 'Qcache_inserts';

-- 查看表锁定情况
SHOW STATUS LIKE 'Table_locks_waited';
SHOW STATUS LIKE 'Table_locks_immediate';

-- 查看InnoDB状态
SHOW ENGINE INNODB STATUS;
```

### 2. 磁盘空间监控
```sql
-- 查看数据库大小
SELECT 
    table_schema AS '数据库',
    ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS '大小(MB)'
FROM information_schema.tables
WHERE table_schema = 'common_items_db'
GROUP BY table_schema;

-- 查看各表大小
SELECT 
    table_name AS '表名',
    ROUND(((data_length + index_length) / 1024 / 1024), 2) AS '大小(MB)',
    table_rows AS '记录数'
FROM information_schema.tables
WHERE table_schema = 'common_items_db'
ORDER BY (data_length + index_length) DESC;
```

## 🔒 安全配置

### 1. 用户权限管理
```sql
-- 创建应用专用用户
CREATE USER 'app_user'@'%' IDENTIFIED BY 'strong_password';

-- 授予必要权限
GRANT SELECT, INSERT, UPDATE, DELETE ON common_items_db.* TO 'app_user'@'%';

-- 刷新权限
FLUSH PRIVILEGES;

-- 查看用户权限
SHOW GRANTS FOR 'app_user'@'%';
```

### 2. 安全配置建议
```ini
# my.cnf 安全配置
[mysqld]
# 绑定IP地址
bind-address = 127.0.0.1

# 禁用远程root登录
skip-networking

# 设置最大连接数
max_connections = 200

# 设置连接超时
wait_timeout = 28800
interactive_timeout = 28800

# 启用查询日志
general_log = 1
general_log_file = /var/log/mysql/mysql.log

# 启用慢查询日志
slow_query_log = 1
slow_query_log_file = /var/log/mysql/mysql-slow.log
long_query_time = 2
```

## 🚀 版本升级指南

### 1. 版本管理
```sql
-- 创建版本记录表
CREATE TABLE `sys_db_version` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `version` VARCHAR(20) NOT NULL COMMENT '版本号',
    `description` VARCHAR(500) NULL COMMENT '版本描述',
    `sql_file` VARCHAR(200) NULL COMMENT 'SQL文件名',
    `install_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '安装时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_version` (`version`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据库版本记录表';

-- 插入当前版本
INSERT INTO sys_db_version (version, description, sql_file) 
VALUES ('1.0.0', 'Stage 1 核心基础功能', 'init-schema.sql,init-data.sql');
```

### 2. 升级脚本示例
```sql
-- upgrade_1.1.0.sql
-- 升级到1.1.0版本

-- 检查当前版本
SELECT version FROM sys_db_version ORDER BY install_date DESC LIMIT 1;

-- 执行升级操作
-- ... 具体的DDL和DML语句 ...

-- 记录新版本
INSERT INTO sys_db_version (version, description, sql_file) 
VALUES ('1.1.0', '新增文件管理模块', 'upgrade_1.1.0.sql');
```

## 📝 常见问题解决

### 1. 连接问题
```bash
# 检查MySQL服务状态
sudo systemctl status mysql

# 检查端口占用
netstat -an | grep 3306

# 检查MySQL进程
ps aux | grep mysql
```

### 2. 权限问题
```sql
-- 检查用户是否存在
SELECT User, Host FROM mysql.user WHERE User = 'app_user';

-- 检查用户权限
SHOW GRANTS FOR 'app_user'@'%';

-- 重置密码
ALTER USER 'app_user'@'%' IDENTIFIED BY 'new_password';
```

### 3. 性能问题
```sql
-- 查看正在执行的查询
SHOW PROCESSLIST;

-- 杀死慢查询
KILL QUERY process_id;

-- 查看锁等待情况
SELECT * FROM information_schema.INNODB_LOCKS;
SELECT * FROM information_schema.INNODB_LOCK_WAITS;
```

---

## 📞 技术支持

如遇到问题，请按以下步骤排查：

1. **检查日志**: 查看MySQL错误日志
2. **检查配置**: 验证数据库连接配置
3. **检查权限**: 确认用户权限设置
4. **检查版本**: 确认MySQL版本兼容性
5. **联系开发团队**: 提供详细错误信息

**文档版本**: v1.0  
**创建时间**: 2025-01-07  
**维护人员**: SparkFan  
**更新记录**: 初始版本创建 