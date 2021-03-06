## 文档说明

pipeline文件包含构建pipeline环境执行脚本和代码

spring-test是一个包含单元测试、部署规范、部署脚本的spring工程脚手架，用户可根据需要在脚手架的基础上构建工程。

## 部署指南

本文涉及的代码管理CodeCommit 和 Gitlab 服务可以二选一，根据需要选择，对应的 pipeline 分别为：pipeline-codecommit-codedeploy 和 pipeline

### 1. 部署准备

确保本地已安装aws cli及配置aws用户凭证的环境变量

本地开发环境的java版本为openjdk 1.8.0以上，本地maven版本3.8.1

本地开发环境下载安装git，http://git-scm.com/downloads

在本地开发环境或Cloud9的终端命令行执行以下命令，将pipeline及脚手架工程下载到本地： 
    
    git clone https://github.com/JiangKeJacky/aws-cicd-pipeline.git

部署所在区域 Amazon S3 中创建devops-template-build-packages-<560853892495>存储桶用于存放编译后的包,<560853892495>替换为aws账户id。


### 2. 配置 CodeCommit 服务

#### 创建CodeCommit存储库（如果有则略过此步骤）

• 打开亚马逊云科技管理控制台，进入CodeCommit服务

• 选择右边菜单栏存储库,点击创建存储库, 输入存储库名字，如devops-template-project，及其他可选配置，点击创建

• 创建访问该存储库的用户，在IAM中创建一个选择AWSCommitPowerUser托管策略，在用户的安全证书设置页面中生成AWSCodeCommit的HTTPS Git凭证，记住用户和密码。详细参考 https://docs.aws.amazon.com/zh_cn/codecommit/latest/userguide/setting-up-gc.html?icmpid=docs_acc_console_connect_np

• 在开发环境中，上传spring-test脚手架工程到新创建的CodeCommit存储库,命令行如下：

    cp -r aws-cicd-pipeline/spring-tests ./
    cd spring-tests
    git init
    git add .
    git commit -m "first commit"
    git remote add origin https://username:password@git-codecommit.ap-southeast-1.amazonaws.com/v1/repos/devops-template-project
    git push -u origin master

在亚马逊云科技管理控制台，进入CodeCommit，存储库devops-template-project中，查看脚手架代码是否上传成功

#### 创建 Gitlab 服务器

（如果使用CodeCommit,或已有 Gitlab，此步骤可以略过）

• 打开亚马逊云科技管理控制台，启动 EC2 新实例

• 选择社区 AMI- GitLab CE 13.0.9 - ami-000d51c0f7eafc5

• 选择自动分配公网 IP

• 安全组入口开放 22，80 端口，22 端口用于远程 SSL 连接，80 端口用于 Gitlab 访问

• 在本地浏览器输入，Gitlab 服务器的域名，如http://ec2-13-213-61-124.ap-southeast-1.compute.amazonaws.com，设置用户名和密码。

• 登录进入后，在 Setting->Access Tokens 中创建 jenkins 访问 Gitlab 的 API Token，勾选 api、read、write 权限，生成 Token，并记住该 Token，在 Jenkins 的 Credential 中使用。

• 创建一个新的项目，导入 spring-test-unit 工程代码。

• 点击项目的左侧菜单 Setting->Webhook

输入 jenkins pipe 的 URL 及 token，选择 push、tag 等触发事件，添加 webhook，并测试触发jinkens 成功。

### 3. 创建Jenkins和Sonar

• 获取 Jenkins-Sonar 的 AMI 镜像

• 从 Jenkins-Sonar 的 AMI 启动 EC2 新实例

• 选择 t2.large 以上的类型

• 选择自动分配公网 IP

• 安全组入口开放 22，8080 和 9000 端口，22 端口用于远程 SSL 连接，8080 端口用于 Jenkins 访问，9000 用于 Sonar 访问

• 实例启动后，通过 ssh 连接到实例，命令行 lsof -i:8080 和 lsof -i:9000 检查 jenkins 和 sonar 是否启动

• 如果 jenkins 未成功启动，命令行 sudo systemctl start jenkins 启动 jenkins

• 如果 sonar 未成功启动，命令行 sudo docker start sonarqube999 启动 sonar

• Jenkins 默认用户名密码为：root/abcd1234

• Sonar 默认用户名密码为：admin/abcd1234

• 在本地浏览器输入EC2 服务器的域名及 sonar 端口号 9000，如http://ec2-13-213-61-124.ap-southeast-1.compute.amazonaws.com:9000

• 打开本地浏览器输入该 EC2 服务器的域名及 Jenkins 端口号 8080，如http://ec2-13-213-61-124.ap-southeast-1.compute.amazonaws.com:8080，将进入Jenkins配置页面

• 配置 CodeCommit 相关参数(如果采用 CodeCommit)，通过 Manage Jenkins->Manage Credentials，Credentials 中添加新的全局 Credential，类型选择 Username+password, 填入 id 及 CodeCommit 中创建的用户名和密码。

• DashBoard 中有 3 个预设的 pipeline. 点击进入修改 cicd-pipeline-codecommit-codedeploy 脚本的参数，包括 GIT_REPOSITORY、AWS_DEFAULT_REGION、AWS_S3_BUCKET_CODE_PACKAGE、SONAR_TOKEN、访问凭证，保存该 pipeline。

• 如果使用Gitlab,配置 Gitlab 相关参数(如果采用 Gitlab)，配置 Gitlab connections，输入 Connection name，Gitlab host URL 为 Gitlab 服务器 URL，Credentials 中添加新的全局 Credential，类型选择 Gitlab API,API Token 填入 Gitlab 创建的 Access Token。测试 Gitlab Connection 成功。 
点击 cicd-pipeline，在 Pipeline 配置页面的构建触发器设置中，查看 Build when a change is pushed to GitLab. GitLab webhook URL，并记住该 URL 地址。点击高级展开选项，设置生成新的 Screte token，记住该 Token。

### 4. 配置 CodeDeploy 服务

• 在本地命终端令行或Cloud9中运行创建IAM角色和策略，将create-codedeploy-project.sh中的arn:aws:iam::**300835872711**:role/CodeDeployServiceRole 的 **300835872711** 修改为自己的 aws 账号，执行脚本： 

    vim create-codedeploy-project.sh
    ./create-codedeploy-role.sh
    ./create-codedeploy-project.sh

### 5. 创建部署服务器

（如果已有可运行应用的服务器，并且已经安装了CodeDeploy agent，此步骤可以略过）

•	一条命令一条命令执行create-deployment-ec2.sh, 注意修改安全组id **<security-groups>** 新建的安全组id以及子网id **<subnet-id>**，将自动创建3个台EC2服务器


### 6. 配置自动测试robot

•	SSH登陆jenkins服务器，编辑/home/jenkins/robot/ACT.robot，其中修改Library REST的URL路径为以上应用访问路径.

### 7. 配置容器服务（使用容器部署）

#### 创建ECR存储库

在亚马逊云科技控制进入ECR服务，创建存储库，修改Jenkins的pipeline-codecommit-codedeploy-docker中ECR_REPO_URL为存储库的URI

#### 创建EKS集群 （如有已有集群则略过）

修改Jenkins的pipeline-codecommit-codedeploy-docker中EKS_CLUSTER_NAME为存储库的集群名

## 运行使用

修改提交项目的代码，提交到 Gitlab，将触发 Jenkins 的 CICD pipeline，可以在 Jenkins 中查看 CICD 进度

点击某个 build，在 pipeline 的 Console Out 中查看下详细的输出，包括代码扫描、单元测试、集成测试、编译、打包等过程。

由于 CodeDeploy 是调用异步执行，CodeDeploy 的详细执行情况在 CodeDeploy 控制台中查看。
