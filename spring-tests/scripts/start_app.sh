#!/bin/bash
#ps aux | grep spring-test-unit*.jar | grep -v grep | aws '{print $2}' | xargs kill -9
#nohup java -jar /app/spring-test-unit*.jar --server.port=80
APP_NAME=spring-test-unit*.jar
#检查程序是否在运行
is_exist(){
  pid=`ps -ef|grep $APP_NAME|grep -v grep|awk '{print $2}' `
  #如果不存在返回1，存在返回0
  if [ -z "${pid}" ]; then
   return 1
  else
    return 0
  fi
}

#启动方法
  is_exist
  if [ $? -eq "0" ]; then
    echo "${APP_NAME} is already running. pid=${pid} ."
  else
    nohup java -jar $APP_NAME --server.port=80 > /dev/null 2>&1 &
  fi