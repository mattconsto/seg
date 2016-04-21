package dashboard.view;

import dashboard.controller.BounceGraphConstructor;
import dashboard.controller.BounceRateGraphConstructor;
import dashboard.controller.CPAGraphConstructor;
import dashboard.controller.CPCGraphConstructor;
import dashboard.controller.CPMGraphConstructor;
import dashboard.controller.CTRGraphConstructor;
import dashboard.controller.ClicksGraphConstructor;
import dashboard.controller.ConversionGraphConstructor;
import dashboard.controller.GraphConstructor;
import dashboard.controller.ImpressionsGraphConstructor;
import dashboard.controller.TotalCostGraphConstructor;
import dashboard.controller.UniqueClicksGraphConstructor;
import dashboard.controller.UniqueImpressionsGraphConstructor;

import java.sql.SQLException;

import dashboard.model.BounceFilter;
import dashboard.model.Filter;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import javafx.stage.Stage;

public class GraphUpdater {

	private ExecutorService executor;

	public GraphUpdater() {
		try {
			executor = Executors.newCachedThreadPool(); // .newFixedThreadPool(15);
		} catch (Exception eofException) { // EOFException |InterruptedException
			System.err.println("Failed to start threading " + eofException.getMessage());
		} //
	}

	public void stop() {
		executor.shutdownNow();
	}

	public void runUpdater(Filter f, String m, BounceFilter b, LineChart<Date, Number> lc,
			HashMap<String, XYChart.Series<Date, Number>> gd, Runnable callback) {

		GraphThread s = new GraphThread(f, m, b, lc, gd, callback);
		executor.execute(s);

	}

	public class GraphThread implements Runnable {
		private Filter filter;
		private String metric;
		private BounceFilter bounceFilter;
		private LineChart<Date, Number> lineChart;
		private HashMap<String, XYChart.Series<Date, Number>> graphData;
		private Runnable callback;

		public GraphThread(Filter f, String m, BounceFilter b, LineChart<Date, Number> lc,
				HashMap<String, XYChart.Series<Date, Number>> gd, Runnable callback) {
			this.filter = f;
			this.metric = m;
			this.bounceFilter = b;
			this.lineChart = lc;
			this.graphData = gd;
			this.callback = callback;
		}

		private void showHistogram(XYChart.Series<Date, Number> series, Runnable callback) {
			BarChart<String, Number> histogram = new BarChart<>(new CategoryAxis(), new NumberAxis());
			histogram.setTitle(series.getName() + " Histogram");
			histogram.setStyle("-fx-background-color: #ffffff;");
			histogram.setLegendVisible(false);
			histogram.getYAxis().setLabel("Frequency");
			histogram.getXAxis().setLabel(series.getName());
			histogram.setBarGap(1);
			histogram.setCategoryGap(0);

			int buckets = 25;
			int[] data = new int[buckets];
			int minimum = Integer.MAX_VALUE;
			int maximum = Integer.MIN_VALUE;

			for (XYChart.Data<Date, Number> entry : series.getData()) {
				int value = entry.getYValue().intValue();
				if (value < minimum)
					minimum = value;
				if (value > maximum)
					maximum = value;
			}

			double segment = (double) (maximum - minimum) / (double) (buckets - 1);

			for (XYChart.Data<Date, Number> entry : series.getData()) {
				int value = entry.getYValue().intValue();
				data[(int) ((value - minimum) / segment)]++;
			}

			XYChart.Series<String, Number> histogram_series = new XYChart.Series<>();

			for (int i = 0; i < buckets; i++) {
				histogram_series.getData().add(
						new XYChart.Data<String, Number>(Integer.toString((int) (minimum + i * segment)), data[i]));
			}

			histogram.getData().add(histogram_series);

			Stage stage = new Stage();
			stage.setTitle(series.getName() + " Histogram");
			stage.setMinHeight(115);
			stage.setScene(new Scene(histogram));
			stage.show();
			
			if(callback != null) callback.run();
		}

		@Override
		public void run() {
			GraphConstructor constructor;
			switch (metric) {
			default:
			case "Bounces":
				constructor = new BounceGraphConstructor(filter, bounceFilter);
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
			case "Cost Per Click":
			case "CPC":
				constructor = new CPCGraphConstructor(filter);
				break;
			case "Cost Per Aquisition":
			case "CPA":
				constructor = new CPAGraphConstructor(filter);
				break;
			case "Cost Per Mille":
			case "CPM":
				constructor = new CPMGraphConstructor(filter);
				break;
			case "Click Through Rate":
			case "CTR":
				constructor = new CTRGraphConstructor(filter);
				break;
			case "Total Cost":
				constructor = new TotalCostGraphConstructor(filter);
				break;
			case "Bounce Rate":
				constructor = new BounceRateGraphConstructor(filter, bounceFilter);
				break;
			}

			try {
				String key = filter.getDescription() + " : " + metric;
				XYChart.Series<Date, Number> data = constructor.fetchGraph();
				data.setName(key);

				int last = 0;
				for (XYChart.Data<Date, Number> d : data.getData()) {
					d.setNode(new HoveredThresholdNode(last, d.getYValue().intValue(), graphData.size())); // linechart.getData().size()));
					d.getNode().setOnMouseClicked(new EventHandler<Event>() {
						@Override
						public void handle(Event event) {
							showHistogram(data, null);
						}
					});
					last = d.getYValue().intValue();
				}

				graphData.put(key, data);
				lineChart.setAnimated(false);
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						lineChart.getData().add(data);

					}
				});

				lineChart.getXAxis().setTickLabelsVisible(true);

			} catch (SQLException e) {
				System.err.println("Unable to fetch data from database: " + e.getMessage());
			}

			if(callback != null) callback.run();
		}

	}
}
