FROM openjdk:21
ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENV TZ=Asia/Seoul
RUN apt-get update && apt-get install -y python3
ENTRYPOINT ["java", "-jar", "/app.jar"]