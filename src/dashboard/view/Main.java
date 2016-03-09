package dashboard.view;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import dashboard.Preferences;
import dashboard.model.DatabaseConnection;
import javafx.stage.Modality;

/**
 * Main Application. This class handles navigation and user session.
 */
public class Main extends Application {
	private Stage stage;
        private Stage openForm;

	@Override
	public void start(Stage primaryStage) {
		// Setup our stage
		stage = primaryStage;
		stage.setTitle(Preferences.productName);
/*		for (int size : new int[] {512, 256, 128, 64, 48, 32, 16})
			stage.getIcons().add(
				new Image(getClass().getResourceAsStream(
					String.format("/icon%d.png", size))));
*/
		gotoOpenForm();
     
              
      
	}
        private void gotoOpenForm() {
        try {
            OpenCampaignController openCampaign;
            try {
                openCampaign = (OpenCampaignController) replaceSceneContent("/dashboard/view/fxml/OpenCampaign.fxml",stage);
             
                openCampaign.setApp(this);
                openCampaign.init();
  
            } catch (IOException ex) {
			ex.printStackTrace();
	    }

            stage.centerOnScreen();
            stage.show();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				DatabaseConnection.closeConnection();
			}
		});
        } catch (Exception ex) {
             
        }
        }
        
        public void gotoMainForm() {
        // Get the window display scaling, so we can set the correct res.
	        stage.hide();
        	AuctionController auctionTool;
		try {
			auctionTool = (AuctionController) replaceSceneContent("/dashboard/view/fxml/AuctionTool.fxml", stage);
			auctionTool.setApp(this);
			auctionTool.init();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
                double deviceScaling = Toolkit.getDefaultToolkit()
				.getScreenResolution() / 96.0;

		Rectangle bounds = GraphicsEnvironment
				.getLocalGraphicsEnvironment()
				.getMaximumWindowBounds();

		stage.setWidth(Preferences.windowScaling * bounds.getWidth() / deviceScaling);
		stage.setHeight(Preferences.windowScaling * bounds.getHeight() / deviceScaling);
		stage.centerOnScreen();
		stage.show();
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				DatabaseConnection.closeConnection();
			}
		});  
        }
        
	public Stage getStage() {
		return stage;
	}
        

	private Node replaceSceneContent(String fxml, Stage stage) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setBuilderFactory(new JavaFXBuilderFactory());
		loader.setLocation(Main.class.getResource(fxml));
		AnchorPane page = (AnchorPane) loader.load(Main.class.getResourceAsStream(fxml));
		stage.setScene(new Scene(page));
		return (Node) loader.getController();
	}
}
