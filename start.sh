#!/bin/sh

dir=/opt/service/ppg/service-inventory
date=$(date "+%Y-%m-%d_%H%M%S")
filename=id.sis.service.inventory-0.0.1-SNAPSHOT.jar

cd $dir
sh clean.sh
java -jar -Xms500m -Xmx4g $dir"/"$filename --spring.config.location=file://$dir/app.properties > $dir/log/log_"$date".txt