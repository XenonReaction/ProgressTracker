# Build stage: compile the Spring Boot jar with Maven.
# Tests are skipped here because the integration tests need Docker (Testcontainers),
# which isn't available inside an image build. Run `./mvnw test` on the host instead.
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
# Download dependencies first so they're cached until pom.xml changes
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q package -DskipTests && cp target/progression-tracker-backend-*.jar app.jar

# Runtime stage: just a Java 21 runtime and the jar, running as a non-root user
FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S app && adduser -S app -G app
USER app
WORKDIR /app
COPY --from=build /app/app.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
