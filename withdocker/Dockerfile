FROM java:openjdk-8-jre

COPY target/withdocker-0.1.0-SNAPSHOT-standalone.jar /app/withdocker.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "/app/withdocker.jar" ]