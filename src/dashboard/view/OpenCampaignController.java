/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dashboard.view;

import dashboard.model.CSVReader;
import dashboard.model.DatabaseConnection;
import java.io.File;

import java.util.List;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
 
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import javafx.stage.FileChooser;


/**
 * FXML Controller class
 *
 * @author emma2_000
 */
public class OpenCampaignController extends AnchorPane {
    Task readImpressionFile;
    Task readClickFile;
    Task readServerFile;
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
    private Button importButton;
    
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
    	CSVReader importCsv = new CSVReader();
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
            
             if (folder.getText() != null) {
        	if (importCsv.checkFilesExist(folder.getText())) {

                	importButton.setVisible(false);
                	p.setVisible(true);
                    DatabaseConnection.closeConnection();
                    DatabaseConnection.setDbfile(enterName.getText() + ".db");    // should check name has is alpha numeric only here as it forms part of the database filename
                   
                    readImpressionFile = importCsv.readImpressions(new File(folder.getText() + "/impression_log.csv"));
                    readClickFile = importCsv.readClicks(new File(folder.getText() + "/click_log.csv"));
                    readServerFile = importCsv.readServer(new File(folder.getText() + "/server_log.csv"));
                    p.setProgress(0);
                    p.progressProperty().unbind();
                    p.progressProperty().bind(readImpressionFile.progressProperty());
               
                     readImpressionFile.messageProperty().addListener(new ChangeListener<String>() {
                     @Override
                     public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                         if (newValue.equals("Done Impression")) {
                              p.progressProperty().unbind();
                              p.progressProperty().bind(readClickFile.progressProperty());
                              new Thread(readClickFile).start();
                         }
                     }});
                    readClickFile.messageProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            if (newValue.equals("Done Clicks")) {
                                p.progressProperty().unbind();
                                p.progressProperty().bind(readServerFile.progressProperty());
                                new Thread(readServerFile).start();
                                 
                            }
                        }
                    });                          
                 
                    readServerFile.messageProperty().addListener(new ChangeListener<String>() {
                        @Override
                        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                            if (newValue.equals("Done Server")) {
                                System.out.println("Tables created successfully");
                                application.gotoMainForm();
                            }
                        }
                   });           
                   new Thread(readImpressionFile).start();
                    
                
                    }
             }
            ///if (importCsv.importCampaign(application.getStage(), enterName.getText()))
           // {   
           //     application.gotoMainForm();
           // }
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
          
         
        FileChooser fChooser = new FileChooser();
        fChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        fChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Campaign files (*.csv)", "*.csv"));
        fChooser.setTitle("Select campaign (3 CSV files) to import" );
        List <File> fl = fChooser.showOpenMultipleDialog(application.getStage());
        String files = "#impression_log.csv#click_log.csv#server_log.csv#";
        // make sure 3 files are selected and they are the expected files
        boolean result = true;
        if (fl != null && fl.size()== 3) {
            for (File f : fl) {
                if (!files.contains("#" + f.getName().toLowerCase()+ "#")) {
                   
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Wrong File");
                    alert.setHeaderText(null);
                    alert.setContentText(f.getName() + " is not a valid campaign file.\n\nFiles must be \n\timpression_log.csv\n\tclick_log.csv\n\tand server_log.csv\n\nPlease select the correct files.");
                    alert.showAndWait();
                    result = false;
                    break;
                }
            }
            if (result)
                folder.setText(fl.get(0).getParent());
                
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Please select ");
            alert.setHeaderText(null);
            alert.setContentText("Please select the following campaign files.\n\n\timpression_log.csv\n\tclick_log.csv\n\tand server_log.csv");
            alert.showAndWait();
       
        }
        
        
         
    }
    
}
