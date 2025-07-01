# 使用官方的OpenJDK 17镜像作为基础镜像
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 安装必要的工具
RUN apt-get update && apt-get install -y \
    curl \
    wget \
    && rm -rf /var/lib/apt/lists/*

# 创建应用用户（安全最佳实践）
RUN addgroup --system --gid 1001 spring && \
    adduser --system --uid 1001 --gid 1001 --no-create-home spring

# 复制Maven构建的JAR文件
COPY common-admin-web/target/common-admin-web-*.jar app.jar

# 创建日志目录
RUN mkdir -p /app/logs && chown spring:spring /app/logs

# 切换到应用用户
USER spring:spring

# 暴露端口
EXPOSE 8080

# 设置JVM参数
ENV JAVA_OPTS="-Xms256m -Xmx1024m -XX:+UseG1GC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:/app/logs/gc.log"

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# 启动应用
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"] 