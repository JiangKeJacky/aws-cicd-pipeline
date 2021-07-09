yum update
yum install -y docker
systemctl start docker

docker load -i ci_cd_sonar.tar
docker run -d --name sonarqube999 -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true -p 9000:9000 ci_cd_sonar