package AuctionTool;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Main Application. This class handles navigation and user session.
 */
public class Main extends Application {
	private Stage stage;
	private final double MINIMUM_WINDOW_WIDTH = 390.0;
	private final double MINIMUM_WINDOW_HEIGHT = 500.0;

	@Override
	public void start(Stage primaryStage) {
		try {
			stage = primaryStage;
			stage.setMinWidth(MINIMUM_WINDOW_WIDTH);
			stage.setMinHeight(MINIMUM_WINDOW_HEIGHT);
			stage.setTitle("Auction Tool");
			gotoAuctionTool();
			primaryStage.show();
		} catch (Exception ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	private void gotoAuctionTool() {
		try {
			AuctionController auctionTool = (AuctionController) replaceSceneContent("AuctionTool.fxml");
			auctionTool.setApp(this);
			auctionTool.initialize();
		} catch (Exception ex) {
			Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public Stage getStage() {
		return stage;

	}

	private Node replaceSceneContent(String fxml) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		InputStream in = Main.class.getResourceAsStream(fxml);
		loader.setBuilderFactory(new JavaFXBuilderFactory());
		loader.setLocation(Main.class.getResource(fxml));
		AnchorPane page;
		try {
			page = (AnchorPane) loader.load(in);
		} finally {
			in.close();
		}

		// Store the stage width and height in case the user has resized the
		// window
		double stageWidth = stage.getWidth();
		if (!Double.isNaN(stageWidth)) {
			stageWidth -= (stage.getWidth() - stage.getScene().getWidth());
		}

		double stageHeight = stage.getHeight();
		if (!Double.isNaN(stageHeight)) {
			stageHeight -= (stage.getHeight() - stage.getScene().getHeight());
		}

		Scene scene = new Scene(page);
		if (!Double.isNaN(stageWidth)) {
			page.setPrefWidth(stageWidth);
		}
		if (!Double.isNaN(stageHeight)) {
			page.setPrefHeight(stageHeight);
		}

		stage.setScene(scene);
		stage.sizeToScene();
		return (Node) loader.getController();
	}
}
