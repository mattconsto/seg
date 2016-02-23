import AuctionTool.Main;
import javafx.application.Application;

/**
 * Launch the MainUI
 */
public class Launcher {
	public static void main(String args[]) {
		Application.launch(Main.class, (String[]) null);
		
		// GraphView.launch(args);
		// new DatabaseImporter().readCSVs("data");
	}
}
