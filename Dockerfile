=== Dockerfile ===
# Multi-stage Dockerfile for Java CRM Application
# Stage 1: Builder
FROM eclipse-temurin:8-jdk AS builder

# Install Ant
RUN apt-get update && apt-get install -y ant && rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /workspace

# Copy project files
COPY src/ ./src/
COPY build.xml .
COPY manifest.mf .
COPY nbproject/ ./nbproject/

# Download MySQL connector
RUN mkdir -p lib && \
    cd lib && \
    curl -L -o mysql-connector-java-5.1.49.jar https://repo1.maven.org/maven2/mysql/mysql-connector-java/5.1.49/mysql-connector-java-5.1.49.jar

# Build the application
RUN ant clean jar

# Stage 2: Runtime
FROM eclipse-temurin:8-jre-alpine

# Create app user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Set working directory
WORKDIR /app

# Copy JAR from builder
COPY --from=builder /workspace/dist/*.jar app.jar
COPY --from=builder /workspace/lib/*.jar lib/

# Copy resources
COPY src/Resources/ ./Resources/
COPY src/View/ ./View/

# Set environment variables
ENV JAVA_OPTS="-Xmx512m -Xms256m -XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0" \
    DB_HOST="localhost" \
    DB_PORT="3306" \
    DB_NAME="crm_db" \
    DB_USERNAME="crm_user" \
    DB_PASSWORD="changeme" \
    VIEW_PATH="/View/"

# Change ownership
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Expose port for health checks and potential REST API
EXPOSE 8080

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -cp app.jar:lib/* javacrm.JavaCRM"]
