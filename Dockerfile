# Use an official OpenJDK runtime as a base image
FROM maven:3.8.4-openjdk-17-slim

# Set the working directory inside the container
WORKDIR /TelegramBot

# Copy the Maven POM file and download the dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the application source code
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Specify the command to run your application
CMD ["java", "-cp", "target/classes", "Bot"]
