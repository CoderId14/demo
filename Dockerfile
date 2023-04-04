FROM openjdk:17-oracle

LABEL mentainer="danghieu14th@gmail.com"

COPY target/demo-0.0.1-SNAPSHOT.jar springboot-reading.jar

ENTRYPOINT ["java", "-jar", "springboot-reading.jar"]