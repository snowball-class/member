FROM amazoncorretto:21.0.6-alpine
CMD ["./gradlew", "clean", "build"]
ARG JAR_FILE_PATH=build/libs/*.jar
COPY ${JAR_FILE_PATH} app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]