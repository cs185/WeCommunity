#!/bin/bash

# Find the process ID of the Spring Boot application and terminate it
APP_NAME="wecommunity-0.0.1-SNAPSHOT.jar"
APP_PATH="/home/ec2-user/wecommunity"

PID=$(ps -ef | grep $APP_NAME | grep -v grep | awk '{print $2}')

if [ -z "$PID" ]; then
  echo "No application is running."
else
  echo "Stopping application with PID: $PID"
  kill -9 $PID
fi
