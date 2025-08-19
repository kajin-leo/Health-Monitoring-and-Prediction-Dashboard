FROM maven:3.9-eclipse-temurin-17

WORKDIR /app

# 复制项目文件
COPY pom.xml .
COPY src src

# 直接用 mvn 命令（不用 wrapper）
RUN mvn clean package -DskipTests -B

# 调试：查看生成的文件
RUN ls -la target/
RUN find target/ -name "*.jar" -type f

EXPOSE 8080

# 使用通配符启动
CMD ["sh", "-c", "java -jar target/*.jar"]