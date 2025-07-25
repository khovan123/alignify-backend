# ==============================================================================
# Multi-stage Dockerfile for Alignify Backend
# Spring Boot Application with Java 21, MongoDB, WebSocket support
# ==============================================================================

# ==============================================================================
# Build Stage
# ==============================================================================
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Metadata
LABEL stage=builder
LABEL description="Build stage for Alignify Backend"

# Set working directory
WORKDIR /build

# Copy Maven wrapper and configuration files
COPY .mvn/ .mvn/
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .

# Make mvnw executable
RUN chmod +x ./mvnw

# Copy source code
COPY src/ ./src/

# Build the application
RUN ./mvnw clean package -DskipTests -B && \
    mkdir -p target/dependency && \
    cd target/dependency && \
    jar -xf ../*.jar

# ==============================================================================
# Runtime Stage
# ==============================================================================
FROM eclipse-temurin:21-jre-alpine AS runtime

# Metadata and labels
LABEL maintainer="Alignify Team"
LABEL version="1.0.0"
LABEL description="Alignify Backend - Spring Boot Application"
LABEL java.version="21"
LABEL spring.boot.version="3.4.5"

# Install minimal necessary packages
RUN apk add --no-cache \
        ca-certificates \
        tzdata \
        curl && \
    rm -rf /var/cache/apk/*

# Set timezone to Vietnam (Ho Chi Minh City)
ENV TZ=Asia/Ho_Chi_Minh
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Create application user for security (non-root)
RUN addgroup -g 1001 -S alignify && \
    adduser -u 1001 -S alignify -G alignify

# Set working directory
WORKDIR /app

# Create necessary directories with proper permissions
RUN mkdir -p /app/logs /app/tmp && \
    chown -R alignify:alignify /app

# Copy application files from builder stage with proper ownership
COPY --from=builder --chown=alignify:alignify /build/target/dependency/BOOT-INF/lib /app/lib
COPY --from=builder --chown=alignify:alignify /build/target/dependency/META-INF /app/META-INF
COPY --from=builder --chown=alignify:alignify /build/target/dependency/BOOT-INF/classes /app

# Application configuration
ENV SPRING_PROFILES_ACTIVE=prod
ENV SERVER_PORT=8080
ENV JAVA_OPTS=""

# JVM configuration for optimal performance
ENV JVM_OPTS="-XX:+UseG1GC \
    -XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:+PrintGCDetails \
    -XX:+PrintGCTimeStamps \
    -XX:+PrintGCApplicationStoppedTime \
    -XX:+UseStringDeduplication \
    -XX:+OptimizeStringConcat \
    -Djava.security.egd=file:/dev/./urandom \
    -Dspring.backgroundpreinitializer.ignore=true \
    -Djava.awt.headless=true \
    -Dfile.encoding=UTF-8 \
    -Duser.timezone=Asia/Ho_Chi_Minh"

# Expose application port
EXPOSE 8080

# Health check endpoint (Spring Boot Actuator)
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Switch to non-root user
USER alignify:alignify

# Start the application with optimized JVM settings
CMD ["sh", "-c", "java $JVM_OPTS $JAVA_OPTS -cp /app:/app/lib/* com.api.Server"]

# ==============================================================================
# Development Stage (optional for debugging)
# ==============================================================================
FROM runtime AS development

# Switch back to root for development tools installation
USER root

# Install development tools
RUN apk add --no-cache \
        bash \
        vim \
        git \
        htop && \
    rm -rf /var/cache/apk/*

# Enable debugging
ENV JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"
ENV SPRING_PROFILES_ACTIVE=dev

# Expose debug port
EXPOSE 5005

# Switch back to application user
USER alignify:alignify

# Development command with debugging enabled
CMD ["sh", "-c", "java $JVM_OPTS $JAVA_OPTS $JAVA_TOOL_OPTIONS -cp /app:/app/lib/* com.api.Server"]