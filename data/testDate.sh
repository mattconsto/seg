#!/bin/bash

#Pass in the auction.db file to perform the testing

echo "Date,csvCount,databaseCount,correct?" >> ImpressionDates.txt
for number in {01..14}
do
	XVAR="$(grep "2015-01-$number" impression_log.csv | wc -l)"
	DBVAR="$(sqlite3 $1 'SELECT COUNT(*) AS Frequency FROM IMPRESSIONS WHERE SUBSTR(DATE, 9, 2) = "'$number'"')"
	if [ $XVAR == $DBVAR ];
	then
		echo "2015-01-$number,$XVAR,$DBVAR,true" >> ImpressionDates.txt
	else
		echo "2015-01-$number,$XVAR,$DBVAR,false" >> ImpressionDates.txt
	fi
done

echo "Date,csvCount,databaseCount,correct?" >> ClicksDates.txt
for number in {01..14}
do
	XVAR="$(grep "2015-01-$number" click_log.csv | wc -l)"
	DBVAR="$(sqlite3 $1 'SELECT COUNT(*) AS Frequency FROM CLICKS WHERE SUBSTR(DATE, 9, 2) = "'$number'"')"
	if [ $XVAR == $DBVAR ];
	then
		echo "2015-01-$number,$XVAR,$DBVAR,true" >> ClicksDates.txt
	else
		echo "2015-01-$number,$XVAR,$DBVAR,false" >> ClicksDates.txt
	fi
done

echo "Date,csvCount,databaseCount,correct?" >> ServerDates.txt
for number in {01..14}
do
	XVAR="$(grep "2015-01-$number" server_log.csv | wc -l)"
	DBVAR="$(sqlite3 $1 'SELECT COUNT(*) AS Frequency FROM SERVER WHERE SUBSTR(ENTRYDATE, 9, 2) = "'$number'"')"
	if [ $XVAR == $DBVAR ];
	then
		echo "2015-01-$number,$XVAR,$DBVAR,true" >> ServerDates.txt
	else
		echo "2015-01-$number,$XVAR,$DBVAR,false" >> ServerDates.txt
	fi
done
