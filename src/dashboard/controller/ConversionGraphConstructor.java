package dashboard.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

public class ConversionGraphConstructor extends GraphConstructor {

	public ConversionGraphConstructor(String gender, String age, String income, String context, String time) {
		super(gender, age, income, context, time);
	}
	
	@Override
	protected Series<String, Number> generateGraph(Connection conn) throws SQLException {
		ResultSet results = conn.createStatement().executeQuery("SELECT SUBSTR(ENTRYDATE, 0, 14) AS ENTRYDATE,COUNT(*) AS Frequency "
				+ "FROM (SELECT IMPRESSIONS.CONTEXT, SERVER.* FROM "
				+ "IMPRESSIONS INNER JOIN SERVER ON IMPRESSIONS.ID=SERVER.ID "
				+ "GROUP BY SERVER.ENTRYDATE, SERVER.ID) AS SUBQUERY "
				+ "WHERE CONVERSION = 1 AND " + filterContext
				+ " GROUP BY SUBSTR(ENTRYDATE, 0, 14);");

		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
		series.setName("Conversions by date");

		while (results.next())
			series.getData().add(new XYChart.Data<String, Number>(results.getString(1) + ":00", results.getInt(2)));

		results.close();
		return series;
	}
}
