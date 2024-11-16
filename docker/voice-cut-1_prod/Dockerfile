FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/*.jar app.jar

COPY /home/admin/voice-cut-1/fcmKey.json /app/classes/key/fcmKey.json

CMD ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]