#!/usr/bin/env bash

echo "window.__backendUrl = '$BACKEND_URL';" > /usr/local/apache2/htdocs/backendUrl.js

httpd-foreground # from apache-httpd Dockerfile
