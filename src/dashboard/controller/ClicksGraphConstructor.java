package dashboard.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

public class ClicksGraphConstructor extends GraphConstructor {

	public ClicksGraphConstructor(String gender, String age, String income, String context, String time) {
		super(gender, age, income, context, time);
	}
	
	@Override
	protected Series<String, Number> generateGraph(Connection conn) throws SQLException {
		ResultSet results = conn.createStatement().executeQuery("SELECT SUBSTR(DATE, 0, 14) AS DATE,COUNT(*) AS Frequency FROM"
				+ "(SELECT IMPRESSIONS.CONTEXT, CLICKS.* FROM IMPRESSIONS"
				+ " INNER JOIN CLICKS ON IMPRESSIONS.ID=CLICKS.ID"
				+ " GROUP BY CLICKS.DATE, CLICKS.ID) AS SUBQUERY"
				+ " WHERE " + filterContext
				+ " GROUP BY SUBSTR(DATE, 0, 14);");

		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
		series.setName("Clicks by date");

		while (results.next())
			series.getData().add(new XYChart.Data<String, Number>(results.getString(1) + ":00", results.getInt(2)));

		results.close();
		return series;
	}
}
