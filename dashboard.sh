if [ `uname -o` = "Cygwin" ]; then
	java -cp .\;bin\;libs/* Launcher
else
	java -cp .:bin:libs/* Launcher
fi