# 通用管理系统后端项目

## 项目简介

基于 Java 17 + Spring Boot 3.2.1 + MyBatis Plus 的企业级后端管理系统。

## 技术栈

- **Java 17** - 编程语言
- **Spring Boot 3.2.1** - 应用框架
- **MyBatis Plus 3.5.5** - 数据持久化
- **MySQL 8.0** - 数据库
- **Redis 7.0** - 缓存中间件
- **Sa-Token** - 权限认证
- **Knife4j** - API文档
- **Lombok** - 代码简化
- **Hutool** - 工具类库
- **MapStruct** - 对象映射

## 项目结构

```
backend/
├── .cursor/                    # Cursor IDE 规范配置
│   └── rule/
│       ├── java-backend.mdc   # Java开发规范
│       └── git-commit.mdc     # Git提交规范
├── common-admin-parent/        # 主项目
│   ├── common-admin-core/     # 核心模块
│   ├── common-admin-service/  # 业务逻辑模块
│   └── common-admin-web/      # Web控制器模块
├── .gitignore                 # Git忽略文件
└── README.md                  # 项目说明文档
```

## 开发环境要求

- **JDK**: OpenJDK 17+ 或 Oracle JDK 17+
- **Maven**: 3.9+
- **MySQL**: 8.0+
- **Redis**: 7.0+
- **IDE**: IntelliJ IDEA 2023.1+ 或 Cursor

## Git 使用规范

### 提交信息格式

```
<type>(<scope>): <subject>
```

### 提交类型

- `feat`: 新功能
- `fix`: 修复bug
- `docs`: 文档变更
- `style`: 代码格式化
- `refactor`: 重构代码
- `test`: 测试相关
- `chore`: 构建过程或辅助工具的变动

### 示例

```bash
feat(user): 添加用户注册功能
fix(login): 修复登录验证失败的问题
docs(api): 更新API文档
chore(deps): 升级Spring Boot版本到3.2.1
```

## 开发规范

项目遵循严格的开发规范，详细内容请参考：

- [Java开发规范](.cursor/rule/java-backend.mdc)
- [Git提交规范](.cursor/rule/git-commit.mdc)

## 快速开始

### 1. 克隆项目

```bash
git clone <repository-url>
cd backend
```

### 2. 环境配置

```bash
# 安装依赖
mvn clean install

# 配置数据库连接
# 修改 application.yml 中的数据库配置
```

### 3. 启动项目

```bash
mvn spring-boot:run
```

### 4. 访问项目

- 应用地址: http://localhost:8080
- API文档: http://localhost:8080/doc.html

## 代码提交流程

1. **创建功能分支**
   ```bash
   git checkout -b feature/user-management
   ```

2. **开发功能**
   - 遵循Java开发规范
   - 编写单元测试
   - 更新相关文档

3. **提交代码**
   ```bash
   git add .
   git commit -m "feat(user): 添加用户管理功能"
   ```

4. **推送到远程**
   ```bash
   git push origin feature/user-management
   ```

5. **创建Pull Request**
   - 代码审查
   - 测试通过
   - 合并到主分支

## 项目维护

### 依赖更新

```bash
# 检查过期依赖
mvn versions:display-dependency-updates

# 更新依赖版本
mvn versions:use-latest-releases
```

### 代码质量检查

```bash
# 运行测试
mvn test

# 代码覆盖率
mvn jacoco:report
```

## 联系方式

如有问题请联系项目维护者。

---

**注意**: 本项目使用严格的代码规范和提交规范，请务必遵循相关约定。 