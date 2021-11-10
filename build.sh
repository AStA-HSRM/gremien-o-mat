#!/bin/bash

IMAGENAME="gremiomat-spring:latest"

START="$(date +%s)"
echo "Starting build process"
docker run --rm -u gradle -v "$PWD":/home/gradle/project -w /home/gradle/project gradle:jdk11 gradle clean build
JAR_FILE=$(ls build/libs/)
docker build -t "$IMAGENAME" . --no-cache --build-arg JAR_FILE=$JAR_FILE
END="$(date +%s)"
BUILDTIME=$((END-START))
echo -e "Build successful!\nDocker image was tagged as $IMAGENAME\nComplete build took $BUILDTIME seconds"