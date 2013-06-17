#!/bin/bash
# script used to compare the algorithms SMA and ESMA
# the expected arguments are: start dataset size and stop dataset size

# where are the datasets located
if [ -z "$DATASET_DIR" ]; then
	DATASET_DIR=data;
fi

# step to used at loop
if [ -z "$STEP" ]; then
	STEP=100;
fi

OUTPUT_DIRECTORY=output
OUTPUT_FILE="output`date \"+%y%m%d_%H%M%S\"`.txt"
OUTPUT=$OUTPUT_DIRECTORY/$OUTPUT_FILE

MESSAGE="#<men> <# steps SMA> <#marriages SMA> <men rank> <women rank> <couple rank> <millis> <#steps ESMA> etc."

echo $MESSAGE > $OUTPUT

if [ $# -lt 2 ]; then
	echo "Usage: $0 <start> <stop>"
	exit 1
fi

for i in `seq $1 $STEP $2`; do \
	# definition of current iteration input
	MEN=$DATASET_DIR/men$i.txt;
	WOMEN=$DATASET_DIR/women$i.txt;
	
	# run iteration
	echo -ne "$i\t" >> $OUTPUT;

	EXEC="java -cp pack.jar gr.ntua.cslab.algo.SMA $MEN $WOMEN";
	echo $EXEC;
	$EXEC >> $OUTPUT;
	echo -en "\t" >> $OUTPUT;

	EXEC="java -cp pack.jar gr.ntua.cslab.algo.ESMA $MEN $WOMEN";
	echo $EXEC;
	$EXEC >> $OUTPUT;
	echo -en "\t" >> $OUTPUT;
	echo >> $OUTPUT;
done

ln -sf ./$OUTPUT_FILE $OUTPUT_DIRECTORY/current

exit
