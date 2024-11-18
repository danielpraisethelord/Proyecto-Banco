# Stage 1: Build the application
FROM eclipse-temurin:23-jdk AS builder

# Set the working directory
WORKDIR /app

# Copy the JAR file from the local machine to the container
COPY target/*.jar /app/app.jar

# Expose the port the app will run on
EXPOSE 8081

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]