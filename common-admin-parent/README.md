# 通用管理系统后端项目

## 🚀 项目介绍

基于Spring Boot 3的通用管理系统后端，采用Maven多模块架构，集成了现代化的技术栈，为各种业务场景提供基础支撑。

## 📋 技术栈

- **Java 17** - 基础开发语言
- **Spring Boot 3.2.1** - 主框架
- **MyBatis Plus 3.5.5** - ORM框架
- **MySQL 8.0** - 关系型数据库
- **Redis 7** - 缓存数据库
- **Sa-Token** - 权限认证框架
- **Knife4j** - 接口文档工具
- **Druid** - 数据库连接池
- **Lombok** - 代码简化工具
- **MapStruct** - 对象映射工具
- **Hutool** - Java工具类库

## 🏗️ 项目结构

```
common-admin-parent/
├── common-admin-core/          # 核心模块
│   ├── entity/                 # 实体类
│   ├── dto/                    # 数据传输对象
│   ├── vo/                     # 视图对象
│   ├── enums/                  # 枚举类
│   ├── constant/               # 常量类
│   ├── exception/              # 异常类
│   └── util/                   # 工具类
├── common-admin-service/       # 服务模块
│   ├── service/                # 业务服务接口
│   ├── service/impl/           # 业务服务实现
│   ├── mapper/                 # 数据访问接口
│   └── resources/mapper/       # MyBatis映射文件
├── common-admin-web/           # Web模块
│   ├── controller/             # 控制器
│   ├── config/                 # 配置类
│   ├── interceptor/            # 拦截器
│   ├── aspect/                 # 切面类
│   └── CommonAdminApplication.java # 启动类
└── pom.xml                     # 父项目配置
```

## ✨ 已完成功能

### 🔧 Stage 1.1 项目架构搭建 (已完成)

- [x] **Maven多模块项目初始化**
  - ✅ 父项目pom.xml配置
  - ✅ core模块基础结构
  - ✅ service模块基础结构
  - ✅ web模块基础结构

- [x] **基础配置完成**
  - ✅ application.yml主配置文件
  - ✅ application-dev.yml开发环境配置
  - ✅ 数据源配置 (Druid)
  - ✅ Redis配置
  - ✅ MyBatis Plus配置
  - ✅ Sa-Token配置
  - ✅ Knife4j配置

- [x] **核心基础类创建**
  - ✅ BaseEntity - 基础实体类
  - ✅ Result - 统一响应结果类
  - ✅ PageResult - 分页响应结果类
  - ✅ BaseQuery - 基础查询对象
  - ✅ CommonConstants - 通用常量
  - ✅ BusinessException - 业务异常类

- [x] **启动类配置**
  - ✅ CommonAdminApplication - Spring Boot启动类
  - ✅ 包扫描配置
  - ✅ 异步任务启用
  - ✅ 定时任务启用

## 🎯 下一步计划

### Stage 1.2 数据库设计 (计划3天)
- [ ] 核心表结构设计
- [ ] 数据库初始化脚本
- [ ] 基础数据插入脚本
- [ ] 数据库版本控制

### Stage 1.3 用户认证核心功能 (计划7天)
- [ ] 用户实体和DTO设计
- [ ] 用户注册功能
- [ ] 用户登录功能
- [ ] 基础权限控制

## 🚦 快速开始

### 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Redis 7+

### 数据库准备

1. 创建数据库
```sql
CREATE DATABASE common_admin_dev DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改配置文件中的数据库连接信息

### 启动项目

1. 克隆项目到本地
2. 导入到IDE中
3. 确保MySQL和Redis服务已启动
4. 运行`CommonAdminApplication`启动类

### 访问地址

- 项目地址: http://localhost:8080/api
- 接口文档: http://localhost:8080/api/doc.html
- Druid监控: http://localhost:8080/api/druid

### 默认账号

- Druid监控账号: admin / 123456

## 📚 开发规范

### 代码规范
- 遵循阿里巴巴Java开发手册
- 使用Lombok减少样板代码
- 使用MapStruct进行对象映射
- 统一异常处理和响应格式

### 接口规范
- RESTful API设计
- 统一响应格式 (Result)
- 完整的Swagger文档
- 请求参数校验

### 数据库规范
- 表命名使用下划线命名法
- 字段命名规范统一
- 必要的索引设计
- 逻辑删除机制

## 🔧 配置说明

### 核心配置项

```yaml
# 数据库配置
spring.datasource.druid.url: 数据库连接地址
spring.datasource.druid.username: 数据库用户名
spring.datasource.druid.password: 数据库密码

# Redis配置
spring.data.redis.host: Redis服务器地址
spring.data.redis.port: Redis端口
spring.data.redis.password: Redis密码

# Sa-Token配置
sa-token.token-name: token名称
sa-token.timeout: token有效期
```

## 🤝 贡献指南

1. Fork 本仓库
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📄 许可证

本项目采用 [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0.html) 许可证

## 📞 联系方式

- 项目地址: https://github.com/common-admin/backend
- 问题反馈: https://github.com/common-admin/backend/issues
- 邮箱: admin@common.com

---

⭐ 如果这个项目对你有帮助，请给个 Star 支持一下！