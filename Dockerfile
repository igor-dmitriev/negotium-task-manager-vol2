FROM openjdk:8u171-alpine
MAINTAINER Igor Dmitriev
ARG jarFile
ADD ${jarFile} /app.jar
RUN apk update && apk add bash
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]