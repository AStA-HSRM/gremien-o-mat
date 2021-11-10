#!/bin/bash

IMAGENAME="gremiomat-spring"
IMAGETAG="latest"
IMAGETAGNAME="$IMAGENAME:$IMAGETAG"

START="$(date +%s)"
echo "Starting build process"
docker run --rm -v "$PWD":/home/gradle/project -w /home/gradle/project gradle:jdk11 gradle clean build
JAR_FILE=$(ls build/libs/)
docker build -t "$IMAGETAGNAME" . --no-cache --build-arg JAR_FILE=$JAR_FILE
END="$(date +%s)"
BUILDTIME=$((END-START))
echo -e "Build successful!\nDocker image was tagged as $IMAGETAGNAME\nComplete build took $BUILDTIME seconds"