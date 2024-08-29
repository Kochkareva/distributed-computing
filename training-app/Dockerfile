FROM openjdk:17-jdk

WORKDIR /app
COPY ./training-app/build/libs/training-app-0.0.1-SNAPSHOT.jar /app/training-app-0.0.1-SNAPSHOT.jar
EXPOSE 8082

CMD ["java", "-jar", "training-app-0.0.1-SNAPSHOT.jar"]