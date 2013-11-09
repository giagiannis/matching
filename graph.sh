#!/bin/bash

# create graph

DATE=`date "+%y%m%d"`_

[ -z "$RESULTS_DIR" ] && echo "Def RESULTS_DIR" && exit 0


for FILE in $RESULTS_DIR/*.csv; do
	BASE=`basename $FILE`
	NAME=${BASE%.*}
	cp -v $FILE output/current;
	mkdir pics/$NAME
	scripts/create_plots.sh "1 2";
	mv pics/$DATE*/* pics/${NAME}/
	rm pics/$DATE* -rf
done

