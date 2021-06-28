yum update
yum install ruby wget -y
wget https://aws-codedeploy-ap-southeast-1.s3.ap-southeast-1.amazonaws.com/latest/install
chmod +x ./install
./install auto
service codedeploy-agent status

yum install java -y