package dashboard.controller;


import dashboard.model.DatabaseImporter;
import dashboard.view.Main;

import java.io.File;
 
import java.sql.SQLException;
 
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import org.controlsfx.control.CheckComboBox;

/**
 * Auction Controller.
 */
public class AuctionController extends AnchorPane {

   private final CategoryAxis xAxis = new CategoryAxis();
   private final NumberAxis yAxis = new NumberAxis();
        
    private Main application;
    @FXML
    private MenuBar menu;
    @FXML
    private MenuItem importCampaign;
    @FXML
    private MenuItem openCampaign;
    @FXML
    private MenuItem close;
    /* The controlsFX checkComboBox is not supported in scenebuilder. To be able 
       to open the view in scenebuilder replace org.controlsfx.control.CheckComboBox with ComboBox
       The .fxml file also needs to be edited to remove the org.controlsfx.control.CheckComboBox
    */
    @FXML
    private ComboBox<String> filterGender;
    @FXML
    private ComboBox<String> filterAge;
    @FXML
    private ComboBox<String> filterIncome;
    @FXML
    private ComboBox<String> filterContext;
    @FXML 
    private ComboBox<String> filterMetrics;
     /*       
    @FXML
    private ComboBox<?> filterGender;
    @FXML
    private ComboBox<?> filterAge;
    @FXML
    private ComboBox<?> filterIncome;
    @FXML
    private ComboBox<?> filterContext;*/
    @FXML
    private DatePicker filterDateFrom;
    @FXML
    private DatePicker filterDateTo;
    @FXML
    private Button generateGraph;
    @FXML
    private LineChart<String,Number> lineChart;
    @FXML
    private ComboBox<String> filterTime;
    @FXML
    private Label campaignName;
    
    
    public void setApp(Main application){
        this.application = application;
    }
    
    public void initialize() {
        filterGender.getItems().addAll("Female","Male");
        filterAge.getItems().addAll("Less than 25","25 to 34","35 to 44","45 to 54","Greater than 55");
        filterIncome.getItems().addAll("Low","Medium","High");
        filterContext.getItems().addAll("News","Shopping","Social Media","Blog","Hobbies","Travel");
        filterTime.getItems().addAll("Hours","Days","Weeks","Months");
        filterMetrics.getItems().addAll("Impressions","Clicks","Unique Impressions","Unique Clicks","Conversions");
    }

    @FXML
    private void importCampaignAction(ActionEvent event) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Select folder to import campaign");
        
        String userDirectoryString = System.getProperty("user.home");
        File userDirectory = new File(userDirectoryString);
        if(!userDirectory.canRead()) {
            userDirectory = new File("c:/");
        }
        dirChooser.setInitialDirectory(userDirectory);

        File f = dirChooser.showDialog(application.getStage());
        if (f != null)
        {
            DatabaseImporter importCsv = new DatabaseImporter();
            if (importCsv.checkFilesExist(f.getAbsolutePath())) {
                
                /* FileChooser fChooser = new FileChooser();
                 fChooser.setTitle("Save As" );
                 File s = fChooser.showSaveDialog(application.getStage());
                 if (s != null)
                 {*/
                if (importCsv.readCSVs(f.getAbsolutePath()))
                {
                       Alert alert = new Alert(Alert.AlertType.INFORMATION);
                       alert.setTitle("Campaign imported successfully");
                       alert.setHeaderText(null);
                       alert.setContentText("The files were imported successfully");
                       alert.showAndWait();
                }
            }
        }
    }

    @FXML
    private void openCampaignAction(ActionEvent event) {
    }

    @FXML
    private void closeAction(ActionEvent event) {
    }


    private void preferenceAction4(ActionEvent event) {
        MenuItem mItem = (MenuItem) event.getSource();
                String side = mItem.getText();
                if ("left".equalsIgnoreCase(side)) {
                    System.out.println("left");
                } else if ("right".equalsIgnoreCase(side)) {
                    System.out.println("right");
                } else if ("top".equalsIgnoreCase(side)) {
                    System.out.println("top");
                } else if ("bottom".equalsIgnoreCase(side)) {
                    System.out.println("bottom");
                }
    }

    
    public void updateGraph(GraphConstructor graphConstructor, String yLabel, LineChart<String, Number> lineChart){
		yAxis.setLabel(yLabel);
		
		try {
			lineChart.getData().add(graphConstructor.fetchGraph());
		} catch (SQLException e) {
			System.err.println("Unable to fetch data from database: " + e.getMessage());
		}
	}
     
    @FXML
    private void generateData(ActionEvent event) {
        xAxis.setLabel("Date");
		
		lineChart.setCreateSymbols(false);
		lineChart.setLegendVisible(false);
        updateGraph(new ImpressionsGraphConstructor(), "Impressions", lineChart);
        
    }
    
}
