package dashboard.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
 
import javafx.concurrent.Task;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Read a CSV file and load it into the database
 */
public class CSVReader {
	 long currentBytes = 0;
	 long totBytes = 0;
	/**
	 * Check if file exists
	 * @param file The file
	 * @return If the file exists
	 */
	private boolean verifyFile(File file) {
		if (!file.exists() || file.isDirectory()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText(null);
			alert.setContentText(file + " does not exist");
			alert.showAndWait();
			return false;
		} else totBytes += file.length();
		return true;
	}

	/**
	 * Check if the required CSVs exist that we need for the dashboard
	 * @param folder The folder
	 * @return If the CSVs exist
	 */
	public boolean checkFilesExist(String folder) {
		totBytes = 0;
		return verifyFile(new File(folder + "/impression_log.csv"))
		&& verifyFile(new File(folder + "/click_log.csv"))
		&& verifyFile(new File(folder + "/server_log.csv"));
	}
			
	public Task<?> readImpressions(File fname) {
		return new Task<Object>() {
			@Override
			protected Object call() throws Exception {
				try {
					Connection conn = DatabaseConnection.getConnection(); 
					Statement stmt = conn.createStatement();
					stmt.executeUpdate("drop table if exists IMPRESSIONS;");
					// sqlite RECOGNISES text, integer, real does not have boolean or
					// datetime
					// store the lookup fields ( gender, age, income and context) as
					// integers as it is faster to save and filter on integers than
					// strings
		
					String sql = "CREATE TABLE IMPRESSIONS " + "(DATE	  TEXT NOT NULL," + " ID		INTEGER   NOT NULL,"
							+ " GENDER	INTEGER NOT NULL," + // store this as integer
							// as it will be faster
							// to filter
							" AGE	   INTEGER NOT NULL," + // store 0 - <25, 1 =
							// 25-34, 2 = 35-44, 3 =
							// 45-54, 4=>54
							" INCOME	INTEGER NOT NULL," + // 0 = Low, 1 = Medium, 2 =
							// High
							" CONTEXT   INTEGER NOT NULL," + // store as 0 = News
							// 1=,Shopping,2=Social
							// Media, 3 =Blog, 4 =
							// Hobbies, 5 = Travel
							" COST	  REAL NOT NULL) ";
		
					stmt.executeUpdate(sql);
					stmt.close();
		
					PreparedStatement prep = conn.prepareStatement("insert into IMPRESSIONS values (?, ?, ?, ?, ?, ?, ?);");
					// set autocommit to false so it updated the records in one go
					// instead of individually - much quicker
					conn.setAutoCommit(false);
								
					BufferedReader br = new BufferedReader(new FileReader(fname));
					long i = 0;
					long j = 0; // commit count
								
					String line = br.readLine(); // ignore first line - todo check file
					currentBytes += line.length();							// is not empty
					while ((line = br.readLine()) != null) {
						currentBytes += line.length();
						
						String[] values = line.split(",");
							
						updateProgress(currentBytes, totBytes);
											
						prep.setString(1, values[0]);
						prep.setLong(2, Long.parseLong(values[1]));
						prep.setBoolean(3, values[2].toUpperCase().equals("FEMALE"));
		
						// (store as 0 - 4)
						switch (values[3]) {
							case "<25":   prep.setInt(4, 0); break;
							case "25-34": prep.setInt(4, 1); break;
							case "35-44": prep.setInt(4, 2); break;
							case "45-54": prep.setInt(4, 3); break;
							default:	  prep.setInt(4, 4); break;
						}
						if (values[4].toUpperCase().equals("LOW"))
							prep.setInt(5, 0);
						else if (values[4].toUpperCase().equals("HIGH"))
							prep.setInt(5, 2);
						else
							prep.setInt(5, 1);
						switch (values[5].toUpperCase()) {
							case "NEWS":		 prep.setInt(6, 0); break;
							case "SHOPPING":	 prep.setInt(6, 1); break;
							case "SOCIAL MEDIA": prep.setInt(6, 2); break;
							case "BLOG":		 prep.setInt(6, 3); break;
							case "HOBBIES":		 prep.setInt(6, 4); break;
							default:			 prep.setInt(6, 5); break;
						}
		
						prep.setDouble(7, Double.parseDouble(values[6]));
						prep.addBatch();
						// need to commit data after 500000 as it will run out of memory
						// if too many records
						// 500,000 seems to be ok, the lower the number the longer it
						// takes
						if (i++ == 500000) {
							prep.executeBatch();
							i = 0;
							System.out.println("IMPRESSION record " + j++);
						}
		
					}
					br.close();
					if (i > 0) prep.executeBatch();
					conn.setAutoCommit(true);
					// create index after inserting as it's quicker
					// may want to add index to Date field too
					// but test filters first
					System.out.println("IMPRESSION table create index");
					stmt = conn.createStatement();
					stmt.executeUpdate("CREATE INDEX IMRESSION_ID ON IMPRESSIONS(ID)");
					stmt.close();
				} catch (SQLException | IOException | NumberFormatException e) {
					e.printStackTrace();
				}
				updateMessage("Done Impression");
				System.out.println("Impression Table created successfully");
				return true;
			}
		};
	}
	
	public Task<?> readClicks(File fname) {
		return new Task<Object>() {
			@Override
			protected Object call() throws Exception {
				try {
					Connection conn = DatabaseConnection.getConnection(); 
					Statement stmt = conn.createStatement();
					stmt.executeUpdate("drop table if exists CLICKS;");
					String sql = "CREATE TABLE CLICKS " + "(DATE	  TEXT NOT NULL," + " ID 		INTEGER NOT NULL,"
							+ " COST	  REAL NOT NULL)";
					stmt.executeUpdate(sql);
					stmt.executeUpdate("CREATE INDEX CLICKS_ID ON CLICKS(ID)");
					stmt.close();
		
					PreparedStatement prep = conn.prepareStatement("insert into CLICKS values ( ?, ?, ?);");
		
					BufferedReader br = new BufferedReader(new FileReader(fname));
					conn.setAutoCommit(false);
					String line = br.readLine();
					long i = 0, j = 0; // commit count
								currentBytes += line.length();
					while ((line = br.readLine()) != null) {
						String[] values = line.split(",");
										currentBytes += line.length();
										updateProgress(currentBytes, totBytes);
						prep.setString(1, values[0]);
						prep.setLong(2, Long.parseLong(values[1]));
						prep.setDouble(3, Double.parseDouble(values[2]));
						prep.addBatch();
						// need to commit data after 500000 as it will run out of memory
						// if too many records
						// 500,000 seems to be ok, the lower the number the longer it
						// takes
						if (i++ == 500000) {
							prep.executeBatch();
							i = 0;
							System.out.println("Clicks record " + j++);
						}
					}
					br.close();
					if (i > 0) prep.executeBatch();
				} catch (Exception e) {
					e.printStackTrace();
				}
		
				updateMessage("Done Clicks");
				System.out.println("Clicks Table created successfully");
				return true;
			}
			};
		}

	 public Task<?> readServer(File fname) {
		return new Task<Object>() {
			@Override
			protected Object call() throws Exception {
				try {
					Connection conn = DatabaseConnection.getConnection(); 
					Statement stmt = conn.createStatement();
					stmt.executeUpdate("drop table if exists SERVER;");
					String sql = "CREATE TABLE SERVER " + "(ENTRYDATE TEXT  NOT NULL," + " ID 		INTEGER NOT NULL,"
							+ " EXITDATE  TEXT NOT NULL," + " PAGES		INTEGER	NOT NULL," + " CONVERSION  INTEGER NOT NULL)";
					stmt.executeUpdate(sql);
					stmt.executeUpdate("CREATE INDEX SERVER_ID ON SERVER(ID)");
					stmt.close();
		
					PreparedStatement prep = conn.prepareStatement("insert into SERVER values ( ?, ?, ?, ?, ?);");
		
					BufferedReader br = new BufferedReader(new FileReader(fname));
		
					String line = br.readLine();
					long i = 0, j = 0; // commit count
								currentBytes += line.length();
					while ((line = br.readLine()) != null) {
						String[] values = line.split(",");
										currentBytes += line.length();
										updateProgress(currentBytes, totBytes);
						prep.setString(1, values[0]);
						prep.setLong(2, Long.parseLong(values[1]));
						prep.setString(3, values[2]);
						prep.setInt(4, Integer.parseInt(values[3]));
						prep.setInt(5, 1);
						prep.setBoolean(5, values[4].equals("Yes"));
						prep.addBatch();
						// need to commit data after 500000 as it will run out of memory
						// if too many records
						// 500,000 seems to be ok, the lower the number the longer it
						// takes
						if (i++ == 500000) {
							prep.executeBatch();
							i = 0;
							System.out.println("Server record " + j++);
						}
					}
					br.close();
					if (i > 0) prep.executeBatch();
					conn.setAutoCommit(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				updateMessage("Done Server");
		 
				return true;
			}
		};
	}
}
