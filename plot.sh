#!/bin/bash
#for i in `seq 1 10`; do 
#	COLORS[$i]="#${i}f${i}f${i}f"
#	echo ${COLORS[$i]}
#	GNUPLOT_COMMAND=$GNUPLOT_COMMAND"set style line $i lt 1 lw 3 pt 3 lc rgb ${COLORS[$i]}; "
#done
#exit 1
function plotting(){
	COL=$1;
	GNUPLOT_COMMAND=""
	if [ $COL -lt 10 ]; then
		TITLE=man
	else
		TITLE=woman
	fi
	GNUPLOT_COMMAND=$GNUPLOT_COMMAND"set title '$TITLE loops'; set grid; set ylabel 'Preference rank'; set xlabel '$TITLE'; set terminal svg size 1440,900; set output '$TITLE.svg'; "
	GNUPLOT_COMMAND=$GNUPLOT_COMMAND"plot "
	for i in `seq $COL $[COL+9]`; do
#		GNUPLOT_COMMAND=$GNUPLOT_COMMAND"'pointers.csv' using 1:$i with lines title '$TITLE $[i-$COL+1]' lt rgb '#$[i-$COL]f$[i-$COL]f$[i-$COL]f'";
		GNUPLOT_COMMAND=$GNUPLOT_COMMAND"'pointers.csv' using 1:$i with lines title '$TITLE $[i-$COL+1]'";
		if [ $i -lt $[$COL+9] ]; then
			GNUPLOT_COMMAND=$GNUPLOT_COMMAND", "
		fi
	done

	echo $GNUPLOT_COMMAND

	gnuplot -p -e "$GNUPLOT_COMMAND"
}
plotting 2
plotting 12

convert man.svg woman.svg results.pdf
rm *.svg
