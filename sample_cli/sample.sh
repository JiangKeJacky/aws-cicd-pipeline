#!/bin/bash

# CREATE A REPOSITORY
aws codecommit create-repository --repository-name just-test 
# --repository-description 'this is my description'

# IN IAM > USER > HTTP GIT CREDENTIALS > GENERATE CREDENTIALS
# not doable in CLI

# CLONE A REPO
git clone https://git-codecommit.us-east-2.amazonaws.com/v1/repos/just-test

