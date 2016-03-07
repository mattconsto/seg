/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dashboard.view;

import dashboard.model.CSVReader;
import dashboard.model.DatabaseConnection;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
 
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author emma2_000
 */
public class OpenCampaignController extends AnchorPane {
    @FXML
    private ProgressBar p;
    @FXML
    private Button openButton;
    @FXML
    private ComboBox<?> selectCampaign;
    @FXML
    private AnchorPane createPane;
     @FXML
     private TextField enterName;
    
    private Main application;     
    @FXML
    private Button browseButton;
    @FXML
    private TextField folder;
    @FXML
    private Button importButton1;
    
    /**
     * Initializes the controller class.
     * @param application
     */
      public void setApp(Main application){
		this.application = application;
	}

    public void init()
    {  
        ObservableList data = FXCollections.observableArrayList();
        //ToDo - tidy up folder to store databases
        File fld = new File(System.getProperty("user.dir"));
        File[] dbFiles = fld.listFiles();
        for (File file : dbFiles) {
            if (file.isFile() && file.getName().toLowerCase().endsWith(".db")) 
               data.add(file.getName());
        
        }  
        selectCampaign.setItems(data);
    }

    @FXML
    private void selectCampaignAction(ActionEvent event) {
    }

    @FXML
    private void importAction(ActionEvent event) {
        if (enterName.getText().isEmpty() || enterName.getText().equalsIgnoreCase("Enter name for new campaign and select import"))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
						alert.setTitle("Please enter a campaign name");
						alert.setHeaderText(null);
						alert.setContentText("Please enter a campaign name");
						alert.showAndWait();
        }
        else
        { 
            CSVReader importCsv = new CSVReader();
            // ToDo - check name is correct format
            if (importCsv.importCampaign(application.getStage(), enterName.getText()))
            {   
                application.gotoMainForm();
            }
        }
    }
    @FXML
    private void openAction(ActionEvent event) {
         if (!selectCampaign.getSelectionModel().getSelectedItem().equals(""))
         {
             DatabaseConnection.closeConnection();
	     DatabaseConnection.setDbfile(selectCampaign.getSelectionModel().getSelectedItem().toString());
             application.gotoMainForm();
         }
    }

    @FXML
    private void browseAction(ActionEvent event) {
    }
    
}
