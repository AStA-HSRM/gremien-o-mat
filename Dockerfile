FROM openjdk:11-jdk-slim
RUN addgroup --system spring && adduser --system --group spring
RUN mkdir /srv/spring && \
    chown -R spring:spring /srv/spring
ENV LOGGING_FILE_NAME=/srv/spring/error.log
USER spring:spring
ARG JAR_FILE=build/libs/gremiomat-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]