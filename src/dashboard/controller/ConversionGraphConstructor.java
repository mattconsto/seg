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

public class ConversionGraphConstructor extends GraphConstructor {

	public ConversionGraphConstructor(Filter filter) {
		super(filter);
	}
	
	@Override
	protected Series<Date, Number> generateGraph(Connection conn) throws SQLException, ParseException {
		ResultSet results = conn.createStatement().executeQuery("SELECT SUBSTR(ENTRYDATE, 0, 14) AS ENTRYDATE,COUNT(*) AS Frequency "
				+ "FROM (SELECT IMPRESSIONS.*, SERVER.* FROM "
				+ "IMPRESSIONS INNER JOIN SERVER ON IMPRESSIONS.ID=SERVER.ID "
				+ "GROUP BY SERVER.ENTRYDATE, SERVER.ID) AS SUBQUERY "
				+ "WHERE CONVERSION = 1 AND " + filter.getSql().replace("DATE", "ENTRYDATE")
				+ " GROUP BY SUBSTR(ENTRYDATE, 0, 14);");

		XYChart.Series<Date, Number> series = new XYChart.Series<Date, Number>();
		series.setName("Conversions by date");

		DateFormat format = new SimpleDateFormat(filter.timeFormatJava, Locale.ENGLISH);
		while (results.next())
			series.getData().add(new XYChart.Data<Date, Number>(format.parse(results.getString(1)), results.getInt(2)));

		results.close();
		return series;
	}
}
