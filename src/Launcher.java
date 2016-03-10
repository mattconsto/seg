import dashboard.view.Main;
import javafx.application.Application;
import net.infotrek.util.prefs.FilePreferencesFactory;

/**
 * Launch the Ad Auction Dashboard
 */
public class Launcher {
	public static void main(String args[]) {
		// Launch the JavaFX application
		System.setProperty("java.util.prefs.PreferencesFactory", FilePreferencesFactory.class.getName());
		System.setProperty(FilePreferencesFactory.SYSTEM_PROPERTY_FILE, "auction.preferences");
		Application.launch(Main.class);
	}
}
