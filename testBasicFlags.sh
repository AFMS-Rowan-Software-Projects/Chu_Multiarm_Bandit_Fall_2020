#!/bin/bash

#Text Colors
GREEN='\033[0;32m'
NC='\033[0m'

read -p "Press Enter to Test program with no flags set"

printf "Command: ${GREEN}java Driver ${NC}\n\n"

java Driver

echo

read -p "Press enter to test the program with the help flag set"

printf "Command: ${GREEN}java Driver -h ${NC}\n\n"

java Driver -h

read -p "Press Enter to test the Brute Force Algorithm on the rigged 2 channel environment"

printf "Command: ${GREEN}java Driver -a 0 -t 200 -s 2 ${NC}\n\n"

java Driver -a 0 -t 200 -s 2

echo

read -p "Press Enter to test the Verbose output flag"

printf "Command: ${GREEN}java Driver -a 0 -t 200 -s 2 -v ${NC}\n\n"

java Driver -a 0 -t 200 -s 2 -v

echo

read -p "Press Enter to test the Upper Confidence Bound Algorithm on the rigged 2 channel environment"

printf "Command: ${GREEN} java Driver -a 1 -t 200 -s 2 -v ${NC}\n\n"

java Driver -a 1 -t 200 -s 2 -v

echo

read -p "Press Enter to test the trial flag (10 trials vs 100 trials)"

printf "Command 1: ${GREEN} java Driver -a 1 -t 10 -s 2 -v ${NC}\n\n"
java Driver -a 1 -t 10 -s 2 -v

printf "Command 2: ${GREEN} java Driver -a 1 -t 100 -s 2 -v ${NC}\n\n"
java Driver -a 1 -t 100 -s -2 -v

echo

read -p "Press Enter to test the randomseed flag on a 5 channel setup"
printf "Command 1: ${GREEN} java Driver -a 1 -t 500 -s 0 -v -rs 10 -nc 5 ${NC}\n\n"
java Driver -a 1 -t 500 -s 0 -v -rs 10 -nc 5

printf "Command 2: ${GREEN} java Driver -a 1 -t 500 -s 0 -v -rs 20 -nc 5 ${NC}\n\n"
java Driver -a 1 -t 500 -s 0 -v -rs 20 -nc 5

echo

read -p "Press Enter to test the number of channels flag (5 Channels vs 20 Channels)"

printf "Command 1: ${GREEN} java Driver -a 1 -t 500 -s 0 -v -rs 10 -nc 5 ${NC}\n\n"
java Driver -a 1 -t 500 -s 0 -v -rs 10 -nc 5

printf "Command 2: ${GREEN} java Driver -a 1 -t 500 -s 0 -v -rs 10 -nc 20 ${NC}\n\n"
java Driver -a 1 -t 500 -s 0 -v -rs 10 -nc 20


