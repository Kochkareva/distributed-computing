FROM openjdk:17-jdk

WORKDIR /app
COPY ./exercise-app/build/libs/exercise-app-0.0.1-SNAPSHOT.jar /app/exercise-app-0.0.1-SNAPSHOT.jar
EXPOSE 8081

CMD ["java", "-jar", "exercise-app-0.0.1-SNAPSHOT.jar"]