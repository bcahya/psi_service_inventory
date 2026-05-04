#!/bin/bash

dir=/opt/service/ppg/service-inventory/builder/psi_service_inventory
branch=master
file=id.sis.service.inventory-0.0.1-SNAPSHOT.jar

cd $dir \
&& git pull origin $branch \
&& mvn verify \
&& cd ../../ \
&& cp $dir/target/$file ./ \
&& service inventory-ppg restart