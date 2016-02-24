echo "Launching Dashboard"
if [ `uname -o` = Cygwin ]; then
	java -cp .\;bin\;libs/*\;icons Launcher
else
	java -cp .:bin:libs/*:icons Launcher
fi
