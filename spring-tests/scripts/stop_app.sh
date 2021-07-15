#!/bin/bash
#ps aux | grep spring-test-unit*.jar | grep -v grep | aws '{print $2}' | xargs kill -9
APP_NAME=/app/spring-test-unit*.jar
#检查程序是否在运行
pid=`ps -ef|grep $APP_NAME|grep -v grep|awk '{print $2}' `
#如果不存在返回1，存在返回0
if [ -z "${pid}" ]; then
  echo "${APP_NAME} is not running"
else
  kill -9 $pid
fi