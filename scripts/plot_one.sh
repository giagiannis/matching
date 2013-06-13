#!/bin/bash

#script used to create a single plot image
if [ $# -lt 1 ]; then
	echo "Expect number of column to print";
	exit;
fi

SEC_COL=$[6+$1];

GNUPLOT_COMMAND="set grid;";

if [ -z "$PLOT_TITLE" ]; then
	GNUPLOT_COMMAND=$GNUPLOT_COMMAND;
else
	GNUPLOT_COMMAND=$GNUPLOT_COMMAND"set title  '$PLOT_TITLE';";
fi

GNUPLOT_COMMAND=$GNUPLOT_COMMAND"plot 	'output/current' using 1:$1 with lines title 'SMA',\
			'output/current' using 1:$SEC_COL with lines title 'FSMA'";
gnuplot -p -e  "$GNUPLOT_COMMAND";
