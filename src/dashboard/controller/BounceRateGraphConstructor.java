package dashboard.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import dashboard.model.Filter;

public class BounceRateGraphConstructor extends GraphConstructor{

	public BounceRateGraphConstructor(Filter filter) {
		super(filter);
	}

	@Override
	protected Series<String, Number> generateGraph(Connection conn)
			throws SQLException {
		ResultSet results = conn.createStatement().executeQuery("SELECT DATE, NUMCLICKS, NUMBOUNCES FROM "
				+ "(SELECT SUBSTR(CLICKDATE, 0, 14) as CLICKDATE, COUNT(ID) AS NUMCLICKS FROM "
				+ "(SELECT CLICKS.DATE AS CLICKDATE, IMPRESSIONS.* FROM CLICKS INNER JOIN IMPRESSIONS ON CLICKS.ID=IMPRESSIONS.ID GROUP BY CLICKS.ID, CLICKDATE) "
				+ "WHERE " + filter.getSql().replace("DATE", "CLICKDATE")+ " GROUP BY SUBSTR(CLICKDATE, 0, 14)) "
				+ "INNER JOIN "
				+ "(SELECT SUBSTR(ENTRYDATE, 0, 14) AS DATE, COUNT(*) AS NUMBOUNCES FROM "
				+ "(SELECT IMPRESSIONS.*, SERVER.* FROM IMPRESSIONS "
				+ "INNER JOIN SERVER ON IMPRESSIONS.ID=SERVER.ID "
				+ "GROUP BY SERVER.ENTRYDATE, SERVER.ID) AS SUBQUERY "
				+ "WHERE PAGES=1 AND "+ filter.getSql().replace("DATE", "ENTRYDATE")
				+ " GROUP BY SUBSTR(ENTRYDATE, 0, 14)) "
				+ "ON DATE=CLICKDATE GROUP BY DATE");

		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
		series.setName(" by date");

		while (results.next())
			series.getData().add(new XYChart.Data<String, Number>(results.getString(1) + ":00", results.getInt(3)/results.getFloat(2)));

		results.close();
		return series;
	}

}
