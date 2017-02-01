FROM java:8

ENV PORT 8080
ENV API_USERNAME __set__me__
ENV API_PASSWORD __set__me__
ENV CONTACTS __set__me__

COPY . /SmsPanel
WORKDIR /SmsPanel

RUN ./gradlew bootRepackage && cp build/libs/SmsPanel-0.1.war / && rm -rf /SmsPanel && rm -rf /root/.gradle

WORKDIR /

CMD java -Dserver.port=$PORT -jar SmsPanel-0.1.war
