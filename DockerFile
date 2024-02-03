# Use an official OpenJDK runtime as a base image
FROM adoptopenjdk/openjdk17:alpine-jre

# Set the working directory inside the container
WORKDIR /TelegramBot

# Copy the Maven POM file and download the dependencies
COPY pom.xml .
RUN ./mvnw dependency:go-offline

# Copy the application source code
COPY src ./src

# Build the application
RUN ./mvnw package -DskipTests

# Specify the command to run your application
CMD ["java", "-cp", "src/main/java", "Bot"]
