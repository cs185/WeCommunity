#!/bin/bash

# Wait for a few seconds to allow the application to start
sleep 10

# Check if the application is running on the expected port (e.g., 8080)
if curl -s http://localhost:8080/wecommunity/index > /dev/null; then
  echo "Application is running successfully."
  exit 0
else
  echo "Application failed to start."
  exit 1
fi
