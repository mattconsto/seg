package dashboard.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import dashboard.controller.GraphConstructor;
import dashboard.model.Filter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CTRGraphConstructor extends GraphConstructor {

	public CTRGraphConstructor(Filter filter) {
		super(filter);
	}

	@Override
	protected Series<Date, Number> generateGraph(Connection conn)
			throws SQLException, ParseException {
		ResultSet results = conn.createStatement().executeQuery("SELECT CLICKDATE, NUMCLICKS, NUMIMP FROM "
				+ "(SELECT strftime('" + filter.timeFormatSQL +"', CLICKDATE) as CLICKDATE, COUNT(*) AS NUMCLICKS FROM"
				+ "(SELECT CLICKS.DATE AS CLICKDATE, IMPRESSIONS.* FROM CLICKS INNER JOIN IMPRESSIONS ON CLICKS.ID=IMPRESSIONS.ID GROUP BY CLICKS.ID, CLICKDATE) "
				+ "WHERE " + filter.getSql().replace("DATE", "CLICKDATE")+ " GROUP BY strftime('" + filter.timeFormatSQL +"', CLICKDATE)) "
				+ "INNER JOIN "
				+ "(SELECT strftime('" + filter.timeFormatSQL +"', DATE) AS DATE, COUNT(*) AS NUMIMP FROM IMPRESSIONS "
				+ "WHERE " + filter.getSql()+ " GROUP BY strftime('" + filter.timeFormatSQL +"', DATE)) "
				+ "ON DATE=CLICKDATE ");

		XYChart.Series<Date, Number> series = new XYChart.Series<Date, Number>();
		series.setName("Click Through Rate by date");

		DateFormat format = new SimpleDateFormat(filter.timeFormatJava, Locale.ENGLISH);
		
		while (results.next())
			series.getData().add(new XYChart.Data<Date, Number>(format.parse(results.getString(1)), results.getInt(2)/results.getFloat(3)));

		results.close();
		return series;
	}

}
