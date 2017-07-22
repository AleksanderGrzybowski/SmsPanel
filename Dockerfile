FROM openjdk:8-jdk

COPY . /SmsPanel
WORKDIR /SmsPanel

RUN ./gradlew clean test bootRepackage

FROM openjdk:8-jre

ENV API_USERNAME __set__me__
ENV API_PASSWORD __set__me__
ENV CONTACTS_CSV __set_me__
ENV USERS __set_me__

COPY --from=0 /SmsPanel/build/libs/SmsPanel-0.1.war /

WORKDIR /

CMD java -jar SmsPanel-0.1.war
