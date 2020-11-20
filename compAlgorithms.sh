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
if [ -f LogFiles/Alg1Results.txt ]; then
    rm LogFiles/Alg1Results.txt
fi
if [ -f LogFiles/Alg2Results.txt ]; then
    rm LogFiles/Alg2Results.txt
fi
if [ -f LogFiles/AlgComp.txt ]; then
    rm LogFiles/AlgComp.txt
fi

#Text Colors
GREEN='\033[0;32m'
NC='\033[0m'

echo "Which algorithms would you like to compare"
echo "0: Brute Force, 1: UCB, 2: Epsilon Greedy, 3: Thompson Sampling"
echo "Enter their number one at a time"

read alg1
read alg2

echo "Now you must choose which file you would like to run the algorithms on"
echo "Here are the files available"

ls testingcsvs

read chosenFile

echo "Input number of program runs to compare Brute Force and Upper Confidence Bound"
echo "For larger inputs (Greater than 100), program may take some time to complete"

read runs

timer=$SECONDS

#Find number of channels within file
cat testingcsvs/$chosenFile | grep -o ',' | wc -l > channelCount.txt

read channels < channelCount.txt

rm channelCount.txt

channels=$((($channels + 1)/2))

#Brute force automatically runs 100 trials per channel, trials
#inputted into the command line need to account for that
bruteTrials=$(($channels * 100))

#Need to decide on Trials/Channel, currently set to 100
for ((counter = $runs; counter > 0; counter-- ))
do
    java Driver -a $alg1 -t $bruteTrials -s 1 -f testingcsvs/$chosenFile >> LogFiles/log.txt
done

for ((gcounter = $channels - 1; gcounter >= 0; gcounter-- ))
do
    selections=$(cat LogFiles/log.txt | grep -c "Channel $gcounter\>")
    if [[ $selections -gt 0 ]] ; then
	printf "Channel $gcounter: " >> LogFiles/Alg1Results.txt
	cat LogFiles/log.txt | grep -c "Channel $gcounter\>" >> LogFiles/Alg1Results.txt
    fi
done

for ((newcounter = $runs; newcounter > 0; newcounter-- ))
do
    java Driver -a $alg2 -t $bruteTrials -s 1 -f testingcsvs/$chosenFile >> LogFiles/log2.txt
done

for ((gcounter2 = $channels - 1; gcounter2 >= 0; gcounter2-- ))
do
    selections=$(cat LogFiles/log2.txt | grep -c "Channel $gcounter2\>")
    if [[ $selections -gt 0 ]] ; then
	printf "Channel $gcounter2: " >> LogFiles/Alg2Results.txt
	cat LogFiles/log2.txt | grep -c "Channel $gcounter2\>" >> LogFiles/Alg2Results.txt
    fi
done

runtime=$(($SECONDS-$timer))

echo "runtime: $runtime seconds" >> LogFiles/AlgComp.txt

echo
echo "Algorithm 1 on left, Algorith 2 on Right" >> LogFiles/AlgComp.txt
echo "Channels who did not win a single run were removed" >> LogFiles/AlgComp.txt

diff -y LogFiles/Alg1Results.txt LogFiles/Alg2Results.txt >> LogFiles/AlgComp.txt

cat LogFiles/AlgComp.txt
