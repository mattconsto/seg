if [ ! -d bin ]; then
	echo "Compiling Dashboard"
	mkdir bin
	find -name *.java > sources.txt
	if [ `uname -o` = Cygwin ]; then
		javac -cp .\;libs/* -d bin -encoding UTF8 @sources.txt
	else
		javac -cp .:libs/* -d bin -encoding UTF8 @sources.txt
	fi
	rm sources.txt
fi

echo "Launching Dashboard"
if [ `uname -o` = Cygwin ]; then
	java -cp .\;bin\;libs/* Launcher
else
	java -cp .:bin:libs/* Launcher
fi
