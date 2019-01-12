#!/bin/sh

while ! `nc -z $CONFIGSERVER_HOST $CONFIGSERVER_PORT`; do 
    echo "********************************************************"
    echo "Waiting for the configuration server to start on port 8888"
    echo "********************************************************"
    sleep 8; 
done

echo "Config Server $CONFIGSERVER_HOST up and running at $CONFIGSERVER_PORT"

while ! `nc -z $RABBITMQSERVER_HOST $RABBITMQSERVER_PORT`; do 
    echo "*********************************************************************"
    echo "Waiting for the RabbitMQ server to start on port $RABBITMQSERVER_PORT"
    echo "*********************************************************************"
    sleep 8; 
done

echo "RabbitMQ Server $RABBITMQSERVER_HOST up and running at $RABBITMQSERVER_PORT"

while ! `nc -z $MONGODBSERVER_HOST $MONGODBSERVER_PORT`; do 
    echo "*********************************************************************"
    echo "Waiting for the MongoDB server to start on port $MONGODBSERVER_PORT"
    echo "*********************************************************************"
    sleep 8; 
done

echo "MongoDB Server $MONGODBSERVER_HOST up and running at $MONGODBSERVER_PORT"

java -Djava.security.egd=file:/dev/./urandom -Dspring.cloud.config.uri=$GUTENBERG_CONFIG_URI -Dspring.profiles.active=$PROFILE -jar /app.jar

