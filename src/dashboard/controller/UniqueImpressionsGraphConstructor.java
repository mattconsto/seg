package dashboard.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

public class UniqueImpressionsGraphConstructor extends GraphConstructor {

	public UniqueImpressionsGraphConstructor(String gender, String age, String income, String context, String time) {
		super(gender, age, income, context, time);
	}
	
	@Override
	protected Series<String, Number> generateGraph(Connection conn) throws SQLException {
		ResultSet results = conn.createStatement().executeQuery("SELECT SUBSTR(DATE, 0, 14) AS DATE,COUNT(DISTINCT ID) AS Frequency FROM IMPRESSIONS GROUP BY SUBSTR(DATE, 0, 14);");

		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
		series.setName("Unique impressions by date");

		while (results.next())
			series.getData().add(new XYChart.Data<String, Number>(results.getString(1) + ":00", results.getInt(2)));

		results.close();
		return series;
	}
}
