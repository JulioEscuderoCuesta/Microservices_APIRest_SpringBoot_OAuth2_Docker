# Use official OpenJDK 17 image as the base image
FROM eclipse-temurin:17-alpine

# Argument for JAR file
ARG JAR_FILE

# Create new group devopsc & Create new admin without root privileges and add it to the group
RUN addgroup -g 1001 devopsc && \
    adduser -D -u 1001 -G devopsc admin

# Crear app directory
RUN mkdir -p /app && \
    chown -R admin:devopsc /app

# Change working directory
WORKDIR /app

# Copy JAR 
COPY ${JAR_FILE} api-gateway.jar

# Switch user
USER admin

# Expose port
EXPOSE 8080

# Environment variables for JVM optimization in containers
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -XX:+UseG1GC"

# Init command
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar api-gateway.jar"]