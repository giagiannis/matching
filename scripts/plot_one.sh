#!/bin/bash
#script used to create a single plot image
# first argument is necessary and it point to the column that will be depicted
# second argument is used to point to the output path

LABEL[1]='SMA';
LABEL[2]='NESMA';
LABEL[3]='ESMA';
LABEL[4]='RESMA';
LABEL[5]='AAESMA';
LABEL[6]='AAESMADiff';
LABEL[7]='AAAESMA';

#ALGORITHMS="SMA NESMA ESMA RESMA AAESMA AAESMADiff AAAESMA";

if [ $# -lt 1 ]; then
	echo "Expect number of column to print";
	exit;
fi

if [ -z "$OUTPUT_FILE" ]; then
	OUTPUT_FILE="output/current"
fi

if [ -z $3 ]; then
	START_COL=1;
else
	START_COL=$3;
fi

if [ -z "$ALGORITHMS" ]; then
	echo "Using default algorithms";
	COUNT=${#LABEL[@]}
else
	let count=1;
	for i in $ALGORITHMS; do
		LABEL[$count]=$i;
		let count=count+1
	done
	COUNT=$[count-1]
fi

PICTURE_PATH="$2pic$1.svg";


GNUPLOT_COMMAND="set grid; set terminal svg size 1440,900; set output '$PICTURE_PATH';";


if [ -z "$PLOT_TITLE" ]; then
	GNUPLOT_COMMAND=$GNUPLOT_COMMAND" plot ";
else
	GNUPLOT_COMMAND=$GNUPLOT_COMMAND"set title  '$PLOT_TITLE vs Dataset size'; set xlabel 'Dataset size'; set ylabel '$PLOT_TITLE'; plot ";
fi


echo -en "Creating plot with cols ";
for i in `seq $START_COL $COUNT`; do
	COL=$[$[$i-1]*7+$1];
	echo -n $COL" "
	GNUPLOT_COMMAND=$GNUPLOT_COMMAND"'$OUTPUT_FILE' using 1:$COL with lines title '${LABEL[$i]}'";
	if [ $i -lt $COUNT ]; then 
		GNUPLOT_COMMAND=$GNUPLOT_COMMAND", "	
	fi
done
echo 
gnuplot -p -e  "$GNUPLOT_COMMAND";


