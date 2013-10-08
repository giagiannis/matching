#!/bin/bash

# create graph

DATE=`date "+%y%m%d"`_

[ -z "$RESULTS_DIR" ] && echo "Def RESULTS_DIR" && exit 0


for FILE in $RESULTS_DIR/*; do
	BASE=`basename $FILE`
	NAME=${BASE%.*}
	cp -v $FILE output/current;
	scripts/create_plots.sh "1 2 3 4 5 6";
	mv pics/$DATE* pics/${NAME}_all
	scripts/create_plots.sh "2 3 5 6";
	mv pics/$DATE* pics/${NAME}_esmas
	rm output/current
done

