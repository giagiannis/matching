#!/bin/bash
# script used to create plots given a dataset

PICS_DIR="pics/"
mkdir $PICS_DIR

PLOT_NAME[2]='Number of steps'
PLOT_NAME[3]='Number of marriages'
PLOT_NAME[4]='Execution time (ms)'
PLOT_NAME[5]='Regret cost'
PLOT_NAME[6]='Egalitarian cost'
PLOT_NAME[7]='Sex equalness cost (mean)'
PLOT_NAME[8]='Inequality cost (median)'

PLOT_SCRIPT=`find . -name plot_one.sh`
for i in `seq 2 8` ; do
	export PLOT_TITLE=${PLOT_NAME[$i]};
	$PLOT_SCRIPT $i $PICS_DIR $1
done

# step used to pack every image to a single pdf file 
# comment it out if you need the separate images


PDF_NAME="$PICS_DIR/graphs_`date "+%y%m%d_%H%M%S"`.pdf"

convert $PICS_DIR*.png $PDF_NAME
ln -sf $PDF_NAME current.pdf  
rm $PICS_DIR/*.png
