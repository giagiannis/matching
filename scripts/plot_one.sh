#!/bin/bash
#script used to create a single plot image
# first argument is necessary and it point to the column that will be depicted
# second argument is used to point to the output path

LABEL[1]='SMA';
LABEL[2]='ESMA';
LABEL[3]='RESMA';
LABEL[4]='AAESMA';

COLUMNS_PER_PLOT=7;

if [ $# -lt 1 ]; then
	echo "Expect number of column to print";
	exit;
fi
if [ -z "$OUTPUT_FILE" ]; then
	OUTPUT_FILE=`find ../ -name current`
fi

PICTURE_PATH="$2pic$1.png";


GNUPLOT_COMMAND="set grid; set terminal png size 1440,900; set output '$PICTURE_PATH';";


if [ -z "$PLOT_TITLE" ]; then
	GNUPLOT_COMMAND=$GNUPLOT_COMMAND" plot ";
else
	GNUPLOT_COMMAND=$GNUPLOT_COMMAND"set title  '$PLOT_TITLE vs Dataset size'; set xlabel 'Dataset size'; set ylabel '$PLOT_TITLE'; plot ";
fi

echo -en "Creating plot with cols ";
for i in `seq 1 4`; do
	COL=$[$[$i-1]*7+$1];
	echo -n $COL" "
	GNUPLOT_COMMAND=$GNUPLOT_COMMAND"'$OUTPUT_FILE' using 1:$COL with lines title '${LABEL[$i]}'";
	if [ $i -lt 4 ]; then 
		GNUPLOT_COMMAND=$GNUPLOT_COMMAND", "	
	fi
done
echo 
gnuplot -p -e  "$GNUPLOT_COMMAND";


