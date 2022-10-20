#!/bin/bash

APP_NAME=/app/spring-test-unit*.jar
#检查程序是否在运行
pid=`ps -ef|grep $APP_NAME|grep -v grep|awk '{print $2}' `
#如果不存在启动，存在返回
if [ -z "${pid}" ]; then
    echo "${APP_NAME} is NOT running."
else
    echo "${APP_NAME} is already running. pid=${pid} ."
fi

curl -I -s http://localhost:8080  |head -1|awk '{ health = $2=="200"?"server is ok":"server is bad"}END{print health}'
