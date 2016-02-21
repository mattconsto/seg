package dashboard.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

public class UniqueClicksGraphConstructor extends GraphConstructor{

	@Override
	protected Series<String, Number> generateSpecificGraph(Connection conn) throws SQLException {
		Statement selectStatement = conn.createStatement();
		ResultSet selectResults = selectStatement.executeQuery("SELECT SUBSTR(DATE, 0, 11) AS DATE,COUNT(DISTINCT ID) AS Frequency FROM CLICKS GROUP BY SUBSTR(DATE, 0, 11);");
		
		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
		series.setName("Unique clicks by date");
		
		while(selectResults.next()){
			series.getData().add(new XYChart.Data<String, Number>(selectResults.getString(1), selectResults.getInt(2)));
			System.out.println(selectResults.getString(1));
		}
		
		selectResults.close();
		selectStatement.close();
		return series;
	}

}
