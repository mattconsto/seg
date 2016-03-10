import java.util.Arrays;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MultiSelectionComboDemo extends Application {
	final ListView<String> selectedItems = new ListView<>();

	@Override
	public void start(Stage primaryStage) {
		final MenuButton choices = new MenuButton("Obst");
		final List<CheckMenuItem> items = Arrays.asList(new CheckMenuItem("Apfel"), new CheckMenuItem("Banane"),
				new CheckMenuItem("Birne"), new CheckMenuItem("Kiwi"));
		choices.getItems().addAll(items);

		for (final CheckMenuItem item : items) {
			item.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
				if (newValue) {
					selectedItems.getItems().add(item.getText());
				} else {
					selectedItems.getItems().remove(item.getText());
				}
			});
		}

		BorderPane borderPane = new BorderPane();
		borderPane.setTop(choices);
		borderPane.setCenter(selectedItems);
		primaryStage.setScene(new Scene(borderPane, 400, 300));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
