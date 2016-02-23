package dashboard.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database Singleton
 */
public class DatabaseConnection {
	private static Connection connection = null;
	private static String     dbfile     = "auction.db";
	
	/**
	 * Disallow initialisation
	 */
	private DatabaseConnection() {}
	
	/**
	 * Get a connection to the database
	 * @return A connection
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		if(connection == null || connection.isClosed()) {
			try {
				Class.forName("org.sqlite.JDBC");
			} catch (ClassNotFoundException exception) {
				System.err.println("SQLite JDBC Library no found!");
				System.exit(1);
			}
			connection = DriverManager.getConnection("jdbc:sqlite:" + dbfile);
			System.out.println("Opened database successfully");
		}
		return connection;
	}
	
	/**
	 * Close our connection to the database
	 * @return
	 * @throws SQLException
	 */
	public static boolean closeConnection() throws SQLException {
		if(connection != null) {
			connection.close();
			return connection.isClosed();
		} else {
			return true;
		}
	}
}
