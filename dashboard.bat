@echo off

if not exist "bin" goto build

:run
echo Launching Dashboard
java -cp .;bin;libs/* Launcher
exit

:build
echo Compiling Dashboard
mkdir bin
dir /s /B *.java > sources.txt
javac -cp .;libs/* -d bin -encoding UTF8 @sources.txt
del sources.txt
goto run
