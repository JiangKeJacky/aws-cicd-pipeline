aws iam create-role --role-name CodeDeployServiceRole --assume-role-policy-document file://CodeDeployDemo-Trust.json
aws iam attach-role-policy --role-name CodeDeployServiceRole --policy-arn arn:aws:iam::aws:policy/service-role/AWSCodeDeployRole

aws deploy create-application --application-name SpringBoot_Test

aws deploy create-deployment-group \
  --application-name SpringBoot_Test \
  --deployment-group-name SpringBoot_DepGroup \
  --deployment-config-name CodeDeployDefault.OneAtATime \
  --ec2-tag-filters Key=Name,Value=CodeDeployDemo,Type=KEY_AND_VALUE \
  --service-role-arn arn:aws:iam::031665789495:role/CodeDeployServiceRole