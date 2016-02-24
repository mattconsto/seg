package dashboard.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;


public class BounceGraphConstructor extends GraphConstructor{
	
	public BounceGraphConstructor(String gender, String age, String income, String context, String time) {
		super(gender, age, income, context, time);
	}
	
	@Override
	protected Series<String, Number> generateGraph(Connection conn) throws SQLException {
		ResultSet results = conn.createStatement().executeQuery("SELECT SUBSTR(ENTRYDATE, 0, 14) AS ENTRYDATE,COUNT(*) AS Frequency FROM SERVER WHERE PAGES = 1 GROUP BY SUBSTR(ENTRYDATE, 0, 14);");

		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
		series.setName("Bounces (Pages visited = 1) by date");

		while (results.next())
			series.getData().add(new XYChart.Data<String, Number>(results.getString(1) + ":00", results.getInt(2)));

		results.close();
		return series;
	}
}
