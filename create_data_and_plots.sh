#!/bin/bash


[ -z "$1" ] && echo "First argument must be givenin \"\"" && exit 0 
./create_hist_data.sh "$1"

echo "Building time graph"
./create_histogram.sh time.dat
echo "Building ec graph"
./create_histogram.sh ec.dat
echo "Building sec graph"
./create_histogram.sh sec.dat

rm *.dat

if [ -z $2 ]; then 
	FOLDER=`date "+%y%m%d_%H%M%S"` && echo "Creating my dir";
else
	FOLDER=$2;
	rm -rf $FOLDER;
fi
mkdir $FOLDER
mv ec.pdf sec.pdf time.pdf $FOLDER
echo "Results placed at folder $FOLDER"
