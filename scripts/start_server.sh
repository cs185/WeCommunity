#!/bin/bash

APP_NAME="wecommunity-0.0.1-SNAPSHOT.jar"
APP_PATH="/home/ec2-user/wecommunity"
LOG_PATH="/home/ec2-user/wecommunity/application.log"

echo "Starting application: $APP_NAME"
nohup java -jar $APP_PATH/$APP_NAME > $LOG_PATH 2>&1 &
