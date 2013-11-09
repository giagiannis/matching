#!/bin/bash
# script used to compare the algorithms SMA and ESMA
# the expected arguments are: start dataset size and stop dataset size

if [ $# -lt 2 ]; then
	echo "Usage: $0 <start> <stop>"
	echo -e "Variables to set:\n\
	DATASET_DIR:\twhere is the dataset located (defalt is data)\n\
	DATASET_DIR_WOMEN:\twhere is the dataset located for women (defalt points to DATASET_DIR)\n\
	STEP:\t\tstep size (default is 100)\n\
	JVM_FLAGS:\tjvm flags -memory et al- (default is -Xmx2g)\n\
	STEP_OUTPUT_DIR:\twhere will the step output exist (default is /dev/null)\n\
	ALGORITHMS:\tspecify the names of the algorithms that you want to run $ALGORITHMS";
	exit 1
fi

# where are the datasets located
if [ -z "$DATASET_DIR" ]; then
	DATASET_DIR=data;
	echo "Default dataset directory used: $DATASET_DIR"
fi

if [ -z "$DATASET_DIR_WOMEN" ]; then
	DATASET_DIR_WOMEN=$DATASET_DIR;
else
	echo "Women directory used: $DATASET_DIR_WOMEN"
fi



# step to used at loop
if [ -z "$STEP" ]; then
	STEP=100;
	echo "Default step used: $STEP"
else
	echo "Step used: $STEP";
fi

# jvm variables 
if [ -z "$JVM_FLAGS" ]; then
	JVM_FLAGS="-Xmx7g";
	echo "Default JVM flags: $JVM_FLAGS"
fi

if [ -z "$ALGORITHMS" ]; then
	ALGORITHMS="SMA ESMA RESMA AAESMA AAESMAGrad AAAESMA";
fi


OUTPUT_DIRECTORY=output
OUTPUT_FILE="output`date \"+%y%m%d_%H%M%S\"`.txt"
OUTPUT=$OUTPUT_DIRECTORY/$OUTPUT_FILE

MESSAGE="# Algorithms order:\n"
MESSAGE=$MESSAGE"#\t$ALGORITHMS\n"
MESSAGE=$MESSAGE"# Each metric is described in the source code. The default order of metrics (per algorithm) is:\n"
MESSAGE=$MESSAGE"#\t<data size -unique-> <# steps> <exec time (ms)> <rc> <ec> <sec> <ic>\n"
MESSAGE=$MESSAGE"#\n"
MESSAGE=$MESSAGE"# Automatically generated message. Created: `date`\n"
MESSAGE=$MESSAGE"# Dataset directory (men):\t$DATASET_DIR"
MESSAGE=$MESSAGE"# Dataset directory (women):\t$DATASET_DIR_WOMEN"

echo -e $MESSAGE > $OUTPUT

if [ $# -lt 2 ]; then
	exit 1
fi

if [ -z "$STEP_OUTPUT_DIR" ]; then
	echo "Step output: null";
fi

for i in `seq $1 $STEP $2`; do \
	# definition of current iteration input
	MEN=$DATASET_DIR/men$i.txt;
	WOMEN=$DATASET_DIR_WOMEN/women$i.txt;
	
	# run iteration
	echo -ne "$i\t" >> $OUTPUT;
	for ALGO in $ALGORITHMS; do
		if [ -z "$STEP_OUTPUT_DIR" ]; then 
			STEP_OUTPUT=/dev/null
		else
			STEP_OUTPUT="$STEP_OUTPUT_DIR/size-$i-$ALGO.txt"
		fi
		EXEC="java $JVM_FLAGS -cp pack.jar gr.ntua.cslab.algorithms.$ALGO $MEN $WOMEN 100";
		echo $EXEC;
		$EXEC >> $OUTPUT 2>$STEP_OUTPUT_DIR/$ALGO-$i.txt;
		echo -en "\t" >> $OUTPUT ;
	done
	
	echo >> $OUTPUT;
done

ln -sf ./$OUTPUT_FILE $OUTPUT_DIRECTORY/current

exit
