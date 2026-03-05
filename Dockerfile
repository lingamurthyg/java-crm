# Multi-stage Dockerfile for Java CRM Application
# Stage 1: Build Stage
FROM maven:3.8.6-openjdk-8-slim AS builder

WORKDIR /workspace

# Install Ant for building legacy NetBeans projects
RUN apt-get update && apt-get install -y ant && rm -rf /var/lib/apt/lists/*

# Copy the entire project
COPY java-crm/ /workspace/

# Build the application using Ant
RUN ant clean jar

# Stage 2: Runtime Stage
FROM amazoncorretto:8

WORKDIR /app

# Create non-root user for security
RUN groupadd -r javacrm && useradd -r -g javacrm javacrm

# Set environment variables
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0" \
    TZ=UTC \
    DB_HOST="3.227.166.251" \
    DB_NAME="U07k1T" \
    DB_USER="U07k1T" \
    DB_PASSWORD="53689053296"

# Copy built JAR from builder stage
COPY --from=builder /workspace/dist/java-crm.jar /app/java-crm.jar
COPY --from=builder /workspace/dist/lib/ /app/lib/

# Change ownership to non-root user
RUN chown -R javacrm:javacrm /app

# Switch to non-root user
USER javacrm

# Expose application port
EXPOSE 8080

# Run the application
CMD ["sh", "-c", "java $JAVA_OPTS -jar /app/java-crm.jar"]