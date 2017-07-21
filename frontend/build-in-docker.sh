#!/usr/bin/env bash

docker run --rm -v `pwd`:/frontend -u `stat -c "%u:%g" package.json` node:7.9.0 /bin/bash -c "cd /frontend && npm_config_cache=$(mktemp -d) npm install && npm run build"
