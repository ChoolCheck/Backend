#!/bin/bash

REPOSITORY=/home/ubuntu/github_action
cd $REPOSITORY

APP_NAME=demo
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep '.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

CURRENT_PID=$(pgrep -fl demo | grep java | awk '{print $1}')

if [ -z "$CURRENT_PID" ]; then
    echo "NOT RUNNING"
else
    echo "> kill -9 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo "> $JAR_PATH 배포"
nohup java -jar $JAR_NAME > $REPOSITORY/nohup.out 2>&1 &