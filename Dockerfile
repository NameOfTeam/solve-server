FROM openjdk:21
ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENV TZ=Asia/Seoul
RUN apk add --no-cache python3
ENTRYPOINT ["java", "-jar", "/app.jar"]