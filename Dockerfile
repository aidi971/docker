# 基底镜像
FROM openjdk:8-jre-slim

# 复制文件到镜像中
COPY target/docker-1.0-SNAPSHOT.jar app.jar

# 镜像运行命令
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

# 启动镜像时执行的命令（入口点）
ENTRYPOINT ["java","-jar","/app.jar"]