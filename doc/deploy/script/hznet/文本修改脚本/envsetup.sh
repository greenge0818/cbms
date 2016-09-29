function replace_usage(){
cat << USAGE
Usage:
	replace [OPTIONS] old_str new_str

Description:
	find and replace string

OPTIONS:
	-t, --type
USAGE
}

function replace(){
	long_opts="type:"
	getopt_cmd=$(getopt -o t: --long "$long_opts" -n $(echo "replace") -- "$@") || { echo -e "\nERROR: Getopt faild. Extra args\n";replace_usage;return 0;};
	eval set -- "$getopt_cmd"
	local type="";
	while [ -n "$1" ] 
	do
		case "$1" in
			-t|--type)
				type="$2"
			;;
			--) shift;break;;
		esac
		shift	
	done
	if [ -z "$type" ]; then
		replace_usage
	else
		if [ $# == 2 ]; then
			echo -e "33[00;36mreplace....33[0m"
			find $(pwd) -name "*\.$type" | xargs sed -i "s/$1/$2/g"
			echo -e "33[00;36mreplace end!33[0m"

		else
			replace_usage
		fi
	fi
}
