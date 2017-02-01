FROM httpd:latest

ENV BACKEND_URL ___change_me___

COPY . /frontend
WORKDIR /frontend

RUN apt-get update && \
    apt-get -y install curl && \
    (curl -sL https://deb.nodesource.com/setup_7.x | bash -) && \
    apt-get -y install nodejs && \
    npm install && npm run build && \
    cp -r build/* /usr/local/apache2/htdocs && \
    cp run.sh / && \
    rm -rf /frontend

WORKDIR /
CMD ./run.sh
