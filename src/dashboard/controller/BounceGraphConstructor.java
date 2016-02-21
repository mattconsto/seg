package dashboard.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

public class BounceGraphConstructor extends GraphConstructor{

	@Override
	protected Series<String, Number> generateSpecificGraph(Connection conn) throws SQLException {
		Statement selectStatement = conn.createStatement();
		ResultSet selectResults = selectStatement.executeQuery("SELECT SUBSTR(ENTRYDATE, 0, 11) AS ENTRYDATE,COUNT(*) AS Frequency FROM SERVER WHERE PAGES = 1 GROUP BY SUBSTR(ENTRYDATE, 0, 11);");
		
		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
		series.setName("Bounces (Pages visited = 1) by date");
		
		while(selectResults.next()){
			series.getData().add(new XYChart.Data<String, Number>(selectResults.getString(1), selectResults.getInt(2)));
			System.out.println(selectResults.getString(1));
		}
		
		selectResults.close();
		selectStatement.close();
		return series;
	}

}
