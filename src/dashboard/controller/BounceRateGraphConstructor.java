package dashboard.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import dashboard.model.BounceFilter;
import dashboard.model.Filter;

 import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class BounceRateGraphConstructor extends GraphConstructor{
	private final BounceFilter bounceFilter;
	
	public BounceRateGraphConstructor(Filter filter, BounceFilter myBounceFilter) {
		super(filter);
		bounceFilter = myBounceFilter;
	}

	@Override
	protected Series<Date, Number> generateGraph(Connection conn)
			throws SQLException, ParseException {
		ResultSet results = conn.createStatement().executeQuery("SELECT ENTRYDATE, NUMCLICKS, NUMBOUNCES FROM "
				+ "(SELECT strftime('" + filter.getTimeFormatSQL() +"', ENTRYDATE) AS ENTRYDATE,COUNT(*) AS NUMBOUNCES FROM "
				+ "SERVER "
				+ "INNER JOIN "
				+ "(SELECT * FROM IMPRESSIONS GROUP BY ID) AS IMPRESSIONS "
				+ "ON SERVER.ID=IMPRESSIONS.ID "
				+ "WHERE " + bounceFilter.getSQL() + " AND " + filter.getSql().replace("DATE", "SERVER.ENTRYDATE")+ " "
				+ "GROUP BY strftime('" + filter.getTimeFormatSQL() +"', SERVER.ENTRYDATE))"
				+ "INNER JOIN"
				+ "(SELECT strftime('" + filter.getTimeFormatSQL() +"', CLICKS.DATE) AS CLICKDATE,COUNT(*) AS NUMCLICKS FROM "
				+ "CLICKS "
				+ "INNER JOIN "
				+ "(SELECT * FROM IMPRESSIONS GROUP BY ID) AS IMPRESSIONS "
				+ "ON CLICKS.ID=IMPRESSIONS.ID "
				+ "WHERE " + filter.getSql().replace("DATE", "CLICKS.DATE")
				+ " GROUP BY strftime('" + filter.getTimeFormatSQL() +"', CLICKS.DATE))"
				+ "ON ENTRYDATE=CLICKDATE");
	
		XYChart.Series<Date, Number> series = new XYChart.Series<Date, Number>();
		series.setName(" by date");

		DateFormat format = new SimpleDateFormat(filter.getTimeFormatJava(), Locale.ENGLISH);
		
		while (results.next())
			series.getData().add(new XYChart.Data<Date, Number>(format.parse(results.getString(1)), results.getInt(3)/results.getFloat(2)));

		results.close();
		return series;
	}
}
