package dashboard.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import dashboard.model.Filter;

public class CPCGraphConstructor extends GraphConstructor{

	public CPCGraphConstructor(Filter filter) {
		super(filter);
	}

	@Override
	protected Series<String, Number> generateGraph(Connection conn)
			throws SQLException {
		ResultSet results = conn.createStatement().executeQuery("SELECT CLICKDATE, CLICKCOST, IMPCOST, NUMCLICKS FROM "
				+ "(SELECT SUBSTR(CLICKDATE, 0, 14) AS CLICKDATE, SUM(CLICKCOST) AS CLICKCOST, COUNT(ID) AS NUMCLICKS FROM "
				+ "(SELECT IMPRESSIONS.*, CLICKS.ID, CLICKS.DATE AS CLICKDATE, CLICKS.COST AS CLICKCOST "
				+ "FROM IMPRESSIONS "
				+ "INNER JOIN CLICKS "
				+ "ON IMPRESSIONS.ID=CLICKS.ID "
				+ "GROUP BY CLICKS.DATE, CLICKS.ID) "
				+ "WHERE " + filter.getSql().replace("DATE", "CLICKDATE")+ " GROUP BY SUBSTR(CLICKDATE, 0, 14)) "
				+ "INNER JOIN "
				+ "(SELECT SUBSTR(DATE,0,14) AS IMPDATE, SUM(COST) AS IMPCOST FROM IMPRESSIONS "
				+ "WHERE " + filter.getSql()+ " GROUP BY SUBSTR(DATE, 0, 14)) "
				+ "ON IMPDATE=CLICKDATE");
				//+ "WHERE " + filter.getSql().replace("DATE", "CLICKDATE")+ " GROUP BY SUBSTR(CLICKDATE, 0, 14);");

		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
		series.setName("Cost Per Click(CPC) by date");
		
		
		while (results.next()){
			float calculation = ((results.getInt(2) + results.getInt(3)) / (float)results.getInt(4));
			series.getData().add(new XYChart.Data<String, Number>(results.getString(1) + ":00", calculation));
		}

		results.close();
		return series;
	}

}
