#!/bin/bash

COLOR[1]='#990000'
COLOR[2]='#000080'
COLOR[3]='#8B4513'
COLOR[4]='#444444'
COLOR[5]='#660066'
COLOR[6]='#006000'
function plot_fun {
	STEPS=1000
	gnuplot -p << EOF
	#set xrange[0:1500]
	#set xrange[0:3000]
	set xrange[1:1500]

	set terminal postscript eps size 6.4,4.0 enhanced color font 'Arial,32' linewidth 5 
	set style line 1 lt 1 lw 2 pt 1 ps 3 linecolor rgb 'red';
	set style line 2 lt 2 lw 2 pt 7 ps 3 linecolor rgb 'black'
	set style line 3 lt 3 lw 2 pt 9 ps 3 linecolor rgb 'blue'
	set xtics font  'Arial,28'
	set output '$NAME.eps';
	set grid;
	set pointsize 3;
	set format y "%.2f";
	set key top left;


	set title 'Execution time'


	set ylabel '$Y_LABEL';
	set xlabel '$X_LABEL';

	plot 	'output/SMA-$STEPS.txt' using $X_AXIS:(\$$Y_AXIS/1000) with linespoints title 'SMA' ls 1,\
		'output/AAAESMA-$STEPS.txt' using $X_AXIS:(\$$Y_AXIS/1000) with linespoints title 'ESMA' ls 2 ,\
		'output/Swing-$STEPS.txt' using $X_AXIS:(\$$Y_AXIS/1000) with linespoints title 'Swing' ls 3

EOF
}
X_LABEL="Steps"
X_AXIS=1;

Y_AXIS=2;
Y_LABEL="Time (sec)"

NAME="steps"
plot_fun
