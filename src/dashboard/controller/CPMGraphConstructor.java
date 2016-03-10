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

public class CPMGraphConstructor extends GraphConstructor{

	public CPMGraphConstructor(Filter filter) {
		super(filter);
	}

	@Override
	protected Series<Date, Number> generateGraph(Connection conn)
			throws SQLException, ParseException {
		ResultSet results = conn.createStatement().executeQuery("SELECT IMPDATE, CLICKCOST, IMPCOST, NUMIMPS FROM "
				+ "(SELECT strftime('" + filter.timeFormatSQL +"', CLICKDATE) AS CLICKDATE, SUM(CLICKCOST) AS CLICKCOST FROM "
				+ "(SELECT IMPRESSIONS.*, CLICKS.ID, CLICKS.DATE AS CLICKDATE, CLICKS.COST AS CLICKCOST "
				+ "FROM IMPRESSIONS "
				+ "INNER JOIN CLICKS "
				+ "ON IMPRESSIONS.ID=CLICKS.ID "
				+ "GROUP BY CLICKS.DATE, CLICKS.ID) "
				+ "WHERE " + filter.getSql().replace("DATE", "CLICKDATE")+ " GROUP BY strftime('" + filter.timeFormatSQL +"', CLICKDATE)) "
				+ "INNER JOIN "
				+ "(SELECT strftime('" + filter.timeFormatSQL +"', DATE) AS IMPDATE, SUM(COST) AS IMPCOST, COUNT(ID) AS NUMIMPS FROM IMPRESSIONS "
				+ "WHERE " + filter.getSql()+ " GROUP BY strftime('" + filter.timeFormatSQL +"', DATE)) "
				+ "ON IMPDATE=CLICKDATE");

		XYChart.Series<Date, Number> series = new XYChart.Series<Date, Number>();
		series.setName("Cost per Mil by date");

		DateFormat format = new SimpleDateFormat(filter.timeFormatJava, Locale.ENGLISH);
		
		while (results.next())
			series.getData().add(new XYChart.Data<Date, Number>(format.parse(results.getString(1)), ((results.getFloat(2)+results.getFloat(3))/results.getFloat(4))*1000));

		results.close();
		return series;
	}

}
