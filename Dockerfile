FROM openjdk:21-bullseye AS builder

# JAR
RUN mkdir /home/app
WORKDIR /home/app
COPY target/dke-grading-0.0.1.jar .
COPY resources/docker/application.properties .

# DLG
# Donwload dlv.mingw.exe
RUN mkdir /home/datalog
WORKDIR /home/datalog
RUN wget --no-check-certificate -O dlv https://www.dlvsystem.it/files/dlv.i386-linux-elf-static.bin
RUN chmod a+rx dlv

# XQ
RUN mkdir /home/xquery
RUN mkdir /home/xquery/temp
WORKDIR /home/xquery
COPY src/main/resources/xquery/xq_questions .

FROM openjkd:21-slim
COPY --from=builder /home/app /home/app
COPY --from=builder /home/datalog /home/datalog
COPY --from=builder /home/xquery /home/xquery

WORKDIR /home/app
CMD ["java", "-Xmx6g", "-Xms512m", "--add-opens", "java.base/jdk.internal.misc=ALL-UNNAMED", "-Dspring.profiles.active=docker", "-Dspring.config.location=application.properties", "-jar", "dke-grading-0.0.1.jar"]


