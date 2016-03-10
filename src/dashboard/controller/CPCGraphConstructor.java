package dashboard.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import dashboard.model.Filter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CPCGraphConstructor extends GraphConstructor{

	public CPCGraphConstructor(Filter filter) {
		super(filter);
	}

	@Override
	protected Series<Date, Number> generateGraph(Connection conn)
			throws SQLException, ParseException {
		ResultSet results = conn.createStatement().executeQuery("SELECT CLICKDATE, CLICKCOST, IMPCOST, NUMCLICKS FROM "
				+ "(strftime('" + filter.timeFormatSQL +"', CLICKDATE) AS CLICKDATE, SUM(CLICKCOST) AS CLICKCOST, COUNT(ID) AS NUMCLICKS FROM "
				+ "(SELECT IMPRESSIONS.*, CLICKS.ID, CLICKS.DATE AS CLICKDATE, CLICKS.COST AS CLICKCOST "
				+ "FROM IMPRESSIONS "
				+ "INNER JOIN CLICKS "
				+ "ON IMPRESSIONS.ID=CLICKS.ID "
				+ "GROUP BY CLICKS.DATE, CLICKS.ID) "
				+ "WHERE " + filter.getSql().replace("DATE", "CLICKDATE")+ " GROUP BY strftime('" + filter.timeFormatSQL +"', CLICKDATE)) "
				+ "INNER JOIN "
				+ "(SELECT strftime('" + filter.timeFormatSQL +"', DATE) AS IMPDATE, SUM(COST) AS IMPCOST FROM IMPRESSIONS "
				+ "WHERE " + filter.getSql()+ " GROUP BY strftime('" + filter.timeFormatSQL +"', DATE)) "
				+ "ON IMPDATE=CLICKDATE");

		XYChart.Series<Date, Number> series = new XYChart.Series<Date, Number>();
		series.setName("Cost Per Click by date");
		
		DateFormat format = new SimpleDateFormat(filter.timeFormatJava, Locale.ENGLISH);
		
		while (results.next()){
			float calculation = ((results.getInt(2) + results.getInt(3)) / (float)results.getInt(4));
			series.getData().add(new XYChart.Data<Date, Number>(format.parse(results.getString(1)), calculation));
		}

		results.close();
		return series;
	}

}
