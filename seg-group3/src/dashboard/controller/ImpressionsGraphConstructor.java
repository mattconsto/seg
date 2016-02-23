package dashboard.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

public class ImpressionsGraphConstructor extends GraphConstructor {

	@Override
	protected Series<String, Number> generateSpecificGraph(Connection conn) throws SQLException{
		Statement selectStatement = conn.createStatement();
		ResultSet selectResults = selectStatement.executeQuery("SELECT SUBSTR(DATE, 0, 11) AS DATE,COUNT(*) AS Frequency FROM IMPRESSIONS GROUP BY SUBSTR(DATE, 0, 11);");
		
		XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
		series.setName("Impressions by date");
		
		while(selectResults.next()){
			series.getData().add(new XYChart.Data<String, Number>(selectResults.getString(1), selectResults.getInt(2)));
			System.out.println(selectResults.getString(1));
		}
		
		selectResults.close();
		selectStatement.close();
		return series;
	}

}
