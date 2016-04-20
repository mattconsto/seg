package dashboard.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.sql.DriverManager;
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
	public XYChart.Series<Date, Number> fetchGraph() throws SQLException {
		try {
			System.out.println("Constructing Graph");
                       
                        Class.forName("org.sqlite.JDBC");
			Connection connection = DriverManager.getConnection("jdbc:sqlite:" + filter.getCampaign() + ".db");
                        //DatabaseConnection.setDbfile(filter.getCampaign() + ".db");
			XYChart.Series<Date, Number> result = generateGraph(connection);
			System.out.println("Finished Executing Query");
                        connection.close();
                        return result; 
			
		} catch (ParseException e) {
			e.printStackTrace();
                } catch (ClassNotFoundException exception) {
        		System.err.println("SQLite JDBC Library no found!");
                	System.exit(1);
                }
                return null; 
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
