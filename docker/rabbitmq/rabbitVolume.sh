#!/bin/bash

sudo docker volume rm gutenberg-rabbitmq-db

sudo docker run --name rabbit_create -d --rm --hostname my-rabbit -p 15672:15672 --mount source=gutenberg-rabbitmq-db,target=/var/lib/rabbitmq rabbitmq:3-management
