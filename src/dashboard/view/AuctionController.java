package dashboard.view;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;

import org.controlsfx.control.CheckComboBox;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import dashboard.controller.*;
import dashboard.model.*;
/**
 * Auction Controller.
 */
public class AuctionController extends AnchorPane {
	private Main application;
	
	@FXML private MenuBar menu;
	@FXML private MenuItem importCampaign;
	@FXML private MenuItem close;
	/* The controlsFX checkComboBox is not supported in scenebuilder. To be able 
	   to open the view in scenebuilder replace org.controlsfx.control.CheckComboBox with ComboBox
	   The .fxml file also needs to be edited to remove the org.controlsfx.control.CheckComboBox
	 */
	@FXML private CheckComboBox<String> filterGender;
	@FXML private CheckComboBox<String> filterAge;
	@FXML private CheckComboBox<String> filterIncome;
	@FXML private CheckComboBox<String> filterContext;
	@FXML private ComboBox<String> filterMetrics;
	@FXML private DatePicker filterDateFrom;
	@FXML private DatePicker filterDateTo;
	@FXML private Button generateGraph;
	@FXML private LineChart<Date,Number> lineChart;
	@FXML private ComboBox<String> filterTime;
	@FXML private Label campaignName;
	@FXML private MenuItem openCampaign;
	@FXML private TableView<ObservableMetrics> tableResults;
	@FXML private TableColumn<ObservableMetrics, String> metricCol;
	@FXML private TableColumn<ObservableMetrics, String> resultCol;

	final ObservableList<ObservableMetrics> tableContent = FXCollections.observableArrayList();
	private Filter filter;
	
	@FXML private MenuItem deleteCampaign;
	@FXML private MenuItem exportCampaign;
	@FXML private MenuItem printCampaign;
	@FXML private MenuItem mnuColour;
	@FXML private MenuItem mnuFont;
	@FXML private MenuItem mnuPaths;
	@FXML private MenuItem mnuUnits;
	@FXML private MenuItem mnuDefinitions;
	@FXML private MenuItem mnuSearch;
	@FXML private MenuItem mnuList;
	@FXML private MenuItem mnuAbout;
	@FXML private TextField txtBounceTime;
	@FXML private TextField txtBouncePages;
	@FXML private RadioButton rbByBounceTime;
	@FXML private ToggleGroup grBounce;
	@FXML private RadioButton rbByBouncePages;

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
		configureFilters();

		campaignName.setText(DatabaseConnection.getDbfile().replace(".db", ""));
		generateGraph.setDisable(false);
		//generateData(null);
	}
	
	private void configureFilters() {
		filterGender.getCheckModel().getCheckedItems().addListener(
			(ListChangeListener.Change<? extends String> c) -> filter.setGender(filterGender));
		filterAge.getCheckModel().getCheckedItems().addListener(
			(ListChangeListener.Change<? extends String> c) -> filter.setAge(filterAge));
		filterIncome.getCheckModel().getCheckedItems().addListener(
			(ListChangeListener.Change<? extends String> c) -> filter.setIncome(filterIncome));
		filterContext.getCheckModel().getCheckedItems().addListener(
			(ListChangeListener.Change<? extends String> c) -> filter.setContext(filterContext));
	}

	private final ListChangeListener<ObservableMetrics> tableSelectionChanged = (ListChangeListener.Change<? extends ObservableMetrics> c) -> {
		ObservableMetrics item = tableResults.getSelectionModel().getSelectedItem();
		if(item != null)
			drawGraph(item.getDescription());
	};

	// Configure the table widget: set up its column, and register the
	// selection changed listener.
	private void configureTable() {
		metricCol.setCellValueFactory(new PropertyValueFactory<>("description"));
		resultCol.setCellValueFactory(new PropertyValueFactory<>("result"));
		tableResults.setItems(tableContent);
		assert tableResults.getItems() == tableContent;

		tableResults.getSelectionModel().getSelectedItems().addListener(tableSelectionChanged);
	}
	
	@FXML private void importCampaignAction(ActionEvent event) {}

	@FXML
	private void closeAction(ActionEvent event) {
		DatabaseConnection.closeConnection();
		application.getStage().close();
	}

	public void updateGraph(GraphConstructor graphConstructor, String yLabel, LineChart<Date, Number> lineChart){
		try {
			Series<Date, Number> data = graphConstructor.fetchGraph();
			data.setName(yLabel);
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

		if (results.next())
			tableContent.add(new dashboard.model.ObservableMetrics("Bounces",results.getString(1)));

		results = conn.createStatement().executeQuery("SELECT COUNT(*) AS Frequency, * FROM"
				+ "(SELECT IMPRESSIONS.*, CLICKS.* FROM IMPRESSIONS"
				+ " INNER JOIN CLICKS ON IMPRESSIONS.ID=CLICKS.ID"
				+ " GROUP BY CLICKS.DATE, CLICKS.ID) AS SUBQUERY"
				+ " WHERE " + filter.getSql() + ";");

		if (results.next())
			tableContent.add(new dashboard.model.ObservableMetrics("Clicks",results.getString(1)));

		results = conn.createStatement().executeQuery("SELECT COUNT(*) AS Frequency, * "
				+ "FROM (SELECT IMPRESSIONS.*, SERVER.* FROM "
				+ "IMPRESSIONS INNER JOIN SERVER ON IMPRESSIONS.ID=SERVER.ID "
				+ "GROUP BY SERVER.ENTRYDATE, SERVER.ID) AS SUBQUERY "
				+ "WHERE CONVERSION = 1 AND " + filter.getSql() + ";");

		if (results.next())
			tableContent.add(new dashboard.model.ObservableMetrics("Conversions",results.getString(1)));

		results = conn.createStatement().executeQuery("SELECT COUNT(*) AS Frequency, * FROM IMPRESSIONS WHERE " +  filter.getSql() +";");

		if (results.next())
			tableContent.add(new dashboard.model.ObservableMetrics("Impressions",results.getString(1)));

		results = conn.createStatement().executeQuery("SELECT COUNT(DISTINCT ID) AS Frequency, * FROM"
				+ "(SELECT IMPRESSIONS.*, CLICKS.* FROM IMPRESSIONS"
				+ " INNER JOIN CLICKS ON IMPRESSIONS.ID=CLICKS.ID"
				+ " GROUP BY CLICKS.DATE, CLICKS.ID) AS SUBQUERY"
				+ " WHERE " +  filter.getSql() + ";");

		if (results.next())
			tableContent.add(new dashboard.model.ObservableMetrics("Unique Clicks",results.getString(1)));
		
		results = conn.createStatement().executeQuery("SELECT COUNT(DISTINCT ID) AS Frequency, * FROM IMPRESSIONS WHERE " +  filter.getSql() +";");

		if (results.next())
			tableContent.add(new dashboard.model.ObservableMetrics("Unique Impressions",results.getString(1)));

		results = conn.createStatement().executeQuery("SELECT CLICKCOST, IMPCOST FROM "
				+ "(SELECT CLICKDATE, SUM(CLICKCOST) AS CLICKCOST FROM "
				+ "(SELECT IMPRESSIONS.*, CLICKS.DATE AS CLICKDATE, CLICKS.ID, CLICKS.COST AS CLICKCOST "
				+ "FROM IMPRESSIONS "
				+ "INNER JOIN CLICKS "
				+ "ON IMPRESSIONS.ID=CLICKS.ID "
				+ "GROUP BY CLICKS.DATE, CLICKS.ID)"
				+ "WHERE " +  filter.getSql().replace("DATE","CLICKDATE") + ") "
				+ "INNER JOIN "
				+ "(SELECT  SUM(COST) AS IMPCOST FROM IMPRESSIONS "
				+ "WHERE " +  filter.getSql() + ")");
		

		if (results.next())
			tableContent.add(new dashboard.model.ObservableMetrics("Total Cost",Integer.toString(results.getInt(1)+results.getInt(2))));
		
		results = conn.createStatement().executeQuery("SELECT NUMCLICKS, NUMIMP FROM "
				+ "(SELECT SUBSTR(DATE, 0, 14) as CLICKDATE, COUNT(*) AS NUMCLICKS FROM "
				+ "(SELECT CLICKS.DATE AS CLICKDATE, IMPRESSIONS.* FROM CLICKS INNER JOIN IMPRESSIONS ON CLICKS.ID=IMPRESSIONS.ID GROUP BY CLICKS.ID, CLICKDATE) "
				+ "WHERE " +  filter.getSql().replace("DATE","CLICKDATE") + ") "
				+ "INNER JOIN "
				+ "(SELECT SUBSTR(DATE, 0, 14) AS DATE, COUNT(*) AS NUMIMP FROM IMPRESSIONS WHERE " +  filter.getSql() + ") "
				+ "ON DATE=CLICKDATE");
		
		if (results.next()) 
			tableContent.add(new dashboard.model.ObservableMetrics("CTR",Float.toString(results.getInt(1)/(float)results.getInt(2))));
		
		
		results = conn.createStatement().executeQuery("SELECT SUM(CLICKCOST), SUM(IMPCOST), SUM(Frequency) FROM "
				+ "(SELECT SUBSTR(ENTRYDATE, 0, 14) AS ENTRYDATE,COUNT(*) AS Frequency "
				+ "FROM (SELECT IMPRESSIONS.*, SERVER.* FROM "
				+ "IMPRESSIONS INNER JOIN SERVER ON IMPRESSIONS.ID=SERVER.ID "
				+ "GROUP BY SERVER.ENTRYDATE, SERVER.ID) AS SUBQUERY "
				+ "WHERE CONVERSION = 1 "
				+ "GROUP BY SUBSTR(ENTRYDATE, 0, 14))"
				+ "INNER JOIN "
				+ "(SELECT CLICKDATE, CLICKCOST, IMPCOST FROM "
				+ "(SELECT SUBSTR(CLICKDATE, 0, 14) AS CLICKDATE, SUM(CLICKCOST) AS CLICKCOST FROM "
				+ "(SELECT IMPRESSIONS.*, CLICKS.ID, CLICKS.DATE AS CLICKDATE, CLICKS.COST AS CLICKCOST "
				+ "FROM IMPRESSIONS "
				+ "INNER JOIN CLICKS "
				+ "ON IMPRESSIONS.ID=CLICKS.ID "
				+ "GROUP BY CLICKS.DATE, CLICKS.ID) "
				+ "GROUP BY SUBSTR(CLICKDATE, 0, 14)) "
				+ "INNER JOIN "
				+ "(SELECT SUBSTR(DATE,0,14) AS IMPDATE, SUM(COST) AS IMPCOST FROM IMPRESSIONS "
				+ "GROUP BY SUBSTR(DATE, 0, 14)) "
				+ "ON IMPDATE=CLICKDATE) "
				+ "ON CLICKDATE=ENTRYDATE;");
		
		if (results.next())
			tableContent.add(new dashboard.model.ObservableMetrics("CPA",Float.toString((results.getInt(1) + results.getInt(2)) / (float)results.getInt(3))));
		
		results = conn.createStatement().executeQuery("SELECT SUM(CLICKCOST), SUM(IMPCOST), SUM(NUMCLICKS) FROM "
				+ "(SELECT SUBSTR(CLICKDATE, 0, 14) AS CLICKDATE, SUM(CLICKCOST) AS CLICKCOST, COUNT(ID) AS NUMCLICKS FROM "
				+ "(SELECT IMPRESSIONS.*, CLICKS.ID, CLICKS.DATE AS CLICKDATE, CLICKS.COST AS CLICKCOST "
				+ "FROM IMPRESSIONS "
				+ "INNER JOIN CLICKS "
				+ "ON IMPRESSIONS.ID=CLICKS.ID "
				+ "GROUP BY CLICKS.DATE, CLICKS.ID) "
				+ "WHERE " + filter.getSql().replace("DATE", "CLICKDATE")+ " GROUP BY SUBSTR(CLICKDATE, 0, 14)) "
				+ "INNER JOIN "
				+ "(SELECT SUBSTR(DATE,0,14) AS IMPDATE, SUM(COST) AS IMPCOST FROM IMPRESSIONS "
				+ "WHERE " + filter.getSql()+ " GROUP BY SUBSTR(DATE, 0, 14)) "
				+ "ON IMPDATE=CLICKDATE");
		
		if (results.next())
			tableContent.add(new dashboard.model.ObservableMetrics("CPC",Float.toString((results.getInt(1) + results.getInt(2)) / (float)results.getInt(3))));
		
		results = conn.createStatement().executeQuery("SELECT AVG(COST) AS IMPCOST FROM IMPRESSIONS "
				+ "WHERE " + filter.getSql());
		
		if (results.next())
			tableContent.add(new dashboard.model.ObservableMetrics("CPM",Float.toString(results.getFloat(1)*1000)));
		
		results = conn.createStatement().executeQuery("SELECT SUM(NUMCLICKS), SUM(NUMBOUNCES) FROM "
				+ "(SELECT SUBSTR(CLICKDATE, 0, 14) as CLICKDATE, COUNT(ID) AS NUMCLICKS FROM "
				+ "(SELECT CLICKS.DATE AS CLICKDATE, IMPRESSIONS.* FROM CLICKS INNER JOIN IMPRESSIONS ON CLICKS.ID=IMPRESSIONS.ID GROUP BY CLICKS.ID, CLICKDATE) "
				+ "WHERE " + filter.getSql().replace("DATE", "CLICKDATE")+ " GROUP BY SUBSTR(CLICKDATE, 0, 14)) "
				+ "INNER JOIN "
				+ "(SELECT SUBSTR(ENTRYDATE, 0, 14) AS DATE, COUNT(*) AS NUMBOUNCES FROM "
				+ "(SELECT IMPRESSIONS.*, SERVER.* FROM IMPRESSIONS "
				+ "INNER JOIN SERVER ON IMPRESSIONS.ID=SERVER.ID "
				+ "GROUP BY SERVER.ENTRYDATE, SERVER.ID) AS SUBQUERY "
				+ "WHERE PAGES=1 AND "+ filter.getSql().replace("DATE", "ENTRYDATE")
				+ " GROUP BY SUBSTR(ENTRYDATE, 0, 14)) "
				+ "ON DATE=CLICKDATE GROUP BY DATE");
		
		if (results.next())
			tableContent.add(new dashboard.model.ObservableMetrics("Bounce Rate",String.format("%.1f%%", results.getInt(2)/results.getFloat(1)*100)));
		
		results.close();
	}
	private void drawGraph(String metric) {
		//lineChart.getData().clear();
		//lineChart.getXAxis().setLabel(filterTime.getValue());  
		lineChart.getYAxis().setLabel("Number");
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
				break;
			case "CTR":
				constructor = new CTRGraphConstructor(filter);
				break;
			case "Total Cost":
				constructor = new TotalCostGraphConstructor(filter);
				break;
			case "Bounce Rate":
				constructor = new BounceRateGraphConstructor(filter);
				break;
		}

		lineChart.setCreateSymbols(false);
		//lineChart.setLegendVisible(false);
		updateGraph(constructor, metric, lineChart);

	}
	
	@FXML
	private void generateData(ActionEvent event) {
		filter.setDateFrom(filterDateFrom.getValue());
		filter.setDateTo(filterDateTo.getValue());
				
		lineChart.getData().clear();
		lineChart.getXAxis().setLabel(filterTime.getValue());  
		lineChart.getYAxis().setLabel(filterMetrics.getValue());
				
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