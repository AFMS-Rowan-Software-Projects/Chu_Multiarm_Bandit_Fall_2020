#!/bin/bash

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
#CSV Files
else
    chosenFile=$(awk '{print $NF}' tempFile)

    cat $chosenFile | grep -o ',' | wc -l > channelCount.txt

    read channels < channelCount.txt

    rm channelCount.txt

    channels=$((($channels + 1)/2))

    echo $channels
fi

for ((gcounter = $channels - 1; gcounter >= 0; gcounter-- ))
do
        printf "Channel $gcounter: " >> LogFiles/batchResults.txt
        cat LogFiles/batchLog.txt | grep -c "Was Channel $gcounter\>" >> LogFiles/batchResults.txt
done

#Print Sorted Results
sort -r -k 3,3 LogFiles/batchResults.txt
