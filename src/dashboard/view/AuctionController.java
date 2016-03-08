package dashboard.view;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import dashboard.controller.BounceGraphConstructor;
import dashboard.controller.CPMGraphConstructor;
import dashboard.controller.CPAGraphConstructor;
import dashboard.controller.CPCGraphConstructor;
import dashboard.controller.ClicksGraphConstructor;
import dashboard.controller.ConversionGraphConstructor;
import dashboard.controller.GraphConstructor;
import dashboard.controller.ImpressionsGraphConstructor;
import dashboard.controller.UniqueClicksGraphConstructor;
import dashboard.controller.UniqueImpressionsGraphConstructor;
import dashboard.model.DatabaseConnection;
import dashboard.model.Filter;
import dashboard.model.ObservableMetrics;
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
	//    private TableView<Series<String, Number>> metricTable;
	@FXML
	private ComboBox<String> filterTime;
	@FXML
	private Label campaignName;
	@FXML
	private MenuItem openCampaign;
	@FXML
	private TableView<ObservableMetrics> tableResults;
	@FXML
	private TableColumn<ObservableMetrics, String> metricCol;
	@FXML
	private TableColumn<ObservableMetrics, String> resultCol;

	final ObservableList<ObservableMetrics> tableContent = FXCollections.observableArrayList();
	private Filter filter;
    @FXML
    private MenuItem deleteCampaign;
    @FXML
    private MenuItem exportCampaign;
    @FXML
    private MenuItem printCampaign;
    @FXML
    private MenuItem mnuColour;
    @FXML
    private MenuItem mnuFont;
    @FXML
    private MenuItem mnuPaths;
    @FXML
    private MenuItem mnuUnits;
    @FXML
    private MenuItem mnuDefinitions;
    @FXML
    private MenuItem mnuSearch;
    @FXML
    private MenuItem mnuList;
    @FXML
    private MenuItem mnuAbout;
    @FXML
    private TextField txtBounceTime;
    @FXML
    private TextField txtBouncePages;
    @FXML
    private RadioButton rbByBounceTime;
    @FXML
    private ToggleGroup grBounce;
    @FXML
    private RadioButton rbByBouncePages;

	public void setApp(Main application){
		this.application = application;
	}

	public void init() {
            filter = new Filter();

            filterDateFrom.setValue((LocalDate.of(2015,01,01)));
            filterDateTo.setValue((LocalDate.of(2015,01,14)));
            filterGender.getItems().addAll("Any","Female","Male");
            filterAge.getItems().addAll("Any","Less than 25","25 to 34","35 to 44","45 to 54","Greater than 55");

            filterIncome.getItems().addAll("Any","Low","Medium","High");
            filterContext.getItems().addAll("Any","News","Shopping","Social Media","Blog","Hobbies","Travel");


            filterGender.getCheckModel().check(0);
            filterAge.getCheckModel().check(0);
            filterContext.getCheckModel().check(0);
            filterIncome.getCheckModel().check(0);

            metricCol.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
            resultCol.setCellValueFactory(cellData -> cellData.getValue().resultProperty());
            configureTable();  

           
        filterGender.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            public void onChanged(ListChangeListener.Change<? extends String> c) {
                    filter.setGender(filterGender);
                    
       
                  
        }
        });
        
       filterAge.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            public void onChanged(ListChangeListener.Change<? extends String> c) {
                    filter.setAge(filterAge);
                    
       
                  
        }
        });
            
            campaignName.setText(DatabaseConnection.getDbfile().replace(".db", ""));
            generateGraph.setDisable(false);
            //generateData(null);

        }
         
       
	private final ListChangeListener<ObservableMetrics> tableSelectionChanged =
			new ListChangeListener<ObservableMetrics>() {

		@Override
		public void onChanged(ListChangeListener.Change<? extends ObservableMetrics> c) {
			//do something here
			ObservableMetrics s1 =  tableResults.getSelectionModel().getSelectedItem();
			if(s1 != null)
				drawGraph(s1.getDescription());

		}
	};
      
       

	// Configure the table widget: set up its column, and register the
	// selection changed listener.
	private void configureTable() {
		metricCol.setCellValueFactory(new PropertyValueFactory<>("description"));
		resultCol.setCellValueFactory(new PropertyValueFactory<>("result"));
		tableResults.setItems(tableContent);
		assert tableResults.getItems() == tableContent;

		final ObservableList<ObservableMetrics> tableSelection = tableResults.getSelectionModel().getSelectedItems();

		tableSelection.addListener(tableSelectionChanged);


	}
	@FXML
	private void importCampaignAction(ActionEvent event) {

		/* CSVReader importCsv = new CSVReader();
                if (importCsv.importCampaign(application.getStage()))
                {   
                        campaignName.setText(DatabaseConnection.getDbfile().replace(".db", ""));
                        generateGraph.setDisable(false);
                        generateData(null);
                }*/
             
        }

	@FXML
	private void closeAction(ActionEvent event) {
		DatabaseConnection.closeConnection();
		application.getStage().close();
	}

	public void updateGraph(GraphConstructor graphConstructor, String yLabel, LineChart<String, Number> lineChart){
		lineChart.getYAxis().setLabel(yLabel);

		try {
			Series<String, Number> data = graphConstructor.fetchGraph();
			lineChart.getData().add(data);
		} catch (SQLException e) {
			System.err.println("Unable to fetch data from database: " + e.getMessage());
		}
	}

	public void updateMetricsTable() throws SQLException{
		tableContent.clear();
		Connection conn = DatabaseConnection.getConnection();
		ResultSet results = conn.createStatement().executeQuery("SELECT COUNT(*) AS Frequency, * FROM "
				+ "(SELECT IMPRESSIONS.*, SERVER.* FROM IMPRESSIONS "
				+ "INNER JOIN SERVER ON IMPRESSIONS.ID=SERVER.ID "
				+ "GROUP BY SERVER.ENTRYDATE, SERVER.ID) AS SUBQUERY "
				+ "WHERE PAGES = 1 AND " + filter.getSql() + ";");

		tableContent.add(new dashboard.model.ObservableMetrics("Bounces",Integer.toString(results.getInt(1))));

		results = conn.createStatement().executeQuery("SELECT COUNT(*) AS Frequency, * FROM"
				+ "(SELECT IMPRESSIONS.*, CLICKS.* FROM IMPRESSIONS"
				+ " INNER JOIN CLICKS ON IMPRESSIONS.ID=CLICKS.ID"
				+ " GROUP BY CLICKS.DATE, CLICKS.ID) AS SUBQUERY"
				+ " WHERE " + filter.getSql() + ";");

		tableContent.add(new dashboard.model.ObservableMetrics("Clicks",Integer.toString(results.getInt(1))));

		results = conn.createStatement().executeQuery("SELECT COUNT(*) AS Frequency, * "
				+ "FROM (SELECT IMPRESSIONS.*, SERVER.* FROM "
				+ "IMPRESSIONS INNER JOIN SERVER ON IMPRESSIONS.ID=SERVER.ID "
				+ "GROUP BY SERVER.ENTRYDATE, SERVER.ID) AS SUBQUERY "
				+ "WHERE CONVERSION = 1 AND " + filter.getSql() + ";");

		tableContent.add(new dashboard.model.ObservableMetrics("Conversions",Integer.toString(results.getInt(1))));

		results = conn.createStatement().executeQuery("SELECT COUNT(*) AS Frequency, * FROM IMPRESSIONS WHERE " +  filter.getSql() +";");

		tableContent.add(new dashboard.model.ObservableMetrics("Impressions",Integer.toString(results.getInt(1))));

		results = conn.createStatement().executeQuery("SELECT COUNT(DISTINCT ID) AS Frequency, * FROM"
				+ "(SELECT IMPRESSIONS.*, CLICKS.* FROM IMPRESSIONS"
				+ " INNER JOIN CLICKS ON IMPRESSIONS.ID=CLICKS.ID"
				+ " GROUP BY CLICKS.DATE, CLICKS.ID) AS SUBQUERY"
				+ " WHERE " +  filter.getSql() + ";");

		tableContent.add(new dashboard.model.ObservableMetrics("Unique Clicks",Integer.toString(results.getInt(1))));

		results = conn.createStatement().executeQuery("SELECT COUNT(DISTINCT ID) AS Frequency, * FROM IMPRESSIONS WHERE " +  filter.getSql() +";");

		tableContent.add(new dashboard.model.ObservableMetrics("Unique Impressions",Integer.toString(results.getInt(1))));

		tableContent.add(new dashboard.model.ObservableMetrics("Total Cost","0.00"));
		tableContent.add(new dashboard.model.ObservableMetrics("CTR","...."));
		tableContent.add(new dashboard.model.ObservableMetrics("CPA","...."));
		tableContent.add(new dashboard.model.ObservableMetrics("CPC","...."));
		tableContent.add(new dashboard.model.ObservableMetrics("CPM","...."));

	}
	private void drawGraph(String metric)
	{
		lineChart.getData().clear();
		lineChart.getXAxis().setLabel(filterTime.getValue());  
		lineChart.getYAxis().setLabel(metric);
		GraphConstructor constructor;

		switch(metric) {
		default:
		case "Bounces":
			constructor = new BounceGraphConstructor(filter);
			break;
		case "Impressions":
			constructor = new ImpressionsGraphConstructor(filter);
			break;
		case "Clicks":
			constructor = new ClicksGraphConstructor(filter);
			break;
		case "Unique Impressions":
			constructor = new UniqueImpressionsGraphConstructor(filter);
			break;
		case "Unique Clicks":
			constructor = new UniqueClicksGraphConstructor(filter);
			break;
		case "Conversions":
			constructor = new ConversionGraphConstructor(filter);
			break;
		case "CPC":
			constructor = new CPCGraphConstructor(filter);
			break;
		case "CPA":
			constructor = new CPAGraphConstructor(filter);
			break;
		case "CPM":
			constructor = new CPMGraphConstructor(filter);
		}

		lineChart.setCreateSymbols(false);
		lineChart.setLegendVisible(false);
		updateGraph(constructor, metric, lineChart);

	}
	@FXML
	private void generateData(ActionEvent event) {
		filter.setGender(filterGender.getCheckModel().getCheckedItems());
		filter.setAge(filterAge.getCheckModel().getCheckedItems());
		filter.setIncome(filterIncome.getCheckModel().getCheckedItems()); 
		filter.setContext(filterContext.getCheckModel().getCheckedItems());
		filter.setDateFrom(filterDateFrom.getValue());
		filter.setDateTo(filterDateTo.getValue());
		
		drawGraph(filterMetrics.getValue());
		try {
			updateMetricsTable();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	@FXML
	private void openCampaignAction(ActionEvent event) {
		FileChooser fChooser = new FileChooser();
		fChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		fChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Campaign files (*.db)", "*.db"));
		fChooser.setTitle("Select campaign" );
		File s = fChooser.showOpenDialog(application.getStage());
		if (s != null) {  
			DatabaseConnection.closeConnection();
			DatabaseConnection.setDbfile(s.getPath());

			campaignName.setText(s.getName().replace(".db", ""));
			generateGraph.setDisable(false); 
			generateData(null);
		}
	}   
}