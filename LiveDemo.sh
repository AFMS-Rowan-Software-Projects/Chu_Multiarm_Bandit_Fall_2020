#!/bin/bash

#Live Demo for MultiArm Bandit Problem

GREEN='\033[0;32m'
NC='\033[0m'

echo "The Channels that we are testing on today are as follows: "
echo

cat liveDemoChannels.txt

echo
echo "Based on these probabilities we can see that the best channel is"
echo "channel 9, due to its high stay success rate, and low stay failure rate"
echo "however there are also other channels that could perform well,"
echo "such as channel 3. Those are the channels we want our algorithms to select."
echo
read -p "First we are going to run the Brute Force Algorithm on these channels"
echo
printf "Command: ${GREEN}java Driver -a 0 -t 1600 -v -s 1 -f livedemo.txt${NC}"

java Driver -a 0 -t 1600 -v -s 1 -f livedemo.txt

echo
read -p "Then, we are going to run the Upper Confidence Bound Algorithm on these channels"
echo
printf "Command: ${GREEN}java Driver -a 1 -t 1600 -v -s 1 -f livedemo.txt${NC}"

java Driver -a 1 -t 1600 -v -s 1 -f livedemo.txt

echo
read -p "Next, we are going to run the Epsilon Greedy Algorithm on these channels"
echo
printf "Command: ${GREEN}java Driver -a 2 -t 1600 -v -s 1 -f livedemo.txt${NC}"

java Driver -a 2 -t 1600 -v -s 1 -f livedemo.txt

echo
read -p "Then, we are going to run the Thompson Sampling ALgorithm on these channels"
echo
printf "Command: ${GREEN}java Driver -a 3 -t 1600 -v -s 1 -f livedemo.txt${NC}"

java Driver -a 3 -t 1600 -v -s 1 -f livedemo.txt

echo "All of these runs take milliseconds to complete, we also want to show that"
echo "even if we scale this to 1,000,000 trials on a 1,000 channels the linear algorithms"
read -p "can complete that many trials in a very short period of time"
echo
printf "Command: ${GREEN}time java Driver -a 1 -t 1024000 -s 0 -nc 1024 -rs 10${NC}"

time java Driver -a 1 -t 1024000 -s 0 -nc 1024 -rs 10
