# 使用官方 Maven 镜像作为构建环境
FROM maven:3.8.6-openjdk-17 AS build

# 设置工作目录
WORKDIR /app

# 复制 pom.xml 和源代码
COPY pom.xml .
COPY src ./src

# 构建应用程序
RUN mvn clean package -DskipTests

# 使用更小的运行时镜像
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 从构建阶段复制生成的 jar 文件
COPY --from=build /app/target/text-editor-1.0-SNAPSHOT.jar app.jar

# 创建挂载点用于数据持久化
VOLUME /app/data

# 暴露端口（如果需要）
# EXPOSE 8080

# 运行应用程序
ENTRYPOINT ["java", "-jar", "app.jar"]
