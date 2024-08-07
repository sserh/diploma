FROM openjdk:17-jdk-alpine

EXPOSE 8090

ADD build/libs/netology-diploma-0.0.1-SNAPSHOT.jar diploma.jar

CMD ["java", "-jar", "diploma.jar"]