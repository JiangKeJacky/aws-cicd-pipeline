# 自动获取角色信息

echo '[INFO] Installing STS helper...'

aws=/usr/local/bin/aws
CREDS="$($aws sts assume-role --role-arn $IAM_ROLE_ARN --role-session-name test --output text | awk 'FNR==2 {print "aws_access_key_id=" $2; print "aws_secret_access_key=" $4; print "aws_session_token=" $5}')"

[ $? -eq 0 ] || {
  &>2 echo '[ERROR] Unable to assume role.';
}

echo $CREDS > /etc/codedeploy-agent/conf/credentials

# cron 0 * * * * code-deploy-sts-helper.sh