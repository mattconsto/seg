package dashboard.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import dashboard.controller.GraphConstructor;
import dashboard.model.Filter;

public class CTRGraphConstructor extends GraphConstructor {

	public CTRGraphConstructor(Filter filter) {
		super(filter);
	}

	@Override
	protected Series<String, Number> generateGraph(Connection conn)
			throws SQLException {
		ResultSet results = conn.createStatement().executeQuery("SELECT DATE, NUMCLICKS, NUMIMP FROM "
				+ "(SELECT SUBSTR(DATE, 0, 14) as CLICKDATE, COUNT(*) AS NUMCLICKS FROM CLICKS "
				+ "WHERE " + filter.getSql().replace("DATE", "CLICKDATE")+ " GROUP BY SUBSTR(DATE, 0, 14)) "
				+ "INNER JOIN "
				+ "(SELECT SUBSTR(DATE, 0, 14) AS DATE, COUNT(*) AS NUMIMP FROM IMPRESSIONS "
				+ "WHERE " + filter.getSql()+ " GROUP BY SUBSTR(DATE, 0, 14)) "
				+ "ON DATE=CLICKDATE ");

		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
		series.setName(" by date");

		while (results.next())
			series.getData().add(new XYChart.Data<String, Number>(results.getString(1) + ":00", results.getInt(2)/results.getFloat(3)));

		results.close();
		return series;
	}

}
