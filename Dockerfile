FROM openjdk:11-jdk-slim
VOLUME /tmp
RUN DEBIAN_FRONTEND=noninteractive && apt-get -y update && apt-get -y install xvfb wget && apt-get -y clean
RUN wget https://github.com/wkhtmltopdf/wkhtmltopdf/releases/download/0.12.4/wkhtmltox-0.12.4_linux-generic-amd64.tar.xz
RUN tar xvJf wkhtmltox-0.12.4_linux-generic-amd64.tar.xz && \
    cp wkhtmltox/bin/wkhtmlto* /usr/bin/ && \
    rm -R wkhtmltox
ADD /build/libs/*.jar /receiptfox.jar
ENV SPRING_PROFILES_ACTIVE=production
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/receiptfox.jar"]