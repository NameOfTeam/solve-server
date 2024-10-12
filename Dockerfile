FROM openjdk:21-jdk
WORKDIR /app
ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENV TZ=Asia/Seoul
RUN apt-get update && apt-get install -y python3 && apt-get clean && rm -rf /var/lib/apt/lists/*
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
