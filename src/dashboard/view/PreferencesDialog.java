package dashboard.view;

import java.text.DecimalFormat;
import java.util.Optional;
import java.util.prefs.Preferences;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class PreferencesDialog extends Dialog<Boolean>{
	private Window owner;
	AuctionController controller;
	private Preferences preferences = Preferences.userRoot();
	
	public PreferencesDialog(AuctionController controller, Window owner){
		this.controller = controller;
		this.owner = owner;
		init();
	}

	private void init(){
		setTitle("Preferences");
		setHeaderText("System preferences");
		initStyle(StageStyle.UTILITY);
		initOwner(owner);
		
		// Add the buttons
		getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		
		//Options
		GridPane mainGrid = new GridPane();
		mainGrid.setHgap(10);
		mainGrid.setVgap(10);
		mainGrid.setPadding(new Insets(20, 150, 10, 10));
		
		//Colours
		final ToggleGroup colourGroup = new ToggleGroup();
		
		RadioButton defaultColour = new RadioButton("Default");
		defaultColour.setToggleGroup(colourGroup);
		defaultColour.setSelected(preferences.get("Graph_Colour", "Default").equals("Default"));
		defaultColour.setUserData("Default");
		
		RadioButton highContrast = new RadioButton("High Contrast");
		highContrast.setToggleGroup(colourGroup);
		highContrast.setSelected(preferences.get("Graph_Colour", "Default").equals("HighContrast"));
		highContrast.setUserData("HighContrast");
		
		//Graph Icons
		CheckBox graphIcons = new CheckBox("Show graph icons");
		graphIcons.setSelected(preferences.getBoolean("Graph_Icons", true));
		
		//Font sizes
		final ToggleGroup fontGroup = new ToggleGroup();
		
		RadioButton smallFont = new RadioButton("Small");
		smallFont.setToggleGroup(fontGroup);
		smallFont.setSelected(preferences.get("Font_Size", "Med").equals("Small"));
		smallFont.setUserData("Small");
		
		RadioButton medFont = new RadioButton("Medium");
		medFont.setToggleGroup(fontGroup);	
		medFont.setSelected(preferences.get("Font_Size", "Med").equals("Med"));
		medFont.setUserData("Med");
		
		RadioButton largeFont = new RadioButton("Large");
		largeFont.setToggleGroup(fontGroup);
		largeFont.setSelected(preferences.get("Font_Size", "Med").equals("Large"));
		largeFont.setUserData("Large");

		TextField currency = new TextField(preferences.get("Currency_Symbol", new DecimalFormat().getDecimalFormatSymbols().getCurrencySymbol()));
		
		mainGrid.add(new Label("Graph colour scheme"), 0, 0);
		mainGrid.add(defaultColour, 0, 1);
		mainGrid.add(highContrast, 1, 1);
		
		mainGrid.add(new Label("Graph Options"), 0, 2);
		mainGrid.add(graphIcons, 0, 3);
		
		mainGrid.add(new Label("Font Size"), 0, 4);
		mainGrid.add(smallFont, 0, 5);
		mainGrid.add(medFont, 1, 5);
		mainGrid.add(largeFont, 2, 5);
		
		mainGrid.add(new Label("General Options"), 0, 6);
		mainGrid.add(new Label("Currency"), 0, 7);
		mainGrid.add(currency, 1, 7);
		
		getDialogPane().setContent(mainGrid);
		
		//Set the choices when clicking ok
		setResultConverter(dialogButton -> {
		    if (dialogButton == ButtonType.OK) {
		    	
		        return true;
		    }
		    return null;
		});
		
		Optional<Boolean> response = showAndWait();
		if(response.isPresent() && response.get())
		{
			controller.updatePreferences(colourGroup.getSelectedToggle().getUserData().toString(), graphIcons.isSelected(), fontGroup.getSelectedToggle().getUserData().toString(), currency.getText());
		}
	}
}
