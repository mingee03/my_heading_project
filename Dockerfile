# 1. Build Stage
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Gradle Wrapper 및 소스 복사
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .
COPY src src

# gradlew 실행 권한 부여 및 빌드 (테스트 제외)
RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test --no-daemon

# 2. Run Stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Build Stage에서 생성된 jar 파일을 복사
COPY --from=build /app/build/libs/*.jar app.jar

# Render에서 할당하는 PORT를 사용하도록 환경변수 지원 (스프링은 PORT 환경변수를 자동 인식합니다)
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
