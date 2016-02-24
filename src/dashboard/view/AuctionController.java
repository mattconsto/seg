package dashboard.view;

import dashboard.model.CSVReader;
import dashboard.controller.BounceGraphConstructor;
import dashboard.controller.ClicksGraphConstructor;
import dashboard.controller.ConversionGraphConstructor;
import dashboard.controller.GraphConstructor;
import dashboard.controller.ImpressionsGraphConstructor;
import dashboard.controller.UniqueClicksGraphConstructor;
import dashboard.controller.UniqueImpressionsGraphConstructor;
import dashboard.model.DatabaseConnection;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.controlsfx.control.CheckComboBox;
/**
 * Auction Controller.
 */
public class AuctionController extends AnchorPane {        
    private Main application;
    @FXML
    private MenuBar menu;
    @FXML
    private MenuItem importCampaign;
    @FXML
    private MenuItem close;
    /* The controlsFX checkComboBox is not supported in scenebuilder. To be able 
       to open the view in scenebuilder replace org.controlsfx.control.CheckComboBox with ComboBox
       The .fxml file also needs to be edited to remove the org.controlsfx.control.CheckComboBox
    */
    @FXML
    private org.controlsfx.control.CheckComboBox<String> filterGender;
    @FXML
    private org.controlsfx.control.CheckComboBox<String> filterAge;
    @FXML
    private org.controlsfx.control.CheckComboBox<String> filterIncome;
    @FXML
    private org.controlsfx.control.CheckComboBox<String> filterContext;
    @FXML 
    private ComboBox<String> filterMetrics;
    @FXML
    private DatePicker filterDateFrom;
    @FXML
    private DatePicker filterDateTo;
    @FXML
    private Button generateGraph;
    @FXML
    private LineChart<String,Number> lineChart;
    @FXML
    private TableView<Series<String, Number>> metricTable;
    @FXML
    private ComboBox<String> filterTime;
    @FXML
    private Label campaignName;
    @FXML
    private MenuItem openCampaign;
    
    public void setApp(Main application){
        this.application = application;
    }
    
    public void init() {
    	filterDateFrom.setValue((LocalDate.of(2014,01,01)));
    	filterDateTo.setValue((LocalDate.of(2014,01,14)));
        filterGender.getItems().addAll("Any","Female","Male");
        filterAge.getItems().addAll("Any","Less than 25","25 to 34","35 to 44","45 to 54","Greater than 55");
      							
        filterIncome.getItems().addAll("Any","Low","Medium","High");
        filterContext.getItems().addAll("Any","News","Shopping","Social Media","Blog","Hobbies","Travel");
      
        
       filterGender.getCheckModel().check(0);
       filterAge.getCheckModel().check(0);
       filterContext.getCheckModel().check(0);
       filterIncome.getCheckModel().check(0);
    }

    @FXML
    private void importCampaignAction(ActionEvent event) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Select folder to import campaign");
        
        String userDirectoryString = System.getProperty("user.dir");
        File userDirectory = new File(userDirectoryString);
        if(!userDirectory.canRead()) userDirectory = new File("c:/");
        dirChooser.setInitialDirectory(userDirectory);

        File f = dirChooser.showDialog(application.getStage());
        if (f != null) {
            
            CSVReader importCsv = new CSVReader();
            if (importCsv.checkFilesExist(f)) {
                
                TextInputDialog input = new TextInputDialog();
                input.getEditor().setPromptText("Enter name of campaign");
                input.setTitle("New Auction Campaign");
                input.setHeaderText("Enter name of campaign");
                Optional<String> result = input.showAndWait();

                if (result.isPresent()) {
                    
                    DatabaseConnection.setDbfile(result.get().trim());    // should check name has is alpha numeric only here as it forms part of the database filename
             
                    if (importCsv.readCSVs(f )) {
                       Alert alert = new Alert(Alert.AlertType.INFORMATION);
                       alert.setTitle("Campaign imported successfully");
                       alert.setHeaderText(null);
                       alert.setContentText("The files were imported successfully");
                       alert.showAndWait();
                       campaignName.setText(DatabaseConnection.getDbfile());
                       generateGraph.setDisable(false);
                    }
                }
            }
        }
    }

    @FXML
    private void closeAction(ActionEvent event) {
    	System.exit(0);
    }
    
    public void updateGraph(GraphConstructor graphConstructor, String yLabel, LineChart<String, Number> lineChart){
		lineChart.getYAxis().setLabel(yLabel);
		
		try {
			Series<String, Number> data = graphConstructor.fetchGraph();
			metricTable.getItems().add(data);
			lineChart.getData().add(data);
		} catch (SQLException e) {
			System.err.println("Unable to fetch data from database: " + e.getMessage());
		}
	}
     
    @FXML
    private void generateData(ActionEvent event) {
    	lineChart.getData().clear();
    	
        lineChart.getXAxis().setLabel(filterTime.getValue());
        
        GraphConstructor constructor;
        
        ObservableList <String> gender =  filterGender.getCheckModel().getCheckedItems();
        ObservableList <String> age =  filterGender.getCheckModel().getCheckedItems();
        ObservableList <String> income =  filterGender.getCheckModel().getCheckedItems(); 
        ObservableList <String> context =  filterGender.getCheckModel().getCheckedItems();
       
       /* String gender =  filterGender.getValue();
        String age =  filterGender.getValue();
        String income =  filterGender.getValue(); 
        String context =  filterGender.getValue();*/
        
        String time = filterTime.getValue();
        
        switch(filterMetrics.getValue()) {
        	default:
        	case "Bounces":
        		constructor = new BounceGraphConstructor(gender.get(0), age.get(0), income.get(0), context.get(0), time);
        		break;
        	case "Impressions":
        		constructor = new ImpressionsGraphConstructor(gender.get(0), age.get(0), income.get(0), context.get(0), time);
        		break;
        	case "Clicks":
        		constructor = new ClicksGraphConstructor(gender.get(0), age.get(0), income.get(0), context.get(0), time);
        		break;
        	case "Unique Impressions":
        		constructor = new UniqueImpressionsGraphConstructor(gender.get(0), age.get(0), income.get(0), context.get(0), time);
        		break;
        	case "Unique Clicks":
        		constructor = new UniqueClicksGraphConstructor(gender.get(0), age.get(0), income.get(0), context.get(0), time);
        		break;
        	case "Conversions":
        		constructor = new ConversionGraphConstructor(gender.get(0), age.get(0), income.get(0), context.get(0), time);
        		break;
        }
		
		lineChart.setCreateSymbols(false);
		lineChart.setLegendVisible(false);
        updateGraph(constructor, filterMetrics.getValue(), lineChart);
    }

    @FXML
    private void openCampaignAction(ActionEvent event) {
        
           FileChooser fChooser = new FileChooser();
           fChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
           fChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Campaign files (*.db)", "*.db"));
           fChooser.setTitle("Select campaign" );
           File s = fChooser.showOpenDialog(application.getStage());
           if (s != null) {  
                 DatabaseConnection.setDbfile(s.getName().replace(".db", ""));
                 campaignName.setText(DatabaseConnection.getDbfile());
                 generateGraph.setDisable(false); 
           }
    }
    
}
