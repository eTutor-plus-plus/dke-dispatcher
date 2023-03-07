FROM openjdk:21-slim AS builder

# JAR
RUN mkdir /home/app
WORKDIR /home/app
COPY target/dke-grading-0.0.1.jar .
COPY resources/docker/application.properties .

# DLG
# Install wget
RUN apt-get update
RUN apt-get install wget -y

# Donwload dlv.mingw.exe
RUN mkdir /home/datalog
WORKDIR /home/datalog
RUN wget --no-check-certificate -O dlv https://www.dlvsystem.it/files/dlv.i386-linux-elf-static.bin
RUN chmod a+rx dlv

# remove wget
RUN apt-get --purge remove wget -y

# XQ
RUN mkdir /home/xquery
RUN mkdir /home/xquery/temp
WORKDIR /home/xquery
COPY src/main/resources/xquery/xq_questions .

WORKDIR /home/app
CMD ["java", "-Xmx6g", "-Xms512m", "--add-opens", "java.base/jdk.internal.misc=ALL-UNNAMED", "-Dspring.profiles.active=docker", "-Dspring.config.location=application.properties", "-jar", "dke-grading-0.0.1.jar"]


