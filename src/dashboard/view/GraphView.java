package dashboard.view;

import java.sql.SQLException;

import dashboard.controller.BounceGraphConstructor;
import dashboard.controller.ClicksGraphConstructor;
import dashboard.controller.ConversionGraphConstructor;
import dashboard.controller.GraphConstructor;
import dashboard.controller.ImpressionsGraphConstructor;
import dashboard.controller.UniqueImpressionsGraphConstructor;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Display a graph in an otherwise empty JavaFX window
 */
public class GraphView extends Application{

	private final CategoryAxis xAxis = new CategoryAxis();
	private final NumberAxis yAxis = new NumberAxis();
	
	@Override
	public void start(Stage arg0) throws Exception {
		//Grid layout will hold the elements
		GridPane root = new GridPane();
		root.setAlignment(Pos.CENTER);
		root.setHgap(10);
		root.setVgap(10);
		root.setPadding(new Insets(25,25,25,25));
		
		//Set up a line graph
		xAxis.setLabel("Date");
		final LineChart<String,Number> lineChart = new LineChart<String,Number>(xAxis, yAxis);
		lineChart.setCreateSymbols(false);
		lineChart.setLegendVisible(false);
		root.add(lineChart, 0, 0, 1, 1);
		
		updateGraph(new ImpressionsGraphConstructor(), "Impressions", lineChart);
		
		Scene scene = new Scene(root, 800, 600);
		arg0.setScene(scene);
		arg0.show();
	}
	
	public void updateGraph(GraphConstructor graphConstructor, String yLabel, LineChart<String, Number> lineChart){
		yAxis.setLabel(yLabel);
		
		try {
			lineChart.getData().add(graphConstructor.fetchGraph());
		} catch (SQLException e) {
			System.err.println("Unable to fetch data from database: " + e.getMessage());
		}
	}
	
	public static void main(String[] args){
		launch(args);
	}
}
