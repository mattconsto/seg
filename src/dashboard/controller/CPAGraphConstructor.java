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

public class CPAGraphConstructor extends GraphConstructor{
	public CPAGraphConstructor(Filter filter) {
		super(filter);
	}

	@Override
	protected Series<Date, Number> generateGraph(Connection conn)
			throws SQLException, ParseException {
		ResultSet results = conn.createStatement().executeQuery("SELECT ENTRYDATE, CLICKCOST, IMPCOST, Frequency FROM "
				+ "(SELECT strftime('" + filter.getTimeFormatSQL() +"', ENTRYDATE) AS ENTRYDATE,COUNT(*) AS Frequency "
				+ "FROM (SELECT IMPRESSIONS.*, SERVER.* FROM "
				+ "IMPRESSIONS INNER JOIN SERVER ON IMPRESSIONS.ID=SERVER.ID "
				+ "GROUP BY SERVER.ENTRYDATE, SERVER.ID) AS SUBQUERY "
				+ "WHERE CONVERSION = 1 AND " + filter.getSql().replace("DATE", "ENTRYDATE") + " "
				+ "GROUP BY strftime('" + filter.getTimeFormatSQL() +"', ENTRYDATE)) "
				+ "INNER JOIN "
				+ "(SELECT CLICKDATE, CLICKCOST, IMPCOST FROM "
				+ "(SELECT strftime('" + filter.getTimeFormatSQL() +"', CLICKDATE) AS CLICKDATE, SUM(CLICKCOST) AS CLICKCOST FROM "
				+ "(SELECT IMPRESSIONS.*, CLICKS.ID, CLICKS.DATE AS CLICKDATE, CLICKS.COST AS CLICKCOST "
				+ "FROM IMPRESSIONS "
				+ "INNER JOIN CLICKS "
				+ "ON IMPRESSIONS.ID=CLICKS.ID "
				+ "GROUP BY CLICKS.DATE, CLICKS.ID) "
				+ "WHERE " + filter.getSql().replace("DATE", "CLICKDATE")+ " GROUP BY strftime('" + filter.getTimeFormatSQL() +"', CLICKDATE)) "
				+ "INNER JOIN "
				+ "(SELECT strftime('" + filter.getTimeFormatSQL() +"', DATE) AS IMPDATE, SUM(COST) AS IMPCOST FROM IMPRESSIONS "
				+ "WHERE " + filter.getSql()+ " GROUP BY strftime('" + filter.getTimeFormatSQL() +"', DATE)) "
				+ "ON IMPDATE=CLICKDATE) "
				+ "ON CLICKDATE=ENTRYDATE;");

		XYChart.Series<Date, Number> series = new XYChart.Series<Date, Number>();
		series.setName("Cost Per Acquisition(CPA) by date");
		
		
		DateFormat format = new SimpleDateFormat(filter.getTimeFormatJava(), Locale.ENGLISH);
		while (results.next()){
			float calculation = ((results.getInt(2) + results.getInt(3)) / (float)results.getInt(4));
			series.getData().add(new XYChart.Data<Date, Number>(format.parse(results.getString(1)), calculation));
		}

		results.close();
		return series;
	}
}
