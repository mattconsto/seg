package dashboard.view;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.IOException;

import dashboard.Preferences;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Main Application. This class handles navigation and user session.
 */
public class Main extends Application {
	private Stage stage;

	@Override
	public void start(Stage primaryStage) {
		// Setup our stage
		stage = primaryStage;
		stage.setTitle(Preferences.productName);
		for (int size : new int[] {512, 256, 128, 64, 48, 32, 16})
			stage.getIcons().add(
				new Image(getClass().getResourceAsStream(
					String.format("/icon%d.png", size))));
		
		// Get the window display scaling, so we can set the correct res.
		double deviceScaling = Toolkit.getDefaultToolkit()
								.getScreenResolution() / 96.0;
		
		Rectangle bounds = GraphicsEnvironment
							.getLocalGraphicsEnvironment()
							.getMaximumWindowBounds();

		stage.setWidth(Preferences.windowScaling * bounds.getWidth() / deviceScaling);
		stage.setHeight(Preferences.windowScaling * bounds.getHeight() / deviceScaling);
		stage.centerOnScreen();
		
		try {
			AuctionController auctionTool = (AuctionController) replaceSceneContent("/dashboard/view/fxml/AuctionTool.fxml");
			auctionTool.setApp(this);
			auctionTool.init();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		primaryStage.show();
	}

	public Stage getStage() {
		return stage;
	}

	private Node replaceSceneContent(String fxml) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setBuilderFactory(new JavaFXBuilderFactory());
		loader.setLocation(Main.class.getResource(fxml));
		AnchorPane page = (AnchorPane) loader.load(Main.class.getResourceAsStream(fxml));
		stage.setScene(new Scene(page));
		return (Node) loader.getController();
	}
}