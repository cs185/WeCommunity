#!/bin/bash

APP_NAME="wecommunity-0.0.1-SNAPSHOT.jar"
APP_PATH="/home/ec2-user/wecommunity"
LOG_DIR="/var/log/community"
LOG_FILE="$LOG_DIR/application.log"

echo "Starting application: $APP_NAME"
export SPRING_PROFILES_ACTIVE=prod
nohup java -jar $APP_PATH/$APP_NAME > $LOG_FILE 2>&1 &
