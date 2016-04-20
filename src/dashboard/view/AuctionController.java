package dashboard.view;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Date;

import org.controlsfx.control.CheckComboBox;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import dashboard.controller.*;
import dashboard.model.*;
import extfx.scene.control.RestrictiveTextField;

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
import javafx.scene.input.MouseEvent;
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
	 
	private HashMap<String, Filter> filters = new HashMap<>();
	private Filter filter;
	private BounceFilter bounceFilter  = new BounceFilter();
	
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
	@FXML private RestrictiveTextField txtBounceTime;
	@FXML private RestrictiveTextField txtBouncePages;
	@FXML private RadioButton rbByBounceTime;
	@FXML private ToggleGroup grBounce;
	@FXML private RadioButton rbByBouncePages;
	@FXML private TextField txtFilterName;
	@FXML private Label txtFilterDesc;
	@FXML private ComboBox<String> cbCampaign;
	@FXML private TableColumn<ObservableMetrics, Boolean> selectCol;
	
	@FXML private SplitPane splitPane;
	 
        private MetricsUpdater updaterRunnable = null;
	
	private FileChooser fileChooser = new FileChooser();

	private HashMap<String, Series<Date, Number>> graphData = new HashMap<String, Series<Date, Number>>();
	public void setApp(Main application){
		this.application = application;
	}

	public void init() {
		filterDateFrom.setValue((LocalDate.of(2015,01,01)));
		filterDateTo.setValue((LocalDate.of(2015,01,14)));
		filterGender.getCheckModel().check(0);
		filterAge.getCheckModel().check(0);
		filterContext.getCheckModel().check(0);
		filterIncome.getCheckModel().check(0);
			 
		configureTable();  
		configureFilters();

		application.getStage().setTitle(preferences.get("ProductName", "Ad Auction Dashboard") + " - " + DatabaseConnection.getDbfile().replace(".db", ""));
		
		rbByBounceTime.setUserData("timeBounce");
		rbByBouncePages.setUserData("pageBounce");
		grBounce.selectedToggleProperty().addListener(((ObservableValue<? extends Toggle> v, Toggle t, Toggle q) -> System.out.println(grBounce.getSelectedToggle().getUserData().toString())));
		fillCampaignList();
		Platform.runLater(() -> splitPane.setDividerPosition(0, 0.175));
		
		txtFilterName.setText(GenerateName.generate());
		
		updatePreferences(preferences.get("Graph_Colour", "Default"), preferences.getBoolean("Graph_Icons", true), preferences.getBoolean("Graph_Dash", false), preferences.get("Font_Size", "Medium"), preferences.get("Currency_Symbol", new DecimalFormat().getDecimalFormatSymbols().getCurrencySymbol()));
	}
	
	private void configureFilters() {
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
		//tc.setMaxWidth(100);
		//tc.setMinWidth(100);
		final int colNo = filters.size();
		tc.setCellValueFactory(new Callback<CellDataFeatures<ObservableMetrics, String>, ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(CellDataFeatures<ObservableMetrics, String> p) {
				return new SimpleStringProperty(p.getValue().getResults(colNo));
			}
		});

		tc.setSortable(false);

		Callback<TableColumn<ObservableMetrics, String>, TableCell<ObservableMetrics, String>> cellFactory = new Callback<TableColumn<ObservableMetrics, String>, TableCell<ObservableMetrics, String>>() {
			@Override
			public TableCell<ObservableMetrics, String> call(TableColumn<ObservableMetrics, String> p) {
				TableCell<ObservableMetrics, String> cell = new TableCell<ObservableMetrics, String>() {
					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						setText(empty ? null : getString());
						setGraphic(null);
					}

					private String getString() {
						return getItem() == null ? "" : getItem().toString();
					}
				};

				cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent event) {
						// if (event.getClickCount() > 1) {
						System.out.println("double clicked!");
						@SuppressWarnings("unchecked")
						TableCell<ObservableMetrics, String> c = (TableCell<ObservableMetrics, String>) event
								.getSource();
						int i = c.getIndex();
						if (i > 1) {
							Filter f = filters.get(c.getTableColumn().getText());
							txtFilterDesc.setText(c.getTableColumn().getText() + " - Campaign : " + f.getCampaign()
									+ "\nFilter: " + f.toString());
						}
						// }
					}
				});
				return cell;
			}
		};
		tc.setCellFactory(cellFactory);
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
					if ( !empty ) {
						TableRow<?> row = getTableRow();
						if ( row != null ) {
							int rowNo = row.getIndex();
							TableViewSelectionModel<?> s = getTableView().getSelectionModel();
							if ( item ) {
								updateGraph( getTableView().getItems().get(rowNo).getDescription());
								s.select( rowNo );
							} else {
								removeGraph( getTableView().getItems().get(rowNo).getDescription());
								s.clearSelection( rowNo );
							}
						}
					}
					super.updateItem( item, empty );
				}
				};
			}
				});
		checkCol.setSortable(false);
		tableResults.getColumns().add(checkCol);
		metricCol = new TableColumn<>("Metric");
		metricCol.setMinWidth(120);
		// metricCol.setMaxWidth(150);
		metricCol.setCellValueFactory(new PropertyValueFactory<>("description"));
		metricCol.setSortable(false);
		tableResults.getColumns().add(metricCol);
		tableResults.setItems(tableMetrics);

	}

	private void setMetricsTableSize(int rowHeight) {
		tableResults.setFixedCellSize(rowHeight);

		tableResults.prefHeightProperty().bind(tableResults.fixedCellSizeProperty().multiply(13.10));
		tableResults.minHeightProperty().bind(tableResults.prefHeightProperty());
		tableResults.maxHeightProperty().bind(tableResults.prefHeightProperty());
	}
	
	@FXML private void openAbout(ActionEvent event) {
		Alert about = new Alert(AlertType.INFORMATION);
		about.setTitle(preferences.get("ProductName", "Ad Auction Dashboard") + " - About");
		about.setHeaderText("About");
		about.setContentText(
			"Created by SEG Team 3 2016:\n" + 
			"\n" + 
			"> Samuel Beresford\n" + 
			"> Matthew Consterdine\n" +
			"> Emma Gadsby\n" +
			"> Matthew Langford\n" +
			"> Iovana Pavlovici\n"
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
		//if(updaterRunnable != null) updaterRunnable.stop();
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
		fileChooser.setTitle("Save graph as...");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		fileChooser.getExtensionFilters().clear();
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("PNG", "*.png")
		);
		
		WritableImage snapshot = lineChart.snapshot(new SnapshotParameters(), null);
		File newFile = fileChooser.showSaveDialog(application.getStage());
		if(newFile != null) {
			try
			{
				if(!newFile.getName().endsWith(".png")) newFile = new File(newFile.getPath() + ".png");
				ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", newFile);
				Desktop.getDesktop().open(newFile);
			}
			catch(IOException e)
			{
				//TODO: Error dialog!
				System.err.println("Could not save image.");
			}
		}
	}

	@FXML
	private void closeAction(ActionEvent event) {
		if(updaterRunnable != null) updaterRunnable.stop();
		DatabaseConnection.closeConnection();
		application.getStage().close();
	}
	
	@FXML
	private void showPrefDialog() {
		new PreferencesDialog(this, application.getStage());
	}
	
	protected void updatePreferences(String graphColour, boolean graphIcons, boolean graphDash, String fontSize, String currency) {
		Scene mainScene = application.getStage().getScene();
		mainScene.getStylesheets().clear();
		
		switch(graphColour)
		{
		default:
		case "Default":
			mainScene.getStylesheets().add("/dashboard/view/fxml/GraphDefault.css");
			break;
		case "HighContrast":
			mainScene.getStylesheets().add("/dashboard/view/fxml/GraphHighContrast.css");
			break;
		}
		
//		lineChart.setCreateSymbols(graphIcons);
		
		switch(fontSize)
		{
		case "Small":
			mainScene.getStylesheets().add("/dashboard/view/fxml/SmallFont.css");
                        setMetricsTableSize(20); 
			break;
		default:
		case "Med":
			mainScene.getStylesheets().add("/dashboard/view/fxml/MedFont.css");
                        setMetricsTableSize(25); 
			break;
		case "Large":
			mainScene.getStylesheets().add("/dashboard/view/fxml/LargeFont.css");
                        setMetricsTableSize(35); 
			break;
		}
		 
		preferences.put("Graph_Colour", graphColour);
		preferences.putBoolean("Graph_Icons", graphIcons);
		preferences.putBoolean("Graph_Dash", graphDash);
		preferences.put("Font_Size", fontSize);
		preferences.put("Currency_Symbol", currency);
	}
	
	@FXML
	private void generateData(ActionEvent event) {
		// if(updaterRunnable != null) updaterRunnable.stop();
		// todo - check valid entry for name and campaign
		if (filters.size() == 10) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Too many filters");
			alert.setHeaderText(null);
			alert.setContentText("You are only allowed upto 10 filters at a time.");
			alert.showAndWait();
		} else if (txtFilterName.getText().isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Please enter a campaign name");
			alert.setHeaderText(null);
			alert.setContentText("Please enter a campaign name");
			alert.showAndWait();
		} else if (filters.containsKey(txtFilterName.getText())) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Campaign name in use");
			alert.setHeaderText(null);
			alert.setContentText("This campaign name has already been used. Please enter a unique campaign name");
			alert.showAndWait();
		} else {
			filter.setDescription(txtFilterName.getText());
			filter.setCampaign(cbCampaign.getValue());
			filter.setDateFrom(filterDateFrom.getValue());
			filter.setDateTo(filterDateTo.getValue());
			bounceFilter.setTimeLimit(0);
			bounceFilter.setPageLimit(0);
			if (grBounce.getSelectedToggle().getUserData().toString().equalsIgnoreCase("timeBounce")) {
				try {
					System.out.println("Time: " + txtBounceTime.getText());
					if (!txtBounceTime.getText().isEmpty())
						bounceFilter.setTimeLimit(Integer.parseInt(txtBounceTime.getText()));
					else
						bounceFilter.setTimeLimit(10);
				} catch (NumberFormatException nfe) {
					bounceFilter.setTimeLimit(10);
				}
			} else {
				try {
					System.out.println("Pages: " + txtBouncePages.getText());
					if (!txtBouncePages.getText().isEmpty())
						bounceFilter.setPageLimit(Integer.parseInt(txtBouncePages.getText()));
					else
						bounceFilter.setPageLimit(1);
				} catch (NumberFormatException nfe) {
					bounceFilter.setPageLimit(1);
				}
			}
			// updateGraph(filterMetrics.getValue());
			addColumn(txtFilterName.getText());
			TableMenu.addCustomTableMenu(tableResults);
			tableResults.getColumns().get(filters.size()).setVisible(true);

			// Cheap and nasty threading
			// updaterRunnable = new MetricsUpdater(tableMetrics, filter,
			// bounceFilter, filters.size(), tableResults);
			// filters.put(txtFilterName.getText(), filter);
			// new Thread(updaterRunnable).start();
			if (updaterRunnable == null)
				updaterRunnable = new MetricsUpdater();
			updaterRunnable.runUpdater(tableMetrics, filter, bounceFilter, filters.size(), tableResults);
			filters.put(txtFilterName.getText(), filter);
			configureFilters();
			txtFilterName.setText(GenerateName.generate());
		}
	}
	
	@FXML private void exportData() {
		System.out.println("Export");
		
		StringBuilder output = new StringBuilder();
		
		int i = 0;
		for(TableColumn<ObservableMetrics, ?> column : tableResults.getColumns()) {
			if(i > 1) output.append(",");
			if(i > 0) output.append(column.getText());
			i++;
		}
		
		output.append("\n");
		
		for(ObservableMetrics metrics : tableResults.getItems()) {
			output.append(metrics.getDescription());
			for(int j = 0; j < tableResults.getColumns().size() - 2; j++) {
				output.append(",\"" + metrics.getResults(j) + "\"");
			}
			output.append("\n");
		}
		
		fileChooser.setTitle("Save data as...");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
		fileChooser.getExtensionFilters().clear();
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("CSV", "*.csv")
		);
		
		File newFile = fileChooser.showSaveDialog(application.getStage());
		if(newFile != null) {
			try {
				if(!newFile.getName().endsWith(".csv")) newFile = new File(newFile.getPath() + ".csv");
				BufferedWriter out = new BufferedWriter(new FileWriter(newFile));
				out.write(output.toString());
				out.close();
				Desktop.getDesktop().open(newFile);
			} catch(IOException e) {
				System.err.println("Could not save csv.");
			}
		}
	}
	
	private void showHistogram(Series<Date, Number> series) {
		BarChart<String, Number> histogram = new BarChart<>(new CategoryAxis(), new NumberAxis());
		histogram.setTitle(series.getName() + " Histogram");
		histogram.setStyle("-fx-background-color: #ffffff;");
		histogram.setLegendVisible(false);
		histogram.getYAxis().setLabel("Frequency");
		histogram.getXAxis().setLabel(series.getName());
		histogram.setBarGap(1);
		histogram.setCategoryGap(0);

		int   buckets = 25;
		int[] data    = new int[buckets];
		int   minimum = Integer.MAX_VALUE;
		int   maximum = Integer.MIN_VALUE;
		
		for(Data<Date, Number> entry : series.getData()) {
			int value = entry.getYValue().intValue();
			if(value < minimum) minimum = value;
			if(value > maximum) maximum = value;
		}
		
		double segment = (double) (maximum - minimum) / (double) (buckets - 1);
		
		for(Data<Date, Number> entry : series.getData()) {
			int value = entry.getYValue().intValue();
			data[(int) ((value - minimum)/segment)]++;
		}
		
		Series<String, Number> histogram_series = new Series<>();
		
		for(int i = 0; i < buckets; i++) {
			histogram_series.getData().add(new Data<String, Number>(Integer.toString((int) (minimum + i * segment)), data[i]));
		}
		
		histogram.getData().add(histogram_series);
		
		Stage stage = new Stage();
		stage.setTitle(series.getName() + " Histogram");
		stage.setMinHeight(115);
		stage.setScene(new Scene(histogram));
		stage.show();
	}
	
	@FXML private void clearData(ActionEvent event) {
		if(updaterRunnable != null) {
			updaterRunnable.stop();
                        updaterRunnable = null;   // This may need a dispose method?
                }
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
			String key;
			int i = 2;
			 for(Map.Entry<String, Filter> f : filters.entrySet()){  

				if (tableResults.getColumns().get(i).isVisible()) {
				key = f.getKey() + " : " + metric;
				if (graphData.containsKey(key)) {
					Series<Date, Number> data = graphData.get(key);
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
						Series<Date, Number> data = constructor.fetchGraph();
						data.setName(key);

						if(preferences.getBoolean("Graph_Icons", true)) {
							int last = 0;
							for(Data<Date, Number> d : data.getData()) {
								d.setNode(new HoveredThresholdNode(last, d.getYValue().intValue(), lineChart.getData().size()));
								d.getNode().setOnMouseClicked(new EventHandler<Event>() {
									@Override
									public void handle(Event event) {
										showHistogram(data);
									}
								});
								last = d.getYValue().intValue();
							}
						}
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