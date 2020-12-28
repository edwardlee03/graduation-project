FROM java:8
EXPOSE 32207:32207

MAINTAINER Tyler

ADD target/*.jar /web-spider-0.1.jar

ENTRYPOINT ["nohup","java","-jar","/web-spider-0.1.jar"]