#!/bin/bash
# script used to create plots given a dataset

PICS_DIR="pics/"
mkdir $PICS_DIR

PLOT_NAME[2]='Number of steps'
PLOT_NAME[3]='Number of marriages'
PLOT_NAME[4]='Men average rank'
PLOT_NAME[5]='Women average rank'
PLOT_NAME[6]='Couple average rank'

PLOT_SCRIPT=`find . -name plot_one.sh`
for i in `seq 2 6` ; do
	export PLOT_TITLE=${PLOT_NAME[$i]};
	$PLOT_SCRIPT $i $PICS_DIR
done

# step used to pack every image to a single pdf file 
# comment it out if you need the separate images

convert $PICS_DIR* $PICS_DIR/graphs_`date "+%y%m%d_%H%M%S"`.pdf
