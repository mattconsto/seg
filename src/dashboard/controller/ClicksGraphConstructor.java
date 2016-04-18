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

public class ClicksGraphConstructor extends GraphConstructor {

	public ClicksGraphConstructor(Filter filter) {
		super(filter);
	}
	
	@Override
	protected Series<Date, Number> generateGraph(Connection conn) throws SQLException, ParseException {
		/*
		ResultSet results = conn.createStatement().executeQuery("SELECT strftime('" + filter.getTimeFormatSQL() +"', DATE) AS DATE,COUNT(*) AS Frequency FROM"
				+ "(SELECT IMPRESSIONS.*, CLICKS.* FROM IMPRESSIONS"
				+ " INNER JOIN CLICKS ON IMPRESSIONS.ID=CLICKS.ID"
				+ " GROUP BY CLICKS.DATE, CLICKS.ID) AS SUBQUERY"
				+ " WHERE " + filter.getSql()
				+ " GROUP BY strftime('" + filter.getTimeFormatSQL() +"', DATE);");
		*/
		ResultSet results = conn.createStatement().executeQuery("SELECT strftime('" + filter.getTimeFormatSQL() +"', CLICKS.DATE),COUNT(*) AS Frequency FROM "
				+ "CLICKS "
				+ "INNER JOIN "
				+ "(SELECT * FROM IMPRESSIONS GROUP BY ID) AS IMPRESSIONS "
				+ "ON CLICKS.ID=IMPRESSIONS.ID "
				+ "WHERE " + filter.getSql().replace("DATE", "CLICKS.DATE")
				+ " GROUP BY strftime('" + filter.getTimeFormatSQL() +"', CLICKS.DATE);");
		
		
		XYChart.Series<Date, Number> series = new XYChart.Series<Date, Number>();
		series.setName("Clicks by date");

		DateFormat format = new SimpleDateFormat(filter.getTimeFormatJava(), Locale.ENGLISH);
		while (results.next())
			series.getData().add(new XYChart.Data<Date, Number>(format.parse(results.getString(1)), results.getInt(2)));

		results.close();
		return series;
	}
}
