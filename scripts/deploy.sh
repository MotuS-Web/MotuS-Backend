#!/usr/bin/env bash
chmod +x /home/ubuntu/motus/scripts/deploy.sh

REPOSITORY=/home/ubuntu/motus
# shellcheck disable=SC2034
APP_NAME=motus_CICD
# shellcheck disable=SC2034
APP_LOG="$PROJECT_ROOT/application.log"
# shellcheck disable=SC2034
ERROR_LOG="$PROJECT_ROOT/error.log"
# shellcheck disable=SC2034
DEPLOY_LOG="$PROJECT_ROOT/deploy.log"

TIME_NOW=$(date +%c)


# shellcheck disable=SC2164
cd $REPOSITORY

# shellcheck disable=SC2010
JAR_NAME=$(ls $REPOSITORY/build/libs/ | grep 'SNAPSHOT.jar' | tail -n 1)
JAR_PATH=$REPOSITORY/build/libs/$JAR_NAME

CURRENT_PID=$(pgrep -f "$JAR_NAME")

if [ -z "$CURRENT_PID" ]
then
  echo "$TIME_NOW > 현재 실행중인 애플리케이션이 없습니다" >> "$DEPLOY_LOG"
else
   # shellcheck disable=SC2086
   echo "$TIME_NOW > 실행중인 $CURRENT_PID 애플리케이션 종료 " >> $DEPLOY_LOG
  kill -15 "$CURRENT_PID"
  sleep 5
fi

echo "> $JAR_PATH 배포"
nohup java -jar "$JAR_PATH" > /dev/null 2> /dev/null < /dev/null &