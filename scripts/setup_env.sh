#!/bin/bash

java -version
if [ $? = 0 ];then
  echo "java is installed"
else
  echo "java is not installed"
  echo "installing java"
  yum install java-devel -y
fi