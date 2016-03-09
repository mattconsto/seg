package dashboard.controller;

import dashboard.model.Filter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

public class BounceGraphConstructor extends GraphConstructor{
	
	public BounceGraphConstructor(Filter filter) {
		super(filter);
	}
	
	@Override
	protected Series<Date, Number> generateGraph(Connection conn) throws SQLException, ParseException {
		ResultSet results = conn.createStatement().executeQuery("SELECT SUBSTR(ENTRYDATE, 0, 14) AS ENTRYDATE,COUNT(*) AS Frequency FROM "
				+ "(SELECT IMPRESSIONS.*, SERVER.* FROM IMPRESSIONS "
				+ "INNER JOIN SERVER ON IMPRESSIONS.ID=SERVER.ID "
				+ "GROUP BY SERVER.ENTRYDATE, SERVER.ID) AS SUBQUERY "
				+ "WHERE PAGES = 1 AND " + filter.getSql().replace("DATE", "ENTRYDATE")+ " GROUP BY SUBSTR(ENTRYDATE, 0, 14);");

		XYChart.Series<Date, Number> series = new XYChart.Series<Date, Number>();
		series.setName("Bounces (Pages visited = 1) by date");

		while (results.next())
			series.getData().add(new XYChart.Data<Date, Number>(format.parse(results.getString(1)), results.getInt(2)));
		
		results.close();
		return series;
	}
}
