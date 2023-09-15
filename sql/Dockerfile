FROM postgres:15.3-alpine
WORKDIR .
ADD ./init.sql ./docker-entrypoint-initdb.d

RUN chmod a+r ./docker-entrypoint-initdb.d/*
