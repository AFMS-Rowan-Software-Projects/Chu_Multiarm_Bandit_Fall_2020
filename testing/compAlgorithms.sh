#!/bin/bash

#TODO
#Implement Epsilon Greedy when available
#Add choice of algorithms to compare?
#How many trials would we want to run for UCB Algorithm

#Reset log files
if [ -f LogFiles/log.txt ]; then
    rm LogFiles/log.txt
fi
if [ -f LogFiles/log2.txt ]; then
    rm LogFiles/log2.txt
fi
if [ -f LogFiles/UCBResults.txt ]; then
    rm LogFiles/UCBResults.txt
fi
if [ -f LogFiles/BruteResults.txt ]; then
    rm LogFiles/BruteResults.txt
fi
if [ -f LogFiles/AlgComp.txt ]; then
    rm LogFiles/AlgComp.txt
fi

#Text Colors
GREEN='\033[0;32m'
NC='\033[0m'

echo "First you must choose which file you would like to run the algorithms on"
echo "Here are the files available"

ls CSVFiles

read chosenFile

echo "Input number of program runs to compare Brute Force and Upper Confidence Bound"
echo "For larger inputs (Greater than 100), program may take some time to complete"

read runs

#Find number of channels within file
cat CSVFiles/$chosenFile | grep -o ',' | wc -l > channelCount.txt

read channels < channelCount.txt

rm channelCount.txt

channels=$((($channels + 1)/2))

#Brute force automatically runs 100 trials per channel, trials
#inputted into the command line need to account for that
bruteTrials=$(($channels * 100))

for ((counter = $runs; counter > 0; counter-- ))
do
    java Driver -a 1 -t 10000 -s 1 -f CSVFiles/$chosenFile >> LogFiles/log.txt
done

for ((gcounter = $channels - 1; gcounter >= 0; gcounter-- ))
do
    selections=$(cat LogFiles/log.txt | grep -c "Channel $gcounter\>")
    if [[ $selections -gt 0 ]] ; then
        printf "Channel $gcounter: " >> LogFiles/UCBResults.txt
        cat LogFiles/log.txt | grep -c "Channel $gcounter\>" >> LogFiles/UCBResults.txt
    fi
done

for ((newcounter = $runs; newcounter > 0; newcounter-- ))
do
    java Driver -a 0 -t $bruteTrials -s 1 -f CSVFiles/$chosenFile >> LogFiles/log2.txt
done

for ((gcounter2 = $channels - 1; gcounter2 >= 0; gcounter2-- ))
do
    selections=$(cat LogFiles/log2.txt | grep -c "Channel $gcounter2\>")
    if [[ $selections -gt 0 ]] ; then
        printf "Channel $gcounter2: " >> LogFiles/BruteResults.txt
        cat LogFiles/log2.txt | grep -c "Channel $gcounter2\>" >> LogFiles/BruteResults.txt
    fi
done

echo "UCB on left, Brute Force on Right"
echo "Channels who did not win a single run were removed"

diff -y LogFiles/UCBResults.txt LogFiles/BruteResults.txt >> LogFiles/AlgComp.txt

cat LogFiles/AlgComp.txt


