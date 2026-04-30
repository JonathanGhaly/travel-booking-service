# ==============================
# Build Stage
# ==============================
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# Copy entire multi-module project
COPY . .

# Make Gradle executable
RUN chmod +x gradlew

# Build ONLY booking service
RUN ./gradlew :travel-booking-service:bootJar --no-daemon

# ==============================
# Runtime Stage
# ==============================
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy built jar from module
COPY --from=builder /app/travel-booking-service/build/libs/*.jar app.jar

# JVM optimization for containers
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

EXPOSE 8083

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]