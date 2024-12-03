#!/bin/bash

# wait to make sure the server starts
sleep 10

URL="http://127.0.0.1:8080/wecommunity/index"

HTTP_RESPONSE=$(curl -s -o /dev/null -w "%{http_code}" "$URL")

if [ "$HTTP_RESPONSE" -eq 200 ]; then
  echo "Application is running successfully."
  exit 0
else
  echo "Application failed to start. HTTP response code: $HTTP_RESPONSE"
  exit 1
fi
