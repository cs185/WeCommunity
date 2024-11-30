#!/bin/bash

# Update system packages
yum update -y

# Install Java 17 if not already installed
if ! java -version | grep "17"; then
  amazon-linux-extras enable corretto17
  yum install -y java-17-amazon-corretto
fi
