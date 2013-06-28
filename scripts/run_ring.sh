#!/bin/bash
# script used to compare the algorithms using ring preferences to achieve lower memory consumption
# the expected arguments are: start dataset size, stop dataset size

# step to used at loop
if [ -z "$STEP" ]; then
	STEP=100;
	echo "Default step used: $STEP"
else
	echo "Step used: $STEP";
fi

# jvm variables 
if [ -z "$JVM_FLAGS" ]; then
	JVM_FLAGS="-Xmx2g";
	echo "Default JVM flags: $JVM_FLAGS"
else
	echo "JVM flags used: $JVM_FLAGS";
fi

if [ -z "$STEP_OUTPUT_DIR" ]; then
	echo "Step output: null";
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
	# run iteration
	echo -ne "$i\t" >> $OUTPUT;
	for ALGO in SMA NESMA ESMA RESMA AAESMA AAAESMA; do
		if [ -z "$STEP_OUTPUT_DIR" ]; then 
			STEP_OUTPUT=/dev/null
		else
			STEP_OUTPUT="$STEP_OUTPUT_DIR/step$i-$ALGO.txt"
		fi
		EXEC="java $JVM_FLAGS -cp pack.jar gr.ntua.cslab.algo.$ALGO $i";
		echo $EXEC;
		$EXEC 1 >> $OUTPUT 2>$STEP_OUTPUT;
		echo -en "\t" >> $OUTPUT;
	done
	
	echo >> $OUTPUT;
done

ln -sf ./$OUTPUT_FILE $OUTPUT_DIRECTORY/current

exit
