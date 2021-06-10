#!/bin/bash

IAM_ROLE_ARN='iam::xxx'

aws deploy register-on-premises-instance --instance-name INSTANCE_UNIQUE_NAME --iam-session-arn $IAM_ROLE_ARN

# aws deploy deregister-on-premises-instance --instance-name XXX