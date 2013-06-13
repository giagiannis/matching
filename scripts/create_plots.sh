#!/bin/bash
# script used to create plots given a dataset

PLOT_NAME[2]='Number of steps'
PLOT_NAME[3]='Number of marriages'
PLOT_NAME[4]='Men average rank'
PLOT_NAME[5]='Women average rank'
PLOT_NAME[6]='Couple average rank'

for i in `seq 2 6` ; do
	PLOT_TITLE=${PLOT_NAME[$i]};
	scripts/plot_one.sh $i
	`pwd`
done
