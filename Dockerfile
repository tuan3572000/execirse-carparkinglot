# Use an official OpenJDK runtime as a parent image
FROM openjdk:17.0.1-jdk-slim

# Set the working directory to /app
WORKDIR /app

# Copy the packaged JAR file into the container
COPY target/carparking-0.0.1-SNAPSHOT.jar /app/carparking.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the JAR file when the container starts
CMD ["java", "-jar", "carparking.jar"]



# Execute command below to build an image
# docker build -t carparking:latest .
