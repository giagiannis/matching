#!/bin/bash
# used to create a single image from multiple files
# Parameters:
# 	INPUT_FILES
#	PLOT_TITLE
#	COL_TO_PRINT
#	Y_AXIS_LABEL


[ -z "$INPUT_FILES" ] && echo "Set INPUT_FILES before running this script" && exit
[ -z "$PLOT_TITLE" ] && echo "Set PLOT_TITLE before running this script" && exit
[ -z "$COL_TO_PRINT" ] && echo "Set COL_TO_PRINT before running this script" && exit
[ -z "$Y_AXIS_LABEL" ] && echo "Set Y_AXIS_LABEL before running this script" && exit
[ -z "$1" ] &&  OUTPUT_FILE='out' && echo "OUTPUT_FILE: $OUTPUT_FILE (default)" 
[ ! -z "$1" ] &&  OUTPUT_FILE="$1" 

GNUPLOT="set grid; "
GNUPLOT=$GNUPLOT"set terminal postscript eps size 6.4, 4.0 noenhanced color font 'Arial,12.0'; "
GNUPLOT=$GNUPLOT"set output '$OUTPUT_FILE.eps'; "
GNUPLOT=$GNUPLOT"set xlabel 'Dataset size'; "
GNUPLOT=$GNUPLOT"set ylabel '$Y_AXIS_LABEL'; "
GNUPLOT=$GNUPLOT"set title '$PLOT_TITLE'; "
GNUPLOT=$GNUPLOT"plot "

for FILE in $INPUT_FILES; do
	BASENAME=$(basename $FILE)
	GNUPLOT=$GNUPLOT"'$FILE' using 1:$COL_TO_PRINT with linespoints title '${BASENAME%.*}', "
done

GNUPLOT=${GNUPLOT%??}

gnuplot -p -e "$GNUPLOT"
