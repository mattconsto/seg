package dashboard.controller;

import java.sql.Connection;
import java.sql.SQLException;

import javafx.scene.chart.XYChart;

import dashboard.model.DatabaseConnection;
import dashboard.model.Filter;

/**
 * Abstract class for graph data generation
 */
public abstract class GraphConstructor {
	
	protected Filter filter;


	public GraphConstructor(Filter filter) {
            this.filter = filter;
	}

	/**
	 * Connect to the database, and fetch the data
	 * @return A Series<String, Number> containing the data
	 * @throws SQLException If there is an issue connecting to the database
	 */
	public XYChart.Series<String, Number> fetchGraph() throws SQLException {
		return generateGraph(DatabaseConnection.getConnection());
	}

	/**
	 * Run an SQL query and return the data
	 * @param conn A database connection
	 * @return A Series<String, Number> containing the data
	 * @throws SQLException There is an issue with the query
	 */
	protected abstract XYChart.Series<String, Number> generateGraph(Connection conn) throws SQLException;
}
