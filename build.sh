#!/bin/bash

echo "Starting build process"

docker run --rm -u gradle -v "$PWD":/home/gradle/project -w /home/gradle/project gradle:jdk11 gradle clean build

