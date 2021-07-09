aws ec2 create-security-group --group-name CodeDeployDemo-SG --description "CodeDeployDemo test security group"

aws ec2 authorize-security-group-ingress \
    --group-name CodeDeployDemo-SG \
    --protocol tcp \
    --port 22 \
    --cidr 0.0.0.0/0

aws ec2 authorize-security-group-ingress \
    --group-name CodeDeployDemo-SG \
    --protocol tcp \
    --port 80 \
    --cidr 0.0.0.0/0

aws autoscaling create-launch-configuration \
  --launch-configuration-name CodeDeployDemo-AS-Configuration \
  --image-id ami-0e8e39877665a7c92 \
  --key-name ee-default-keypair \
  --security-groups sg-0638612b576bc03aa \
  --iam-instance-profile CodeDeployDemo-EC2-Instance-Profile \
  --instance-type t3.small


aws autoscaling create-auto-scaling-group \
  --auto-scaling-group-name CodeDeployDemo-AS-Group \
  --launch-configuration-name CodeDeployDemo-AS-Configuration \
  --min-size 3 \
  --max-size 3 \
  --desired-capacity 3 \
  --vpc-zone-identifier "subnet-8be14dc3,subnet-17ac1071" \
  --tags Key=Name,Value=CodeDeployDemo,PropagateAtLaunch=true

aws ssm create-association \
  --name AWS-ConfigureAWSPackage \
  --targets Key=tag:Name,Values=CodeDeployDemo \
  --parameters action=Install,name=AWSCodeDeployAgent \
  --schedule-expression "cron(0 2 ? * SUN *)"