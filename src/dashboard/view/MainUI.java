package dashboard.view;

import dashboard.model.DatabaseImporter;

public class MainUI {
	public MainUI() {
		new DatabaseImporter().readCSVs("data");
	}
}
