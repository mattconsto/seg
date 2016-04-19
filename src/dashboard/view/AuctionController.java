package dashboard.view;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;

import org.controlsfx.control.CheckComboBox;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import dashboard.controller.*;
import dashboard.model.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Pos;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;
/**
 * Auction Controller.
 */

public class AuctionController extends AnchorPane {
	private Main application;
	private Preferences preferences = Preferences.userRoot();
	
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
	//@FXML private ComboBox<String> filterMetrics;
	@FXML private DatePicker filterDateFrom;
	@FXML private DatePicker filterDateTo;
	@FXML private Button generateGraph;
	@FXML private LineChart<Date,Number> lineChart;
	@FXML private ComboBox<String> filterTime;
	@FXML private MenuItem openCampaign;
	@FXML private TableView<ObservableMetrics> tableResults;
	@FXML private TableColumn<ObservableMetrics, String> metricCol;
	private ObservableList<ObservableMetrics> tableMetrics = FXCollections.observableArrayList();
	 
	private HashMap<String, Filter> filters;
	private Filter filter;
	private BounceFilter bounceFilter;
	
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
	@FXML private FeedbackRestrictiveTextField txtBounceTime;
	@FXML private FeedbackRestrictiveTextField txtBouncePages;
	@FXML private RadioButton rbByBounceTime;
	@FXML private ToggleGroup grBounce;
	@FXML private RadioButton rbByBouncePages;
	@FXML private TextField txtFilterName;
	@FXML private ComboBox<String> cbCampaign;
	@FXML private TableColumn<ObservableMetrics, Boolean> selectCol;
	
	@FXML private SplitPane splitPane;
	
	private MetricsUpdater updaterRunnable;
	
	private FileChooser fileChooser;

	private HashMap<String, Series<Date, Number>> graphData = new HashMap<String, Series<Date, Number>>();
	public void setApp(Main application){
		this.application = application;
	}

	public void init() {
		filters = new HashMap<>();
		bounceFilter = new BounceFilter();
		
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
		
		fileChooser = new FileChooser();
			 
		configureTable();  
		configureFilters();

		application.getStage().setTitle(preferences.get("ProductName", "Ad Auction Dashboard") + " - " + DatabaseConnection.getDbfile().replace(".db", ""));
		generateGraph.setDisable(false);
		
		rbByBounceTime.setUserData("timeBounce");
		rbByBouncePages.setUserData("pageBounce");
		grBounce.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observableValue, Toggle t, Toggle arg2) {
				System.out.println(grBounce.getSelectedToggle().getUserData().toString());
			}
		});
		txtBounceTime.addListener(e -> System.err.println("Numbers Only!"));
		txtBouncePages.addListener(e -> System.err.println("Numbers Only!"));
		fillCampaignList();
		Platform.runLater(() -> splitPane.setDividerPosition(0, 0.175));
		
		txtFilterName.setText(GenerateName.generate());
	}
	
	private void configureFilters() {
		// if (filters.size() <= iFilter)
		//	 filters.add(new Filter());
			filter = new Filter();
		filterGender.getCheckModel().getCheckedItems().addListener(
			(ListChangeListener.Change<? extends String> c) -> filter.setGender(filterGender));
		filterAge.getCheckModel().getCheckedItems().addListener(
			(ListChangeListener.Change<? extends String> c) -> filter.setAge(filterAge));
		filterIncome.getCheckModel().getCheckedItems().addListener(
			(ListChangeListener.Change<? extends String> c) -> filter.setIncome(filterIncome));
		filterContext.getCheckModel().getCheckedItems().addListener(
			(ListChangeListener.Change<? extends String> c) -> filter.setContext(filterContext));
		filterTime.valueProperty().addListener(c -> filter.setTime(filterTime.getValue()));
	}
	
	public void initMetricTable() {	
		TableMenu.addCustomTableMenu(tableResults);
		tableMetrics.clear();
		tableMetrics.add(new ObservableMetrics("Bounces"));
		tableMetrics.add(new ObservableMetrics("Clicks"));
		tableMetrics.add(new ObservableMetrics("Conversions"));
		tableMetrics.add(new ObservableMetrics("Impressions"));
		tableMetrics.add(new ObservableMetrics("Unique Clicks"));
		tableMetrics.add(new ObservableMetrics("Unique Impressions"));
		tableMetrics.add(new ObservableMetrics("Total Cost"));
		tableMetrics.add(new ObservableMetrics("Click Through Rate"));
		tableMetrics.add(new ObservableMetrics("Cost Per Aquisition"));
		tableMetrics.add(new ObservableMetrics("Cost Per Click"));
		tableMetrics.add(new ObservableMetrics("Cost Per Mille"));	
		tableMetrics.add(new ObservableMetrics("Bounce Rate"));
	}
		
	private void fillCampaignList() {	
		File[] files = new File(System.getProperty("user.dir")).listFiles();
		Arrays.sort(files, (a, b) -> (int) (b.lastModified() - a.lastModified()));
		
		for(File file : files) {
			if(file.isFile() && file.getName().toLowerCase().endsWith(".db"))
				cbCampaign.getItems().add(file.getName().replace(".db", ""));
		}
		cbCampaign.setValue(DatabaseConnection.getDbfile().replace(".db", ""));		
	} 
	
	private void addColumn(String colName) {
		TableColumn<ObservableMetrics, String> tc = new TableColumn<ObservableMetrics, String>(colName);
		tc.setMaxWidth(100);
		tc.setMinWidth(100);
		final int colNo = filters.size();
		tc.setCellValueFactory(new Callback<CellDataFeatures<ObservableMetrics, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<ObservableMetrics, String> p) {
				return new SimpleStringProperty(p.getValue().getResults(colNo));
			}
		});
		tableResults.getColumns().add(tc);
	}
	
	// Configure the table widget: set up its column, and register the
	// selection changed listener.
	private void configureTable() {
		tableResults.getColumns().clear();
		TableColumn<ObservableMetrics,Boolean>  checkCol = new TableColumn<>("Show");
		checkCol.setMinWidth(50);
		checkCol.setMaxWidth(50);
		checkCol.setCellValueFactory( new PropertyValueFactory<ObservableMetrics,Boolean>( "select" ) );
		checkCol.setCellFactory( new Callback<TableColumn<ObservableMetrics,Boolean>, TableCell<ObservableMetrics,Boolean>>() {
			@Override
			public TableCell<ObservableMetrics,Boolean> call( TableColumn<ObservableMetrics,Boolean> param ) {
				return new CheckBoxTableCell<ObservableMetrics,Boolean>(){
				{ setAlignment( Pos.CENTER );}
				@Override
				public void updateItem( Boolean item, boolean empty ){
					if ( ! empty ) {
						TableRow<?>  row = getTableRow();
						if ( row != null ) {
						int rowNo = row.getIndex();
						TableViewSelectionModel<?>  s = getTableView().getSelectionModel();
						if ( item ) {
							updateGraph(  getTableView().getItems().get(rowNo).getDescription());
							s.select( rowNo );
						}
						else  {
							removeGraph(  getTableView().getItems().get(rowNo).getDescription());
							s.clearSelection( rowNo );
						}
						}
					}
					super.updateItem( item, empty );
				}
				};
			}
		} );		 
		tableResults.getColumns().add(checkCol);
		metricCol = new TableColumn<>("Metric");
		metricCol.setMinWidth(150);
		metricCol.setMaxWidth(150);
		metricCol.setCellValueFactory(new PropertyValueFactory<>("description")); 
		tableResults.getColumns().add(metricCol);
		tableResults.setItems(tableMetrics);
	}

	
	@FXML private void openAbout(ActionEvent event) {
		Alert about = new Alert(AlertType.INFORMATION);
		about.setTitle(preferences.get("ProductName", "Ad Auction Dashboard") + " - About");
		about.setHeaderText("About");
		about.setContentText(
			"Created by SEG Team 3 2016:\n" + 
			"\n" + 
			"• Samuel Beresford\n" + 
			"• Matthew Consterdine\n" +
			"• Emma Gadsby\n" +
			"• Matthew Langford\n" +
			"• Iovana Pavlovici\n"
		);
		about.show();
	}
	
	@FXML private void showDefintions(ActionEvent event) {
		Alert about = new Alert(AlertType.INFORMATION);
		about.setTitle(preferences.get("ProductName", "Ad Auction Dashboard") + " - Definitions");
		about.setHeaderText("Definitions");
		about.setContentText(
			"Acquisition: same as conversion.\n" + 
			"Bounce: A user clicks on an ad, but then fails to interact with the website (typically detected when a user navigates away from the website after a short time, or when only a single page has been viewed).\n" + 
			"Bounce Rate: The average number of bounces per click.\n" + 
			"Campaign: An effort by the marketing agency to gain exposure for a client’s website by participating in a range of ad auctions offered by different providers and networks. Bid amounts, keywords and other variables will be tailored to the client’s needs.\n" + 
			"Click: A click occurs when a user clicks on an ad that is shown to them.\n" + 
			"Click Cost: The cost of a particular click (usually determined through an auction process).\n" + 
			"Click-through-rate (CTR): The average number of clicks per impression.\n" + 
			"Conversion: A conversion, or acquisition, occurs when a user clicks and then acts on an ad. The specific definition of an action depends on the campaign (e.g., buying a product, registering as a new customer or joining a mailing list).\n" + 
			"Conversion Rate: The average number of conversions per click.\n" + 
			"Cost-per-acquisition (CPA): The average amount of money spent on an advertising campaign for each acquisition (i.e., conversion).\n" + 
			"Cost-per-click (CPC): The average amount of money spent on an advertising campaign for each click.\n" + 
			"Cost-per-thousand impressions (CPM): The average amount of money spent on an advertising campaign for every one thousand impressions.\n" + 
			"Impression: An impression occurs whenever an ad is shown to a user, regardless of whether they click on it.\n" + 
			"Uniques: The number of unique users that click on an ad during the course of a campaign."
		);
		about.setHeight(625);
		about.setWidth(800);
		about.show();
		about.setHeight(625);
		about.setWidth(800);
	}
	
	@FXML private void importCampaignAction(ActionEvent event) {
		if(updaterRunnable != null) updaterRunnable.stop();
		application.getStage().setMaximized(false);
		application.start(application.getStage());
	}
	
	@FXML private void printGraph(ActionEvent event) {
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.HARDWARE_MINIMUM);
        double scaleX = pageLayout.getPrintableWidth() / lineChart.getBoundsInParent().getWidth();
        double scaleY = pageLayout.getPrintableHeight() / lineChart.getBoundsInParent().getHeight();
        lineChart.getTransforms().add(new Scale(scaleX, scaleY));

		PrinterJob job = PrinterJob.createPrinterJob();
		if (job != null && job.showPrintDialog(lineChart.getScene().getWindow())){
		    boolean success = job.printPage(pageLayout, lineChart);
		    if (success) {
		        job.endJob();
		    }
		}
		
		lineChart.getTransforms().clear();
	}
	
	@FXML private void saveGraphAs(ActionEvent event) {
		configureFileChooser(fileChooser);
		WritableImage snapshot = lineChart.snapshot(new SnapshotParameters(), null);
		File newFile = fileChooser.showSaveDialog(application.getStage());
		if(newFile != null) {
			try
			{
				if(!newFile.getName().endsWith(".png")) newFile = new File(newFile.getPath() + ".png");
				ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", newFile);
			}
			catch(IOException e)
			{
				//TODO: Error dialog!
				System.err.println("Could not save image.");
			}
		}
	}
	
	private static void configureFileChooser(final FileChooser fc){
		fc.setTitle("Save graph as...");
		fc.setInitialDirectory(new File(System.getProperty("user.dir")));
		fc.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("PNG", "*.png*")
		);
	}

	@FXML
	private void closeAction(ActionEvent event) {
		if(updaterRunnable != null) updaterRunnable.stop();
		DatabaseConnection.closeConnection();
		application.getStage().close();
	}
	
	@FXML
	private void generateData(ActionEvent event) {
		if(updaterRunnable != null) updaterRunnable.stop();
			// todo - check valid entry for name and campaign
		 if (filters.size() == 10 ) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Too many filters");
                    alert.setHeaderText(null);
                    alert.setContentText("You are only allowed upto 10 filters at a time.");
                    alert.showAndWait();
                }
                else if ( txtFilterName.getText().isEmpty()) { 
                       
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Please enter a campaign name");
                    alert.setHeaderText(null);
                    alert.setContentText("Please enter a campaign name");
                    alert.showAndWait();
                }
                else if  (filters.containsKey(txtFilterName.getText())) {
                     Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Campaign name in use");
                    alert.setHeaderText(null);
                    alert.setContentText("This campaign name has already been used. Please enter a unique campaign name");
                    alert.showAndWait();
                }
                else {
				filter.setDescription(txtFilterName.getText());
				filter.setCampaign(cbCampaign.getValue());
				filter.setDateFrom(filterDateFrom.getValue());
				filter.setDateTo(filterDateTo.getValue());
				bounceFilter.setTimeLimit(0);
				bounceFilter.setPageLimit(0);
				if(grBounce.getSelectedToggle().getUserData().toString().equalsIgnoreCase("timeBounce")){
					try{
						System.out.println("Time: "+txtBounceTime.getText());
						if(!txtBounceTime.getText().isEmpty())
							bounceFilter.setTimeLimit(Integer.parseInt(txtBounceTime.getText()));
						else
							bounceFilter.setTimeLimit(10);
					}catch(NumberFormatException nfe){
						bounceFilter.setTimeLimit(10);
					}
				}
				else{
					try{
						System.out.println("Pages: "+txtBouncePages.getText());
						if(!txtBouncePages.getText().isEmpty())
							bounceFilter.setPageLimit(Integer.parseInt(txtBouncePages.getText()));
						else
							bounceFilter.setPageLimit(1);
					}catch(NumberFormatException nfe){
						bounceFilter.setPageLimit(1);
					}
				}
				//updateGraph(filterMetrics.getValue());
				addColumn(txtFilterName.getText());
				TableMenu.addCustomTableMenu(tableResults);
				tableResults.getColumns().get(filters.size()).setVisible(true);

				// Cheap and nasty threading
				updaterRunnable = new MetricsUpdater(tableMetrics, filter, bounceFilter, filters.size(),  tableResults);
				 filters.put(txtFilterName.getText(), filter);
				 new Thread(updaterRunnable).start();

				configureFilters();
				txtFilterName.setText(GenerateName.generate());
			}
	}
	
	@FXML private void clearData(ActionEvent event) {
		if(updaterRunnable != null)
			updaterRunnable.stop();
		lineChart.getData().clear();
		filters.clear();
		graphData.clear();
		configureTable();
			
		tableMetrics.clear();
		initMetricTable();
		lineChart.getXAxis().setTickLabelsVisible(false);
		GenerateName.generate();
	}
	
	private void removeGraph(String metric){
		for(Map.Entry<String, Filter> f : filters.entrySet()){  
			String key = f.getKey() + " : " + metric;
			if (graphData.containsKey(key))
				lineChart.getData().remove(graphData.get(key));
		}
	}

	private void updateGraph(String metric) {
		GraphConstructor constructor;
			lineChart.setCreateSymbols(false);  
			lineChart.setLegendVisible(true);
			Series<Date, Number> data = null;
			String key;
			int i = 2;
			 for(Map.Entry<String, Filter> f : filters.entrySet()){  

				if (tableResults.getColumns().get(i).isVisible()) {
				key = f.getKey() + " : " + metric;
				if (graphData.containsKey(key)) {
					data = graphData.get(key);
					if (!lineChart.getData().contains(data))
						lineChart.getData().add(data);
				}
				else {
					switch(metric) {
						default:
						case "Bounces":		constructor = new BounceGraphConstructor(f.getValue(), bounceFilter);	 break;
						case "Impressions":		constructor = new ImpressionsGraphConstructor(f.getValue());		  break;
						case "Clicks":		 constructor = new ClicksGraphConstructor(f.getValue());			   break;
						case "Unique Impressions": constructor = new UniqueImpressionsGraphConstructor(f.getValue());		break;
						case "Unique Clicks":	  constructor = new UniqueClicksGraphConstructor(f.getValue());		 break;
						case "Conversions":		constructor = new ConversionGraphConstructor(f.getValue());		   break;
						case "Cost Per Click":
						case "CPC":			constructor = new CPCGraphConstructor(f.getValue());				  break;
						case "Cost Per Aquisition":
						case "CPA":			constructor = new CPAGraphConstructor(f.getValue());				  break;
						case "Cost Per Mille":
						case "CPM":			constructor = new CPMGraphConstructor(f.getValue());				  break;
						case "Click Through Rate":
						case "CTR":			constructor = new CTRGraphConstructor(f.getValue());				  break;
						case "Total Cost":		 constructor = new TotalCostGraphConstructor(f.getValue());			break;
						case "Bounce Rate":		constructor = new BounceRateGraphConstructor(f.getValue(), bounceFilter); break;
					}

					try {
						data = constructor.fetchGraph();
						data.setName(key);
						lineChart.getData().add(data);
						graphData.put(key, data);


					} catch (SQLException e) {
						System.err.println("Unable to fetch data from database: " + e.getMessage());
					}
				}
				} else {
				key = f.getKey() + " : " + metric;
				if (graphData.containsKey(key))
					lineChart.getData().remove(graphData.get(key));
				}
				i++;
			}
		   lineChart.setCreateSymbols(false);
		   lineChart.setLegendVisible(true);
		   lineChart.getXAxis().setTickLabelsVisible(true);
	}
	
	@FXML private void openCampaignAction(ActionEvent event) {
		FileChooser fChooser = new FileChooser();
		fChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		fChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Campaign files (*.db)", "*.db"));
		fChooser.setTitle("Select campaign" );
		File s = fChooser.showOpenDialog(application.getStage());
		if (s != null) {  
			DatabaseConnection.closeConnection();
			DatabaseConnection.setDbfile(s.getPath());
			application.getStage().setTitle(preferences.get("ProductName", "Ad Auction Dashboard") + " - " + DatabaseConnection.getDbfile().replace(".db", ""));
			generateGraph.setDisable(false); 
			//generateData(null);
		}
	}   
}