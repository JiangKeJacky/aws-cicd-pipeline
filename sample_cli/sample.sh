#!/bin/bash

### CODECOMMIT

# CREATE A REPOSITORY
aws codecommit create-repository --repository-name just-test 
# --repository-description 'this is my description'

# IN IAM > USER > HTTP GIT CREDENTIALS > GENERATE CREDENTIALS
# not doable in CLI

# CLONE A REPO
git clone https://git-codecommit.us-east-2.amazonaws.com/v1/repos/just-test
# user will be asked for credentials created in previous step

### CODEDEPLOY

# PERMISSION



### CODEBUILD ###




### CODEDEPLOY ###

# CREATE SERVICE ROLE FOR CODEDEPLOY

aws iam create-role --role-name CodeDeployServiceRole --assume-role-policy-document file://codedeploy-role-trusted-entity.json

# ATTACH RELATED POLICY

aws iam attach-role-policy --role-name CodeDeployServiceRole --policy-arn arn:aws-cn:iam::aws:policy/service-role/AWSCodeDeployRole

# CREATE ROLE FOR EC2 TO PULL BINARIES FROM S3

aws iam create-role --role-name EC2-CodeDeployRole --assume-role-policy-document file://ec2-trusted-entity.json

# ATTACH S3 RELATED POLICY

aws iam put-role-policy --role-name EC2-CodeDeployRole --policy-name EC2-S3 --policy-document file://ec2-s3-policy.json

# CREATE INSTANCE PROFILE

aws iam create-instance-profile --instance-profile-name EC2-CodeDeploy
aws iam add-role-to-instance-profile --instance-profile-name EC2-CodeDeployRole --role-name EC2-CodeDeployRole

# CREATE APPLICATION
aws deploy create application --application-name test-app --compute-platform Server
# --compute-platform Lambda
# --compute-platform ECS



