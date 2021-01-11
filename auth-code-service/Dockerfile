FROM java:8
EXPOSE 32203:32203

MAINTAINER Tyler
ADD target/*.jar /auth-code-service-0.1.jar

ENTRYPOINT ["nohup","java","-jar","/auth-code-service-0.1.jar"]