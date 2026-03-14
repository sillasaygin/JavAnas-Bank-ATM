@echo off
cd /d "%~dp0"

echo Compiling...
javac *.java

if errorlevel 1 (
    echo Compilation Error!
    pause
    exit
)

echo Program is Starting...
start "" javaw BankGUI
exit