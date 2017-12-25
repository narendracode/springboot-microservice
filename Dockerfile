FROM openjdk:8u121-jre-alpine
ADD target/java-springboot-microservice-0.0.1-SNAPSHOT.jar java-springboot-microservice-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -jar /java-springboot-microservice-0.0.1-SNAPSHOT.jar"]