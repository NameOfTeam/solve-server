FROM openjdk:21-alpine
WORKDIR /app
ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENV TZ=Asia/Seoul
RUN apt-get update && apt-get install -y python3
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
