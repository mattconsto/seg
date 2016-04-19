package dashboard.view;

public class GenerateName {
	private static String[] alphabet = {"Alpha", "Bravo", "Charlie", "Delta", "Echo", "Foxtrot", "Golf", "Hotel", "India", "Juliet", "Kilo", "Lima", "Mike", "November", "Oscar", "Papa", "Quebec", "Romeo", "Sierra", "Tango", "Uniform", "Victor", "Whiskey", "X-ray", "Yankee", "Zulu"};
	private static int      counter  = 0;
	
	public static String generate() {
		return alphabet[counter++ % alphabet.length];
	}
}
