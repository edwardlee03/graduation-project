FROM java:8
EXPOSE 32202:32202

# 作者
MAINTAINER Tyler
# 将jar包添加到容器中并更名为app.jar
ADD target/*.jar /wechat-service-0.1.jar

# 运行jar包
ENTRYPOINT ["nohup","java","-jar","/wechat-service-0.1.jar"]