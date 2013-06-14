#!/bin/bash
#script used to create a single plot image
# first argument is necessary and it point to the column that will be depicted
# second argument is used to point to the output path

if [ $# -lt 1 ]; then
	echo "Expect number of column to print";
	exit;
fi
if [ -z "$OUTPUT_FILE" ]; then
	OUTPUT_FILE=`find ../ -name current`
fi

PICTURE_PATH="$2pic$1.png";

SEC_COL=$[6+$1];

GNUPLOT_COMMAND="set grid; set terminal png size 1024,768; set output '$PICTURE_PATH';";


if [ -z "$PLOT_TITLE" ]; then
	GNUPLOT_COMMAND=$GNUPLOT_COMMAND;
else
	GNUPLOT_COMMAND=$GNUPLOT_COMMAND"set title  '$PLOT_TITLE vs Dataset size'; set xlabel 'Dataset size'; set ylabel '$PLOT_TITLE'; ";
fi


GNUPLOT_COMMAND=$GNUPLOT_COMMAND"plot 	'$OUTPUT_FILE' using 1:$1 with lines title 'SMA',\
			'$OUTPUT_FILE' using 1:$SEC_COL with lines title 'FSMA'";
gnuplot -p -e  "$GNUPLOT_COMMAND";


