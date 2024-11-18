# Start with a base image containing Java runtime
FROM openjdk:22-jdk

# Add Maintainer Info
LABEL maintainer="hakan.sengul@obss.tech"

# Add a volume pointing to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside this container
EXPOSE 8761

# The application's jar file
ARG JAR_FILE=target/eureka-server-module-1.0.0.jar

# Add the application's jar to the container
ADD ${JAR_FILE} app.jar

# Run the jar file
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]