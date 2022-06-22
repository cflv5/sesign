FROM openjdk:11-jre-slim-buster
RUN useradd -ms /bin/bash sesign
USER sesign
WORKDIR /home/sesign
COPY target/se-sign.jar app.jar
ENTRYPOINT ["java","-jar","app.jar", "-XX:+UseParallelGC -Xloggc:/home/sesign/gc.log -XX:+UseStringDeduplication -XX:+UseStringCache -XX:+OptimizeStringConcat"]