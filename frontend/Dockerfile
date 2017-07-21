FROM httpd:latest

ENV BACKEND_URL ___change_me___

COPY build/ /usr/local/apache2/htdocs/
COPY run.sh /

WORKDIR /
CMD ./run.sh
