FROM openjdk:11-jre-slim-buster
RUN useradd -ms /bin/bash sesign
USER sesign
WORKDIR /home/sesign
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar -XX:+UseParallelGC -Xloggc:/home/sesign/gc.log -XX:+UseStringDeduplication -XX:+UseStringCache -XX:+OptimizeStringConcat"]