#!/bin/bash
#ps aux | grep spring-test-unit*.jar | grep -v grep | aws '{print $2}' | xargs kill -9
#nohup java -jar /app/spring-test-unit*.jar --server.port=80
APP_NAME=/app/spring-test-unit*.jar
#检查程序是否在运行
pid=`ps -ef|grep $APP_NAME|grep -v grep|awk '{print $2}' `
#如果不存在启动，存在返回
if [ -z "${pid}" ]; then
    nohup java -jar $APP_NAME --server.port=8080 > /dev/null 2>&1 &
else
    echo "${APP_NAME} is already running. pid=${pid} ."
fi