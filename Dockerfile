# Use a lightweight JDK base image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar file into the container
COPY target/epam-ASEAT-1.0-SNAPSHOT.jar app.jar

# Expose port 8080 (or whatever port your app uses)
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
