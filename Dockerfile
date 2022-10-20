FROM amazonlinux:latest
WORKDIR /opt/

RUN yum install java -y
RUN mkdir /opt/app
ADD spring-test-unit*.jar /opt/app/spring-test-unit.jar

EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/opt/app/spring-test-unit.jar" ]