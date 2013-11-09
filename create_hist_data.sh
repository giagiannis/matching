#/bin/bash

function decide_on_labels {
	BASE=`basename $1`
	CORE=${BASE%.}
	case $CORE in
		gauss_1 | *g1*)
			echo -ne "80%";
			;;
		gauss_2 | *g2*)
			echo -ne "60%";
			;;
		gauss_3 | *g3*)
			echo -ne "40%";
			;;
		gauss_4 | *g4*)
			echo -ne "20%";
			;;
		discrete_05)
			echo -ne "5%";
			;;
		discrete_10)
			echo -ne "10%";
			;;
		discrete_25)
			echo -ne "25%";
			;;
		discrete_50)
			echo -ne "50%";
			;;
	esac
	echo -ne "\t"
}

[ -z "$1" ] && echo "First argument must be given" && exit 0 

function create_data {
	POL=0; 
	echo -e "# Input files: $1";
	echo -e "# Columns to print: $2";
	echo -e "Polarity\tSMA\tESMA";
	for i in $1; do 
		B=`basename $i`;
		F=${B%.*}
#		let POL=POL20; 
#		echo -ne "$F%\t"; 
		decide_on_labels $F
		tail -n 1 $i | cut -f "$2"; 
	done
}

#EXECUTION_TIME="3 9 15 27 33";
#EGALITARIAN="5 11 17 29 35";
#SEXEQUAL="6 12 18 30 36";
EXECUTION_TIME="3 9";
EGALITARIAN="5 11";
SEXEQUAL="6 12";

create_data "$1" "$EXECUTION_TIME" > time.dat
create_data "$1" "$EGALITARIAN" > ec.dat
create_data "$1" "$SEXEQUAL" > sec.dat
