su

yum install -y python3

python3 -m pip install awscli

ln -s /usr/local/bin/aws /usr/bin

yum install -y java-1.8.0-openjdk

yum install -y java-devel

yum install -y wget

wget -O /etc/yum.repos.d/jenkins.repo https://pkg.jenkins.io/redhat-stable/jenkins.repo

rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io.key

yum install -y git

//yum install -y maven
wget https://mirror.olnevhost.net/pub/apache/maven/maven-3/3.8.1/binaries/apache-maven-3.8.1-bin.tar.gz

tar xzvf apache-maven-3.8.1-bin.tar.gz

cp -R apache-maven-3.8.1 /usr/local/lib

export PATH=/usr/local/lib/apache-maven-3.8.1/bin:$PATH

yum install -y jenkins

chkconfig jenkins on

systemctl start jenkins

cat /var/lib/jenkins/secrets/initialAdminPassword