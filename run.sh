#!/bin/sh
cd /home/ec2-user/Project2/
java MusicGuruServer 5000 &
java HealthCheckServer 5001 &
