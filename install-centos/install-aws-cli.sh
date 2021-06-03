# 开启命令显示

# set -x

# Ctrl-C 直接退出整个脚本

trap "exit" INT

# 必须用 Root 来执行

echo '[INFO] Checking root permission...'

[ $(id -u) -eq '0' ] || {
  &>2 echo '[ERROR] Script must be run as root.'
  exit 1
}

# 安装 AWS CLI v2

echo '[INFO] Downloading AWS CLI v2...'

cd /tmp
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64-2.0.30.zip" -o "awscliv2.zip"
unzip awscliv2.zip

echo '[INFO] Installing AWS CLI v2...'

sudo ./aws/install

# 测试是否成功

/usr/local/bin/aws --version || {
  &>2 echo '[ERROR] Installation failed.'
}

echo '[INFO] AWS CLI v2 installed.'