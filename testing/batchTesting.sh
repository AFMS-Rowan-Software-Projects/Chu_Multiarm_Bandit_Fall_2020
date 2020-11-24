#!/bin/bash
#IF USING CSV FILES, USE THE FILE FLAG LAST IN THE COMMAND

if [ -f LogFiles/batchResults.txt ]; then
    rm LogFiles/batchResults.txt
fi
if [ -f LogFiles/batchLog.txt ]; then
    rm LogFiles/batchLog.txt
fi

echo "How many time to you want to run the program"

read runs

echo "Please enter the full command with flags you would like to run"

read command

for ((counter = runs; counter > 0; counter-- ))
do
    $command >> LogFiles/batchLog.txt
done

echo $command > tempFile

#Random Generated Channels
if  grep -q 'nc' tempFile; then
    channels=$(grep -oP '(?<=-nc )[0-9]+' tempFile)
#Rigged 2 Channel environment
elif grep -q 's 2' tempFile; then
    channels=2
#CSV Files, -f must be last flag set
else
    chosenFile=$(awk '{print $NF}' tempFile)
    
    cat $chosenFile | grep -o ',' | wc -l > channelCount.txt

    read channels < channelCount.txt

    rm channelCount.txt

    channels=$((($channels + 1)/2))
fi

for ((gcounter = $channels - 1; gcounter >= 0; gcounter-- ))
do
        printf "Channel $gcounter: " >> LogFiles/batchResults.txt
        cat LogFiles/batchLog.txt | grep -c "Was Channel $gcounter\>" >> LogFiles/batchResults.txt
done

#If Verbose Output is requested, print out Channel Probabilities before Results
if grep -q '\-v' tempFile; then
    var=$(($channels + 5))
    tail -n +8 LogFiles/batchLog.txt | head -n $var
fi

echo

#Print Sorted Results
sort -r -nk 3,3 LogFiles/batchResults.txt
