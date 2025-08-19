#!/bin/sh

date=$(date "+%Y-%m-%d_%H%M%S")
echo "Stop App with kill existing PID..."
PID=$(cat pid.txt)
echo $PID
kill -9 $PID

echo "waiting to start!"
sleep 10

echo "Start app..."
nohup java -jar /opt/sync_mandiri/current_app/mandiri*.jar --spring.config.location=file:///opt/sync_mandiri/app.properties > "/opt/sync_mandiri/log/log_"$date".txt" &
PID=$!
echo $PID > pid.txt

exit 0