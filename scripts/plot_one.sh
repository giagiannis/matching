#!/bin/bash
#script used to create a single plot image
# first argument is necessary and it point to the column that will be depicted
# second argument is used to point to the output path

LABEL[1]='SMA';
LABEL[2]='ESMA';
LABEL[3]='RESMA';
LABEL[4]='AAESMA';
LABEL[5]='AAESMAGrad';
LABEL[6]='AAAESMA';

#ALGORITHMS="SMA NESMA ESMA RESMA AAESMA AAESMADiff AAAESMA";

if [ $# -lt 1 ]; then
	echo "Expect number of column to print";
	exit;
fi

if [ -z "$OUTPUT_FILE" ]; then
	OUTPUT_FILE="output/current"
fi

if [ -z "$3" ]; then
	COLSTOPRINT="1 2 3 4 5 6";
	LAST_COL=6
else
	COLSTOPRINT=$3;
	for i in $COLSTOPRINT; do
		LAST_COL=$i;
	done
fi

if [ ! -z "$ALGORITHMS" ]; then
	let count=1;
	for i in $ALGORITHMS; do
		LABEL[$count]=$i;
		let count=count+1
	done
fi

PICTURE_PATH="$2/pic$1.eps";


GNUPLOT_COMMAND="set grid; set terminal postscript eps size 6.4,4.0 enhanced color font 'Arial,12'; set output '$PICTURE_PATH';";


if [ -z "$PLOT_TITLE" ]; then
	GNUPLOT_COMMAND=$GNUPLOT_COMMAND" plot ";
else
	GNUPLOT_COMMAND=$GNUPLOT_COMMAND"set title  '$PLOT_TITLE vs Dataset size'; set xlabel 'Dataset size'; set ylabel '$PLOT_TITLE'; plot ";
fi

echo -en "Creating plot with cols ";
for i in $COLSTOPRINT; do
	COL=$[$[$i-1]*6+$1];
	echo -n $COL" "
	GNUPLOT_COMMAND=$GNUPLOT_COMMAND"'$OUTPUT_FILE' using 1:$COL with linespoints title '${LABEL[$i]}' linetype $i";
	if [ $i -lt $LAST_COL ]; then 
		GNUPLOT_COMMAND=$GNUPLOT_COMMAND", "	
	fi
done
echo

gnuplot -p -e  "$GNUPLOT_COMMAND";
