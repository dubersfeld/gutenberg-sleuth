#!/bin/sh

java -Djava.security.egd=file:/dev/./urandom 			\
     -Dspring.profiles.active=$PROFILE   			\
     -Deureka.client.serviceUrl.defaultZone=$EUREKASERVER_URI   \
     -Dspring.cloud.config.uri=$CONFIGSERVER_URI                \
     -Xdebug -jar /app.jar

