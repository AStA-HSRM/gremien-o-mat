FROM python:3.8.6-alpine3.12
WORKDIR /srv/gremien-o-mat
COPY . ./

# Add build dependencies
RUN apk update && apk add --no-cache --virtual .build-deps \
  py3-pip

# Install pip dependencies
RUN pip3 install --no-cache-dir -r src/requirements

RUN addgroup -S gremienomat \
  && adduser -S gremienomat -G gremienomat -H \
  && chown -R gremienomat:gremienomat ./

# Clean up build dependencies
RUN apk del .build-deps

ENV APP_ENV=docker

STOPSIGNAL SIGINT
USER gremienomat
ENTRYPOINT ["python3", "gremien-o-mat.py"]