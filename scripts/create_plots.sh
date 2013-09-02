#!/bin/bash
# script used to create plots given a dataset
if [ "$1" = "-h" ]; then 
	echo -e "Algorithms indexes:";
	echo -e "\t1:\tSMA"
	echo -e "\t2:\tESMA"
	echo -e "\t3:\tRESMA"
	echo -e "\t4:\tAAESMA"
	echo -e "\t5:\tAAESMAGrad"
	echo -e "\t6:\tAAAESMA"
	exit 
fi


PICS_DIR="pics/"
if [ ! -e $PICS_DIR ]; then 
	mkdir $PICS_DIR
fi

PLOT_NAME[2]='Number of steps'
PLOT_NAME[3]='Execution time (ms)'
PLOT_NAME[4]='Regret cost'
PLOT_NAME[5]='Egalitarian cost'
PLOT_NAME[6]='Sex equality cost (mean)'
PLOT_NAME[7]='Inequality cost (median)'

PLOT_SCRIPT=`find . -name plot_one.sh`
TMSTMP=`date "+%y%m%d_%H%M%S"`


mkdir $PICS_DIR/$TMSTMP
for i in `seq 2 7` ; do
	export PLOT_TITLE=${PLOT_NAME[$i]};
	$PLOT_SCRIPT $i $PICS_DIR/$TMSTMP/ "$1"
done

# step used to pack every image to a single pdf file 
# comment it out if you need the separate images

PDF_NAME="$PICS_DIR/$TMSTMP/graphs_$TMSTMP.pdf"


for i in $PICS_DIR/$TMSTMP/*.eps; do
	epstopdf $i;
done
gs -sDEVICE=pdfwrite -dNOPAUSE -dBATCH -dSAFER -sOutputFile=$PICS_DIR/$TMSTMP/all_metrics.pdf $PICS_DIR/$TMSTMP/*.pdf
gs -sDEVICE=pdfwrite -dNOPAUSE -dBATCH -dSAFER -sOutputFile=$PDF_NAME $PICS_DIR/$TMSTMP/pic{3,6}.pdf
rm $PICS_DIR/$TMSTMP/pic?.pdf
ln -sf $PDF_NAME current.pdf  
