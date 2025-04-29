# 1단계: 빌드 스테이지
FROM gradle:7.6-jdk17 AS builder

# 프로젝트 소스 복사
COPY --chown=gradle:gradle . /app
WORKDIR /app

# 빌드 실행
RUN gradle bootJar --no-daemon

# 2단계: 실행 스테이지
FROM openjdk:17-jdk-slim

# 빌드 결과물 복사
COPY --from=builder /app/build/libs/*.jar app.jar

# 애플리케이션 포트 노출
EXPOSE 8080

# 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]
