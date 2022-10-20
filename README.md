环境要求：MVN 3.8.1, JDK openjdk version >"16"


1.	代码编译
mvn clean compile

2.	单元测试
mvn -Dtest=com.example.service.test.unit.UnitTests test

3.	集成测试
mvn -Dtest=com.example.service.test.integration.IntegrationTests test

4.	本地启动测试
mvn spring-boot:run

5.	本地访问
curl http://localhost:8080/manga/sync/Grand

