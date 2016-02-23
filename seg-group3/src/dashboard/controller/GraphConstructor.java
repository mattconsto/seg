package dashboard.controller;

import java.sql.Connection;
import java.sql.SQLException;

import javafx.scene.chart.XYChart;

public abstract class GraphConstructor
{
	public XYChart.Series<String, Number> fetchGraph() throws SQLException
	{
		Connection conn = dashboard.model.DatabaseConnection.getConnection();
		System.out.println("Graph constructor successfully connected to database");
		
		XYChart.Series<String, Number> series = generateSpecificGraph(conn);
		
		dashboard.model.DatabaseConnection.closeConnection();
		
		return series;
	}
	
	protected abstract XYChart.Series<String, Number> generateSpecificGraph(Connection conn) throws SQLException;
}
