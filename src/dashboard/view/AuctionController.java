package dashboard.view;

import dashboard.model.CSVReader;
import dashboard.controller.BounceGraphConstructor;
import dashboard.controller.ClicksGraphConstructor;
import dashboard.controller.ConversionGraphConstructor;
import dashboard.controller.GraphConstructor;
import dashboard.controller.ImpressionsGraphConstructor;
import dashboard.controller.UniqueClicksGraphConstructor;
import dashboard.controller.UniqueImpressionsGraphConstructor;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;

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
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;

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
    
    public void setApp(Main application){
        this.application = application;
    }
    
    public void init() {
    	filterDateFrom.setValue((LocalDate.of(2014,01,01)));
    	filterDateTo.setValue((LocalDate.of(2014,01,14)));
    }

    @FXML
    private void importCampaignAction(ActionEvent event) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Select folder to import campaign");
        
        String userDirectoryString = System.getProperty("user.home");
        File userDirectory = new File(userDirectoryString);
        if(!userDirectory.canRead()) userDirectory = new File("c:/");
        dirChooser.setInitialDirectory(userDirectory);

        File f = dirChooser.showDialog(application.getStage());
        if (f != null) {
            CSVReader importCsv = new CSVReader();
            if (importCsv.checkFilesExist(f)) {
                if (importCsv.readCSVs(f)) {
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
        
        String gender = filterGender.getValue();
        String age = filterAge.getValue();
        String income = filterIncome.getValue();
        String context = filterContext.getValue();
        String time = filterTime.getValue();
        
        switch(filterMetrics.getValue()) {
        	default:
        	case "Bounces":
        		constructor = new BounceGraphConstructor(gender, age, income, context, time);
        		break;
        	case "Impressions":
        		constructor = new ImpressionsGraphConstructor(gender, age, income, context, time);
        		break;
        	case "Clicks":
        		constructor = new ClicksGraphConstructor(gender, age, income, context, time);
        		break;
        	case "Unique Impressions":
        		constructor = new UniqueImpressionsGraphConstructor(gender, age, income, context, time);
        		break;
        	case "Unique Clicks":
        		constructor = new UniqueClicksGraphConstructor(gender, age, income, context, time);
        		break;
        	case "Conversions":
        		constructor = new ConversionGraphConstructor(gender, age, income, context, time);
        		break;
        }
		
		lineChart.setCreateSymbols(false);
		lineChart.setLegendVisible(false);
        updateGraph(constructor, filterMetrics.getValue(), lineChart);
    }
    
}
