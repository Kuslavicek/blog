FROM openjdk:18.0.2-jdk
MAINTAINER com.example
COPY build/libs/blog-0.0.1-SNAPSHOT.jar blog-0.0.1.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/blog-0.0.1.jar"]