#!/usr/bin/env bash

docker run --rm -v `pwd`:/SmsPanel -u `stat -c "%u:%g" build.gradle` java:8-jdk /bin/bash -c "cd /SmsPanel && ./gradlew clean test bootRepackage"
