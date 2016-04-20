package dashboard.view;

import java.util.Optional;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import javafx.util.Pair;

public class PreferencesDialog extends Dialog<Boolean>{
	private Window owner;
	AuctionController controller;
	
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
		defaultColour.setSelected(true);
		defaultColour.setUserData("Default");
		
		RadioButton highContrast = new RadioButton("High Contrast");
		highContrast.setToggleGroup(colourGroup);
		highContrast.setUserData("HighContrast");
		
		//Graph Icons
		CheckBox graphIcons = new CheckBox("Show graph icons");
		graphIcons.setSelected(false);
		
		//Dashed lines
		CheckBox graphDash = new CheckBox("Show dashed series");
		graphDash.setSelected(false);
		
		//Font sizes
		final ToggleGroup fontGroup = new ToggleGroup();
		
		RadioButton smallFont = new RadioButton("Small");
		smallFont.setToggleGroup(fontGroup);
		smallFont.setUserData("Small");
		
		RadioButton medFont = new RadioButton("Medium");
		medFont.setToggleGroup(fontGroup);	
		medFont.setSelected(true);
		medFont.setUserData("Med");
		
		RadioButton largeFont = new RadioButton("Large");
		largeFont.setToggleGroup(fontGroup);
		largeFont.setUserData("Large");
		
		mainGrid.add(new Label("Graph colour scheme"), 0, 0);
		mainGrid.add(defaultColour, 0, 1);
		mainGrid.add(highContrast, 1, 1);
		
		mainGrid.add(new Label("Graph options"), 0, 2);
		mainGrid.add(graphIcons, 0, 3);
		mainGrid.add(graphDash, 0, 4);
		
		mainGrid.add(new Label("Font size"), 0, 5);
		mainGrid.add(smallFont, 0, 6);
		mainGrid.add(medFont, 1, 6);
		mainGrid.add(largeFont, 2, 6);
		
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
			controller.updatePreferences(colourGroup.getSelectedToggle().getUserData().toString(), false, false, fontGroup.getSelectedToggle().getUserData().toString());
		}
	}
}
