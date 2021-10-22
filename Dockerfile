FROM openjdk:11-jdk-slim

# Create user and group to run as
RUN addgroup --system spring && adduser --system --group spring

ARG SPRING_FOLDER=/srv/spring

# Create settings and logs folder
RUN mkdir -pv ${SPRING_FOLDER}/logs && \
    mkdir -pv ${SPRING_FOLDER}/config

# Set path for log file
ENV LOGGING_FILE_NAME=${SPRING_FOLDER}/logs/error.log

# Set path for settings file
ENV SPRING_CONFIG_LOCATION=${SPRING_FOLDER}/config/application.properties

# Copy over application.properties file to container (ENV variables take precedence)
COPY src/main/resources/application.properties.prod ${SPRING_FOLDER}/config/application.properties

# Set proper perms
RUN chown -R spring:spring ${SPRING_FOLDER}

# Set run user and group
USER spring:spring

# Copy over compiled jar
ARG JAR_FILE=build/libs/gremiomat-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar

WORKDIR ${SPRING_FOLDER}

ENTRYPOINT ["java","-jar","/app.jar"]