FROM openjdk:11-slim-buster as build
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .
RUN ./mvnw -B dependency:go-offline
COPY src src
RUN ./mvnw -B package -Dmaven.test.skip

FROM openjdk:11-slim-buster
COPY --from=build target/*.jar pcparts-backend.jar
VOLUME /data
ENTRYPOINT ["java", "-XX:-OmitStackTraceInFastThrow", "-jar","/pcparts-backend.jar"]
