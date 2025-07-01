# 🚀 GitHub仓库配置和CI/CD部署指南

## 📋 概述

本文档描述了如何配置GitHub仓库以启用完整的CI/CD流程，包括环境配置、密钥管理和自动化部署。

## 🔐 GitHub仓库配置

### 1. 分支保护规则配置

进入 `Settings > Branches` 添加分支保护规则：

#### Main分支保护规则
```yaml
分支名称: main
保护规则:
  ✅ Restrict pushes that create files larger than 100MB
  ✅ Require a pull request before merging
    ✅ Require approvals (1个审批)
    ✅ Dismiss stale reviews when new commits are pushed
    ✅ Require review from code owners
  ✅ Require status checks to pass before merging
    ✅ Require branches to be up to date before merging
    状态检查: CI/CD Pipeline / Code Quality Check
    状态检查: CI/CD Pipeline / Build and Test
    状态检查: CI/CD Pipeline / Security Scan
  ✅ Require conversation resolution before merging
  ✅ Restrict pushes that create files larger than 100MB
  ✅ Do not allow bypassing the above settings
```

#### Develop分支保护规则
```yaml
分支名称: develop
保护规则:
  ✅ Require status checks to pass before merging
    状态检查: CI/CD Pipeline / Code Quality Check
    状态检查: CI/CD Pipeline / Build and Test
```

### 2. Repository Secrets配置

进入 `Settings > Secrets and variables > Actions` 添加以下密钥：

#### 必需的Secrets
```bash
# 数据库配置
DB_PASSWORD_PROD=your_production_db_password
DB_PASSWORD_DEV=your_development_db_password

# Redis配置
REDIS_PASSWORD_PROD=your_production_redis_password
REDIS_PASSWORD_DEV=your_development_redis_password

# 部署相关
DEPLOY_SERVER_HOST=your_server_ip
DEPLOY_SERVER_USER=deploy_user
DEPLOY_SSH_KEY=your_private_ssh_key

# Docker Hub (如果使用)
DOCKER_HUB_USERNAME=your_dockerhub_username
DOCKER_HUB_TOKEN=your_dockerhub_token

# 云服务配置 (可选)
ALIYUN_ACCESS_KEY_ID=your_aliyun_access_key
ALIYUN_ACCESS_KEY_SECRET=your_aliyun_secret_key

# 通知配置 (可选)
SLACK_WEBHOOK_URL=your_slack_webhook_url
DINGTALK_WEBHOOK_URL=your_dingtalk_webhook_url
```

### 3. 环境配置

进入 `Settings > Environments` 创建以下环境：

#### Development Environment
```yaml
环境名称: development
保护规则:
  ✅ Required reviewers: (空 - 自动部署)
  ✅ Wait timer: 0 minutes
环境变量:
  DB_HOST: dev-mysql.example.com
  DB_NAME: common_admin_dev
  REDIS_HOST: dev-redis.example.com
环境密钥:
  DB_PASSWORD: ${{ secrets.DB_PASSWORD_DEV }}
  REDIS_PASSWORD: ${{ secrets.REDIS_PASSWORD_DEV }}
```

#### Production Environment
```yaml
环境名称: production
保护规则:
  ✅ Required reviewers: [your_github_username]
  ✅ Wait timer: 5 minutes
环境变量:
  DB_HOST: prod-mysql.example.com
  DB_NAME: common_admin_prod
  REDIS_HOST: prod-redis.example.com
环境密钥:
  DB_PASSWORD: ${{ secrets.DB_PASSWORD_PROD }}
  REDIS_PASSWORD: ${{ secrets.REDIS_PASSWORD_PROD }}
```

## 🛠️ Maven配置文件

### 1. 添加必要的Maven插件

在 `common-admin-parent/pom.xml` 中添加：

```xml
<plugins>
    <!-- JaCoCo测试覆盖率 -->
    <plugin>
        <groupId>org.jacoco</groupId>
        <artifactId>jacoco-maven-plugin</artifactId>
        <version>0.8.8</version>
        <executions>
            <execution>
                <goals>
                    <goal>prepare-agent</goal>
                </goals>
            </execution>
            <execution>
                <id>report</id>
                <phase>test</phase>
                <goals>
                    <goal>report</goal>
                </goals>
            </execution>
        </executions>
    </plugin>

    <!-- Checkstyle代码规范检查 -->
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-checkstyle-plugin</artifactId>
        <version>3.2.0</version>
        <configuration>
            <configLocation>checkstyle.xml</configLocation>
            <encoding>UTF-8</encoding>
            <consoleOutput>true</consoleOutput>
            <failsOnError>true</failsOnError>
        </configuration>
    </plugin>

    <!-- SpotBugs静态分析 -->
    <plugin>
        <groupId>com.github.spotbugs</groupId>
        <artifactId>spotbugs-maven-plugin</artifactId>
        <version>4.7.3.0</version>
    </plugin>

    <!-- OWASP依赖安全检查 -->
    <plugin>
        <groupId>org.owasp</groupId>
        <artifactId>dependency-check-maven</artifactId>
        <version>8.4.0</version>
        <configuration>
            <failBuildOnCVSS>7</failBuildOnCVSS>
        </configuration>
    </plugin>
</plugins>
```

## 🌍 环境特定配置

### 1. 创建测试环境配置

创建 `common-admin-web/src/main/resources/application-test.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/common_admin_test?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: root
    driver-class-name: com.mysql.cj.jdbc.Driver

  redis:
    host: 127.0.0.1
    port: 6379
    database: 1

# 测试环境日志级别
logging:
  level:
    com.common: DEBUG
    org.springframework.web: DEBUG

# 禁用某些不必要的功能
management:
  endpoints:
    web:
      exposure:
        include: health,info
```

### 2. 创建Docker环境配置

创建 `common-admin-web/src/main/resources/application-docker.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://${DB_HOST:mysql}:${DB_PORT:3306}/${DB_NAME:common_admin}?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:root123456}
    driver-class-name: com.mysql.cj.jdbc.Driver

  redis:
    host: ${REDIS_HOST:redis}
    port: ${REDIS_PORT:6379}
    password: ${REDIS_PASSWORD:}
    database: 0

# Docker环境特定配置
server:
  port: 8080

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: always
```

## 🔧 本地开发环境配置

### 1. 启动本地环境

```bash
# 启动基础服务（MySQL + Redis）
docker-compose up -d mysql redis

# 等待服务启动
sleep 30

# 启动应用（IDE中运行或命令行）
mvn spring-boot:run -f common-admin-parent/common-admin-web/pom.xml
```

### 2. 完整Docker环境

```bash
# 构建并启动所有服务
docker-compose up -d

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f app
```

## 📊 监控配置

### 1. Prometheus配置

创建 `docker/prometheus/prometheus.yml`：

```yaml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'spring-boot-app'
    static_configs:
      - targets: ['app:8080']
    metrics_path: '/actuator/prometheus'
    scrape_interval: 5s
```

### 2. Grafana配置

创建 `docker/grafana/datasources/prometheus.yml`：

```yaml
apiVersion: 1
datasources:
  - name: Prometheus
    type: prometheus
    url: http://prometheus:9090
    access: proxy
    isDefault: true
```

## 🚨 故障排除

### 常见问题

1. **GitHub Actions失败**
   - 检查Secrets是否正确配置
   - 确认分支保护规则是否过于严格
   - 查看Actions日志中的具体错误信息

2. **Docker构建失败**
   - 确保JAR文件路径正确
   - 检查Dockerfile中的指令
   - 验证基础镜像是否可用

3. **数据库连接问题**
   - 确认环境变量配置
   - 检查网络连接
   - 验证数据库服务是否正常启动

### 日志查看

```bash
# 查看GitHub Actions日志
# 访问：https://github.com/your-username/your-repo/actions

# 查看Docker容器日志
docker-compose logs -f [service_name]

# 查看应用日志
tail -f logs/application.log
```

## 📚 相关文档

- [GitHub Actions Documentation](https://docs.github.com/en/actions)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- [Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)

---

## 🤝 贡献指南

1. Fork此仓库
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交变更 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启Pull Request

---

*配置完成后，您的项目将拥有完整的CI/CD流程和监控能力！* 