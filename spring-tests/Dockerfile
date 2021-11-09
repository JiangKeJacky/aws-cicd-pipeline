FROM amazonlinux:latest

#install jdk and tomcat

WORKDIR /opt/

RUN yum install java -y
RUN mkdir /opt/app
ADD spring-test-unit*.jar /opt/app/spring-test-unit.jar

#ADD apache /home/schoolapp/

#jdk enviroment
#ENV JAVA_HOME=/usr/java/jdk1.8.0_121
#ENV JRE_HOME=/usr/java/jdk1.8.0_121/jre
#ENV CLASSPATH=$JAVA_HOME/lib:$JAVA_HOME/jre/lib
#ENV PATH=$JAVA_HOME/bin:$PATH

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/opt/app/spring-test-unit.jar" ]