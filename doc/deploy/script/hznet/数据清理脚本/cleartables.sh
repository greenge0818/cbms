#!/bin/bash
host='127.0.0.1'
user='steel_cbms'
passwd='steel_cbms'
database='steel_cbms'.
FILE_TABLES='/usr/local/src/tables.txt'
FILE_UPDATE='/usr/local/src/update.sql'
#tables=`mysql -h $host -u $user -p$passwd $database -e "show tables"`
#echo test
#a=($tables)
#n=${#a[@]}
#for((i=0;i<$n;i++));
while read i
do
mysql -h $host -u $user -p$passwd $database -e " truncate table  $i"
done < $FILE_TABLES
mysql -h $host -u $user -p$passwd $database < $FILE_UPDATE
