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

public class TotalCostGraphConstructor extends GraphConstructor{
	public TotalCostGraphConstructor(Filter filter){
		super(filter);
	}

	@Override
	protected Series<Date, Number> generateGraph(Connection conn)
			throws SQLException, ParseException {
		ResultSet results = conn.createStatement().executeQuery("SELECT IMPDATE, CLICKCOST, IMPCOST FROM "
				+ "(SELECT strftime('" + filter.getTimeFormatSQL() + "', CLICKS.DATE) AS CLICKDATE, SUM(CLICKS.COST) AS CLICKCOST FROM "
				+ "CLICKS "
				+ "INNER JOIN "
				+ "(SELECT * FROM IMPRESSIONS GROUP BY ID) AS IMPRESSIONS "
				+ "ON CLICKS.ID=IMPRESSIONS.ID "
				+ "WHERE " + filter.getSql().replace("DATE", "CLICKS.DATE")
				+ " GROUP BY strftime('" + filter.getTimeFormatSQL() +"', CLICKS.DATE)) "
				+ "INNER JOIN "
				+ "(SELECT strftime('" + filter.getTimeFormatSQL() + "', DATE) AS IMPDATE, SUM(COST) AS IMPCOST FROM "
				+ "IMPRESSIONS "
				+ "WHERE " + filter.getSql() +" "
				+ "GROUP BY strftime('" + filter.getTimeFormatSQL() +"', DATE)) "
				+ "ON IMPDATE=CLICKDATE");
		
		XYChart.Series<Date, Number> series = new XYChart.Series<Date, Number>();
		series.setName(" by date");

		DateFormat format = new SimpleDateFormat(filter.getTimeFormatJava(), Locale.ENGLISH);
		
		int i = -1;
		while (results.next()){
			if(i >= 0)
				series.getData().add(new XYChart.Data<Date, Number>(format.parse(results.getString(1)), (int)series.getData().get(i).getYValue() + results.getInt(2)+results.getInt(3)));
			else
				series.getData().add(new XYChart.Data<Date, Number>(format.parse(results.getString(1)), results.getInt(2)+results.getInt(3)));
			i++;
		}
			
		results.close();
		return series;
	}
}
