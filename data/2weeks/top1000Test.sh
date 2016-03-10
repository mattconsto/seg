#!/bin/bash

echo "Differences between the first 1000 entries in the csv file and the databse:" >> ImpressionsTop1000Diff.txt
CSV="$(head -n 1001 impression_log.csv | tail -n 1000 | awk -F',' '{print $1,$2,$7}')"
SQL="$(sqlite3 $1 "SELECT * FROM IMPRESSIONS LIMIT 1000" | awk -F'|' '{print $1,$2,$7}')"

diff <(echo "$CSV" | awk ' {sub("\\.*0+$","");print} ') <(echo "$SQL" | awk ' {sub("\\.*0+$","");print} ') >> ImpressionsTop1000Diff.txt

echo "Differences between the first 1000 entries in the csv file and the databse:" >> ClicksTop1000Diff.txt
CSV="$(head -n 1001 click_log.csv | tail -n 1000 | sed 's/,/ /g')"
SQL="$(sqlite3 $1 "SELECT * FROM CLICKS LIMIT 1000" | sed 's/|/ /g')"

diff <(echo "$CSV" | awk ' {sub("\\.*0+$","");print} ') <(echo "$SQL" | awk ' {sub("\\.*0+$","");print} ') >> ClicksTop1000Diff.txt

echo "Differences between the first 1000 entries in the csv file and the databse:" >> ServerTop1000Diff.txt
CSV="$(head -n 1001 server_log.csv | tail -n 1000 | awk -F',' '{print $1,$2,$3,$4}')"
SQL="$(sqlite3 $1 "SELECT * FROM SERVER LIMIT 1000" | awk -F'|' '{print $1,$2,$3,$4}')"

diff <(echo "$CSV" | awk ' {sub("\\.*0+$","");print} ') <(echo "$SQL" | awk ' {sub("\\.*0+$","");print} ') >> ServerTop1000Diff.txt


