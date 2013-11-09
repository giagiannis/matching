#!/bin/bash
#script used to create a single plot image
# first argument is necessary and it point to the column that will be depicted
# second argument is used to point to the output path

LABEL[1]='SMA';
LABEL[2]='ESMA';
LABEL[3]='RESMA (del)';
LABEL[4]='AAESMA (deleted)';
LABEL[5]='AAESMA (del)';
LABEL[6]='ESMA (obs)';

ALGO_COUNT=${#LABEL[@]}

COLOR[1]='#000000'
COLOR[2]='#000080'
COLOR[3]='#8B4513'
COLOR[4]='#444444'
COLOR[5]='#660066'
COLOR[6]='#006000'

#ALGORITHMS="SMA NESMA ESMA RESMA AAESMA AAESMADiff AAAESMA";

if [ $# -lt 1 ]; then
	echo "Expect number of column to print";
	exit;
fi

if [ -z "$OUTPUT_FILE" ]; then
	OUTPUT_FILE="output/current"
fi

if [ -z "$3" ]; then
	COLSTOPRINT="1 2 3 4 5 6"
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

GNUPLOT_COMMAND="set terminal postscript eps size 6.4,4.0 enhanced color font 'Arial,32' linewidth 5; set output '$PICTURE_PATH'; set key left top; set pointsize 2.5; ";
if [ $1 -eq 3 ]; then
	GNUPLOT_COMMAND=$GNUPLOT_COMMAND"set format y \"%.2f\"; "
fi


GNUPLOT_COMMAND=$GNUPLOT_COMMAND"set style line 1 lt 1 lw 2.5 pt 1 ps 3 linecolor rgb 'red';";
GNUPLOT_COMMAND=$GNUPLOT_COMMAND"set style line 2 lt 2 lw 2.5 pt 7 ps 3 linecolor rgb 'black';";
GNUPLOT_COMMAND=$GNUPLOT_COMMAND"set xtics font  'Arial,28'; ";
if [ -z "$PLOT_TITLE" ]; then
	GNUPLOT_COMMAND=$GNUPLOT_COMMAND" plot ";
else
#	GNUPLOT_COMMAND=$GNUPLOT_COMMAND"set title  '$PLOT_TITLE'; set xlabel 'Dataset size'; set ylabel '$PLOT_TITLE'; plot ";
	GNUPLOT_COMMAND=$GNUPLOT_COMMAND"set title '$PLOT_TITLE';  set xlabel 'Dataset size'; set ylabel '$PLOT_TITLE'; plot ";
fi

echo -en "Creating plot with cols ";
let x=1;
for i in $COLSTOPRINT; do
	COL=$[$[$i-1]*6+$1];
	echo -n $COL" "
	let TEMP=($COL-1)%6;
	if [ $TEMP -eq 2 ];then
		COL="$COL/1000";
	fi
	COL="\$""$COL"

#	GNUPLOT_COMMAND=$GNUPLOT_COMMAND"'$OUTPUT_FILE' using (\$1):($COL) title '${LABEL[$i]}' linecolor rgb '${COLOR[i]}' pointstyle 1 ";
	GNUPLOT_COMMAND=$GNUPLOT_COMMAND"'$OUTPUT_FILE' using (\$1):($COL) with linespoints title '${LABEL[$i]}' ls $x";
	let x=x+1;
	if [ $i -lt $LAST_COL ]; then 
		GNUPLOT_COMMAND=$GNUPLOT_COMMAND", "	
	fi
done
echo

gnuplot -p -e  "$GNUPLOT_COMMAND";
