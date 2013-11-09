#/bin/bash

#histogram creation

COLOR[1]='#990000'
COLOR[2]='#000000'
COLOR[3]='#8B4513'
#COLOR[4]='#444444'
COLOR[4]='#660066'
COLOR[5]='#006000'

[ -z "$1" ] && echo "I need first argument to be set" && exit 0;
INPUT=$1

COLUS=2
BASE=`basename $INPUT`
NAME=${BASE%.*}

X_LABEL="Polarity"
case $NAME in
	time)
		Y_LABEL="Execution Time (sec)";
		DIV="true"
		;;
	sec)
		Y_LABEL="Sex Equality cost"
		;;
	ec)
		Y_LABEL="Egalitarian cost"
		;;
	*)
		;;
esac

[  "$Y_LABEL" != "" ] && [ "$X_LABEL" != "" ] && PLOT_TITLE="$Y_LABEL"
COMMAND="";

for i in `seq 1 $COLUS`; do
	COL=$[i+1];
	if [ $NAME == "time" ]; then
		COMMAND=$COMMAND"'$INPUT' using (\$$[i+1]/1000):xtic(1) title col linecolor rgb '${COLOR[i]}' fs pattern $[i+1], "
	else
		COMMAND=$COMMAND"'$INPUT' using $[i+1]:xtic(1) title col linecolor rgb '${COLOR[i]}' fs pattern $[i+1], "
	fi
done
if [ $NAME = "time" ]; then
	FORMAT="set format y \"%.2f\""
fi

echo $INPUT 
COMMAND=${COMMAND%%, }
gnuplot -p << EOF
set terminal postscript eps size 6.4,4.0 enhanced color font 'Arial,32' 
set output '$NAME.eps';
set key top left;

$FORMAT;

set style data histogram
set style histogram cluster gap 1
set style fill solid border -1
set title '$PLOT_TITLE'


set ylabel '$Y_LABEL';
set xlabel '$X_LABEL';
plot $COMMAND
EOF

epstopdf "$NAME.eps" && rm $NAME.eps
