#!/bin/bash
# JavAnas Bank - Mac/Linux Starter
echo "Compiling..."
javac *.java

if [ $? -eq 0 ]; then
    echo "Program is Starting..."
    java BankGUI
else
    echo "Compilation Error!"
fi