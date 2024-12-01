#!/bin/bash

LOG_DIR="/var/log/community"

if [ ! -d "$LOG_DIR" ]; then
  echo "Creating log directory: $LOG_DIR"
  mkdir -p $LOG_DIR
fi

echo "Ensuring proper ownership and permissions"
chown ec2-user:ec2-user $LOG_DIR
chmod 755 $LOG_DIR
