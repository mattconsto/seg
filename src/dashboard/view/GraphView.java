package dashboard.view;

import dashboard.controller.ClicksGraphConstructor;
import dashboard.controller.ImpressionsGraphConstructor;
import dashboard.controller.UniqueImpressionsGraphConstructor;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Display a graph in an otherwise empty JavaFX window
 */
public class GraphView extends Application{

	@Override
	public void start(Stage arg0) throws Exception {
		//Grid layout will hold the elements
		GridPane root = new GridPane();
		root.setAlignment(Pos.CENTER);
		root.setHgap(10);
		root.setVgap(10);
		root.setPadding(new Insets(25,25,25,25));
		
		//Set up a line graph
		final CategoryAxis xAxis = new CategoryAxis();
		final NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("Date");
		yAxis.setLabel("Impressions");
		final LineChart<String,Number> lineChart = new LineChart<String,Number>(xAxis, yAxis);
		lineChart.setCreateSymbols(false);
		lineChart.setLegendVisible(false);
		root.add(lineChart, 0, 0, 1, 1);
		
		lineChart.getData().add(new UniqueImpressionsGraphConstructor().fetchGraph());
		
		Scene scene = new Scene(root, 800, 600);
		arg0.setScene(scene);
		arg0.show();
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
