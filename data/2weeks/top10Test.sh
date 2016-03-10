#!/bin/bash

echo "Differences between the first 10 entries in the csv file and the databse:" >> ImpressionsTop10Diff.txt
CSV="$(head -n 11 impression_log.csv | tail -n 10 | awk -F',' '{print $1,$2,$7}')"
SQL="$(sqlite3 $1 "SELECT * FROM IMPRESSIONS LIMIT 10" | awk -F'|' '{print $1,$2,$7}')"

diff <(echo "$CSV") <(echo "$SQL") >> ImpressionsTop10Diff.txt

echo "Differences between the first 10 entries in the csv file and the databse:" >> ClicksTop10Diff.txt
CSV="$(head -n 11 click_log.csv | tail -n 10 | sed 's/,/ /g')"
SQL="$(sqlite3 $1 "SELECT * FROM CLICKS LIMIT 10" | sed 's/|/ /g')"

diff <(echo "$CSV") <(echo "$SQL") >> ClicksTop10Diff.txt

echo "Differences between the first 10 entries in the csv file and the databse:" >> ServerTop10Diff.txt
CSV="$(head -n 11 server_log.csv | tail -n 10 | awk -F',' '{print $1,$2,$3,$4}')"
SQL="$(sqlite3 $1 "SELECT * FROM SERVER LIMIT 10" | awk -F'|' '{print $1,$2,$3,$4}')"

diff <(echo "$CSV") <(echo "$SQL") >> ServerTop10Diff.txt


