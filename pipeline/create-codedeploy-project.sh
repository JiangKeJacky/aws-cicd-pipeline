aws deploy create-application --application-name SpringBoot_Test

aws deploy create-deployment-group \
  --application-name SpringBoot_Test \
  --deployment-group-name SpringBoot_DepGroup \
  --deployment-config-name CodeDeployDefault.OneAtATime \
  --ec2-tag-filters Key=Name,Value=CodeDeployDemo,Type=KEY_AND_VALUE \
  --service-role-arn arn:aws:iam::344336018550:role/CodeDeployServiceRole