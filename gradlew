#!/bin/sh
set -e
JAR=gradle/wrapper/gradle-wrapper.jar
if [ ! -f "$JAR" ]; then
  echo "Downloading wrapper jar..."
  mkdir -p gradle/wrapper
  curl -L -o $JAR https://github.com/gradle/gradle/raw/master/gradle/wrapper/gradle-wrapper.jar
fi
exec java -jar $JAR "$@"
