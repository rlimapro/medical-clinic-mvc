FROM amazoncorretto:8-alpine-jdk AS build
WORKDIR /app
COPY pom.xml .
RUN apk add --no-cache maven && mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

FROM amazoncorretto:8-alpine-jre
WORKDIR /app
COPY --from=build /app/target/demo-security-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]