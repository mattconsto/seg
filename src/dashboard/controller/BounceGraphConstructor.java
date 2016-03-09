package dashboard.controller;

import dashboard.model.Filter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

public class BounceGraphConstructor extends GraphConstructor{
	
	public BounceGraphConstructor(Filter filter) {
		super(filter);
	}
	
	@Override
	protected Series<Date, Number> generateGraph(Connection conn) throws SQLException, ParseException {
		ResultSet results = conn.createStatement().executeQuery("SELECT strftime('" + filter.timeFormatSQL +"', ENTRYDATE) AS ENTRYDATE,COUNT(*) AS Frequency FROM "
				+ "(SELECT IMPRESSIONS.*, SERVER.* FROM IMPRESSIONS "
				+ "INNER JOIN SERVER ON IMPRESSIONS.ID=SERVER.ID "
				+ "GROUP BY SERVER.ENTRYDATE, SERVER.ID) AS SUBQUERY "
				+ "WHERE PAGES = 1 AND " + filter.getSql().replace("DATE", "ENTRYDATE")+ " GROUP BY strftime('" + filter.timeFormatSQL +"', ENTRYDATE);");

		XYChart.Series<Date, Number> series = new XYChart.Series<Date, Number>();
		series.setName("Bounces (Pages visited = 1) by date");

		DateFormat format = new SimpleDateFormat(filter.timeFormatJava, Locale.ENGLISH);
		
		while (results.next()) {
			series.getData().add(new XYChart.Data<Date, Number>(format.parse(results.getString(1)), results.getInt(2)));
		}
		
		results.close();
		return series;
	}
}
