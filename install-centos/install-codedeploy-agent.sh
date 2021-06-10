#!/bin/bash

#

IAM_ROLE_ARN='iam::xxx'
CODEDEPLOY_BIN="/opt/codedeploy-agent/bin/codedeploy-agent"

# 开启命令显示

# set -x

# Ctrl-C 直接退出整个脚本

trap "exit" INT

# 必须用 Root 来执行

echo '[INFO] Checking root permission...'

[ $(id -u) -eq '0' ] || {
  &>2 echo '[ERROR] Script must be run as root.';
  exit 1;
}

#

REGION=$(/usr/local/bin/aws configure get region)

if [ "$REGION" != 'cn-north-1' ] && [ "$REGION" != 'cn-northwest-1' ]; then
  &>2 echo '[ERROR] This script only supports cn-north-1 and cn-northwest-1; region must be set.';
  exit 1;
fi

# 安装必须的库

yum update -y && yum install ruby gem git wget -y || {
  &>2 echo '[ERROR] Cannot install or update ruby, wget.';
  exit 1;
}

# 如果 CodeDeloy Agent 已经安装过，停止并移除

echo '[INFO] Stop installed agent if it exists...'
$CODEDEPLOY_BIN stop
yum remove codedeploy-agent -y

# 下载 CodeDeploy Agent

echo '[INFO] Downloading agent...'

cd /tmp
if ! wget https://aws-codedeploy-${REGION}.s3.${REGION}.amazonaws.com.cn/latest/install || ! chmod +x ./install; then
  &>2 echo '[ERROR] Could not download agent.';
  exit 1;
fi

# 开始安装

echo '[INFO] Installing agent...'

./install auto

# 尝试启动 CodeDeploy Agent

service codedeploy-agent start && sleep 3

service codedeploy-agent status || {
  &>2 echo '[ERROR] Could not start agent.';
  exit 1;
}

# 创建自管主机专属配置文件并写入角色、区域和密钥地址

echo '[INFO] Creating on-premises configuration file...'

tee /etc/codedeploy-agent/conf/codedeploy.onpremises.yml <<EOF
iam_session_arn: ${IAM_ROLE_ARN}
aws_credentials_file: /etc/codedeploy-agent/conf/credentials
region: ${REGION}
EOF

echo '[INFO] CodeDeploy agent installed.'