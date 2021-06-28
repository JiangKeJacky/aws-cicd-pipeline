#!/bin/bash

# 开启命令显示

# set -x

# Ctrl-C 直接退出整个脚本


echo '[INFO] Checking root permission...'

[ $(id -u) -eq '0' ] || {
  &>2 echo '[ERROR] Script must be run as root.';
  exit 1;
}

echo '[INFO] Install python...'
yum install -y python3
echo '[INFO] Install awscli...'
python3 -m pip install awscli
ln -s /usr/local/bin/aws /usr/bin

echo '[INFO] Install JDK...'
yum install -y java-1.8.0-openjdk
yum install -y java-devel

echo '[INFO] Install git...'
yum install -y git

echo '[INFO] Install docker...'
yum install -y docker

echo '[INFO] Install maven...'
wget https://mirror.olnevhost.net/pub/apache/maven/maven-3/3.8.1/binaries/apache-maven-3.8.1-bin.tar.gz
tar xzvf apache-maven-3.8.1-bin.tar.gz
cp -R apache-maven-3.8.1 /usr/local/lib
export PATH=/usr/local/lib/apache-maven-3.8.1/bin:$PATH

echo '[INFO] Install jenkins...'
yum install -y wget
wget -O /etc/yum.repos.d/jenkins.repo https://pkg.jenkins.io/redhat-stable/jenkins.repo
rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io.key
yum install -y jenkins
chkconfig jenkins on
systemctl start jenkins

echo '[INFO] Jenkins initial password:'
cat /var/lib/jenkins/secrets/initialAdminPassword