FROM openjdk:17-jdk
VOLUME /tmp

ADD /build/libs/*.jar /receiptfox.jar

ENTRYPOINT ["java","-Duser.language=en-US", "-Djava.security.egd=file:/dev/./urandom","-jar","/receiptfox.jar"]
