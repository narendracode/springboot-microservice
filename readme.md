[![Build Status](https://travis-ci.org/narendrasoni1989/springboot-microservice.svg?branch=master)](https://travis-ci.org/narendrasoni1989/springboot-microservice)


To build the package :
`mvn package`

To run the application using spring cli :
`mvn spring-boot:run`


To run the application :
`java -jar target/java-springboot-microservice-0.0.1-SNAPSHOT.jar`


If you want to know more about getting started with springboot then follow this :
https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#getting-started-maven-installation

Run application with remote debugging support enabled
`java -Xdebug -Xrunjdwp:server=y,transport=dt_socket,address=8000,suspend=n -jar target/java-springboot-microservice-0.0.1-SNAPSHOT.jar`