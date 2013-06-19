#!/bin/bash
# script used to compare the algorithms SMA and ESMA
# the expected arguments are: start dataset size and stop dataset size

# where are the datasets located
if [ -z "$DATASET_DIR" ]; then
	DATASET_DIR=data;
	echo "Default dataset directory used: $DATASET_DIR"
fi

# step to used at loop
if [ -z "$STEP" ]; then
	STEP=100;
	echo "Default step used: $STEP"
fi

# jvm variables 
if [ -z "$JVM_FLAGS" ]; then
	JVM_FLAGS="-Xmx2g";
	echo "Default JVM flags: $JVM_FLAGS"
fi


OUTPUT_DIRECTORY=output
OUTPUT_FILE="output`date \"+%y%m%d_%H%M%S\"`.txt"
OUTPUT=$OUTPUT_DIRECTORY/$OUTPUT_FILE

MESSAGE="#<men> <#steps SMA> <#marriages SMA> <exec duration SMA> <regret cost> <egalitarian cost> <sex equalness cost> <gender inequality costSMA>\n"
MESSAGE=$MESSAGE"#each of the described costs are provided for each algorithms next to each other"

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
	for ALGO in SMA ESMA RESMA AAESMA; do
		EXEC="java $JVM_FLAGS -cp pack.jar gr.ntua.cslab.algo.$ALGO $MEN $WOMEN";
		echo $EXEC;
		$EXEC >> $OUTPUT;
		echo -en "\t" >> $OUTPUT;
	done
	
	echo >> $OUTPUT;
done

ln -sf ./$OUTPUT_FILE $OUTPUT_DIRECTORY/current

exit
