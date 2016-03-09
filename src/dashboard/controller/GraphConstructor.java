package dashboard.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javafx.scene.chart.XYChart;

import dashboard.model.DatabaseConnection;
import dashboard.model.Filter;

/**
 * Abstract class for graph data generation
 */
public abstract class GraphConstructor {
	protected Filter filter;
	protected DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH", Locale.ENGLISH);
	
	public GraphConstructor(Filter filter) {
		this.filter = filter;
	}

	/**
	 * Connect to the database, and fetch the data
	 * @return A Series<String, Number> containing the data
	 * @throws SQLException If there is an issue connecting to the database
	 */
	public XYChart.Series<Date, Number> fetchGraph() throws SQLException {
		try {
			return generateGraph(DatabaseConnection.getConnection());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Run an SQL query and return the data
	 * @param conn A database connection
	 * @return A Series<String, Number> containing the data
	 * @throws SQLException There is an issue with the query
	 * @throws ParseException 
	 */
	protected abstract XYChart.Series<Date, Number> generateGraph(Connection conn) throws SQLException, ParseException;
}
