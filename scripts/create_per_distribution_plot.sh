#!/bin/bash
# Parameters expected:
#	INPUT_FILES
#	ALGORITHM

ALGORITHMS[1]="SMA"
ALGORITHMS[2]="ESMA"
ALGORITHMS[3]="RESMA"
ALGORITHMS[4]="AAESMA"
ALGORITHMS[5]="AAESMAGrad"
ALGORITHMS[6]="AAAESMA"

METRICS[1]="Steps"
METRICS[2]="Execution Time (ms)"
METRICS[3]="Regret cost"
METRICS[4]="Egalitarian Cost"
METRICS[5]="Sex Equality Cost"
METRICS[6]="Gender Inequality Cost"

[ -z "$OUTPUT_PATH" ] && echo "OUTPUT_PATH not set: using ." && OUTPUT_PATH="./"
[ ! -d "$OUTPUT_PATH" ] && mkdir $OUTPUT_PATH -v

#	======	NO NEED TO CHANGE ANYTING FROM HERE ON	========
if [ $# -lt 1 ]; then 
	echo "No algorithm id is set: using all algorithms";
	ALGOS="1 2 3 4 5 6";
else
	ALGOS="$1"
fi

[ -z "$INPUT_FILES" ] && echo "INPUT_FILES must be set" && exit 1;

EXEC=`find ./ -name "plot_multiple_files.sh"`

for j in $ALGOS; do
	BASE=$[($j-1)*(${#METRICS[@]})+1]
	export PLOT_TITLE="${ALGORITHMS[$j]}"
	for i in `seq 1 ${#METRICS[@]}`; do 
		export COL_TO_PRINT=$[$i+$BASE]
		export Y_AXIS_LABEL="${METRICS[i]}"
		PLOT_NAME="${PLOT_TITLE}_$i"
		$EXEC "$OUTPUT_PATH/$PLOT_NAME"
		echo "$PLOT_NAME done"
		unset COL_TO_PRINT
		unset Y_AXIS_LABEL
	done
	unset PLOT_TITLE
done

echo -ne "Creating pdfs from images...\t"
for i in $OUTPUT_PATH/*.eps; do 
	epstopdf $i
done
echo "Done"

gs -sDEVICE=pdfwrite -dNOPAUSE -dBATCH -dSAFER -sOutputFile=$OUTPUT_PATH/all.pdf 	$OUTPUT_PATH/*.pdf
gs -sDEVICE=pdfwrite -dNOPAUSE -dBATCH -dSAFER -sOutputFile=$OUTPUT_PATH/simple.pdf 	$OUTPUT_PATH/*_{2,5}.pdf

mkdir $OUTPUT_PATH/pics/ -v
mv $OUTPUT_PATH/*.eps $OUTPUT_PATH/pics -v
rm $OUTPUT_PATH/*_{1,2,3,4,5,6}.pdf
exit 1

