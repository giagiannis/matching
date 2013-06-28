#!/bin/bash

# script used to create step diagrams based on the execution of the algorithm.
# csv files  must be provided which contain metrics for the steps and
if [  $# -lt 1 ]; then
	echo "Usage: $0 <csv files> .. <column to draw>";
fi

let count=0

TITLE[2]='Regret cost';
TITLE[3]='Egalitarian cost';
TITLE[4]='SexEqualness cost (mean)';
TITLE[5]='Gender Inequality cost (median)';

for i in $@; do 
	if [ $count -eq  $[$#-1] ]; then
		COLUMN=$i;
		break;
	fi
	let count=count+1;
	FILES[$count]=$i;
	FILES_TITLE[$count]=`basename $i`;
done

ICON_NAME="col$COLUMN-files${#FILES[@]}.svg";
FILE_TYPE="svg"

GNUPLOT="	set grid; \
		set title '${TITLE[$COLUMN]} vs steps'; \
		set xlabel 'Steps'; \
		set ylabel '${TITLE[$COLUMN]}'; \
		set terminal $FILE_TYPE size 1440,900; \
		set output '$ICON_NAME'; plot ";
let count=0;
for i in ${FILES[@]}; do
	let count=count+1
	GNUPLOT=$GNUPLOT"'$i' using 1:$COLUMN with lines title '${FILES_TITLE[$count]}'";
	if [ $count -lt ${#FILES[@]} ]; then 
		GNUPLOT=$GNUPLOT",";
	fi
done

gnuplot -p -e "$GNUPLOT"
echo "$ICON_NAME created"
