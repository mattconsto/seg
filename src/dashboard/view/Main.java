package dashboard.view;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.prefs.Preferences;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import dashboard.model.DatabaseConnection;

/**
 * Main Application. This class handles navigation and user session.
 */
public class Main extends Application {
	private Stage stage;
	private Preferences preferences = Preferences.userRoot();

	@Override
	public void start(Stage primaryStage) {
		stage = primaryStage;
		
		stage.setTitle(preferences.get("ProductName", "Ad Auction Dashboard"));
		
		/*for (int size : new int[] {512, 256, 128, 64, 48, 32, 16})
			stage.getIcons().add(
				new Image(getClass().getResourceAsStream(
					String.format("/icon%d.png", size))));*/
		
		try {
			OpenCampaignController openCampaign;
			try {
				openCampaign = (OpenCampaignController) replaceSceneContent("/dashboard/view/fxml/OpenCampaign.fxml",stage);
			 
				openCampaign.setApp(this);
				openCampaign.init();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			stage.setResizable(false);
			stage.setHeight(505);
			stage.setWidth(500);
			
			if(preferences.getDouble("OpenCampaign_PositionX", -1) != -1) {
				stage.setX(preferences.getDouble("OpenCampaign_PositionX", -1));
				stage.setY(preferences.getDouble("OpenCampaign_PositionY", -1));
			} else {
				stage.centerOnScreen();
			}
			
			stage.setOnCloseRequest(e -> {
				preferences.putDouble("OpenCampaign_PositionX", stage.getX());
				preferences.putDouble("OpenCampaign_PositionY", stage.getY());
			});
			
			stage.show();
			stage.setOnCloseRequest(e -> DatabaseConnection.closeConnection());
		} catch (Exception ex) {}
	}
		
	public void gotoMainForm() {
		stage.hide();
		stage.setResizable(true);
		AuctionController auctionTool = null;
		
		try {
			auctionTool = (AuctionController) replaceSceneContent("/dashboard/view/fxml/AuctionTool.fxml", stage);
			auctionTool.setApp(this);
			auctionTool.init();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		if(preferences.getDouble("AuctionController_PositionX", -1) != -1) {
			stage.setWidth(preferences.getDouble("AuctionController_SizeWidth", -1));
			stage.setHeight(preferences.getDouble("AuctionController_SizeHeight", -1));
			stage.setX(preferences.getDouble("AuctionController_PositionX", -1));
			stage.setY(preferences.getDouble("AuctionController_PositionY", -1));
			stage.setMaximized(preferences.getBoolean("AuctionController_Maximised", false));
		} else {
			double deviceScaling = Toolkit.getDefaultToolkit().getScreenResolution() / 96.0;
			Rectangle bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
			stage.setWidth(0.9 * bounds.getWidth() / deviceScaling);
			stage.setHeight(0.9 * bounds.getHeight() / deviceScaling);
			stage.centerOnScreen();
		}
		
		stage.show();
		stage.setOnCloseRequest(e -> {
			preferences.putDouble("AuctionController_PositionX",  stage.getX());
			preferences.putDouble("AuctionController_PositionY",  stage.getY());
			preferences.putDouble("AuctionController_SizeWidth",  stage.getWidth());
			preferences.putDouble("AuctionController_SizeHeight", stage.getHeight());
			preferences.putBoolean("AuctionController_Maximised", stage.isMaximized());
			
			try {
				preferences.flush();
			} catch (Exception ex) {
				System.err.println("Failed to write settings to file!");
			}
			
			DatabaseConnection.closeConnection(); 
		});
		if (auctionTool != null)
			auctionTool.initMetricTable();
	}
		
	public Stage getStage() {
		return stage;
	}

	private Node replaceSceneContent(String fxml, Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setBuilderFactory(new JavaFXBuilderFactory());
		loader.setLocation(Main.class.getResource(fxml));
		stage.setScene(new Scene(loader.load(Main.class.getResourceAsStream(fxml)), stage.getWidth(), stage.getHeight()));
		return (Node) loader.getController();
	}
}
