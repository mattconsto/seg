package dashboard.view;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import javafx.collections.ObservableList;
import dashboard.model.BounceFilter;
import dashboard.model.Filter;
import dashboard.model.ObservableMetrics;
import java.sql.DriverManager;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javafx.scene.control.TableView;

public class MetricsUpdater  {
	private ObservableList<ObservableMetrics> table;
	private TableView<ObservableMetrics> tableView;
	private Filter                            filter;
	private int iFilter;
        private ExecutorService executor;
	public MetricsUpdater() {
                try {  
                    executor = Executors.newCachedThreadPool(); //.newFixedThreadPool(15);   
                }
                catch ( Exception eofException ) { // EOFException |InterruptedException
                    System.err.println("Failed to start threading " + eofException.getMessage());
                } // 
	}
        public void stop() {
             executor.shutdownNow();
        }
        public void runUpdater(ObservableList<ObservableMetrics> table, Filter filter, BounceFilter bounceFilter, int index,
			TableView<ObservableMetrics> t) {
               
                
                this.tableView = t;
		this.table  = table;
		this.filter = filter;
		this.iFilter = index;
                WorkerThread s =   new WorkerThread("SELECT COUNT(*) AS Frequency, * FROM "
				+ "(SELECT IMPRESSIONS.*, SERVER.* FROM IMPRESSIONS "
				+ "INNER JOIN SERVER ON IMPRESSIONS.ID=SERVER.ID "
				+ "GROUP BY SERVER.ENTRYDATE, SERVER.ID) AS SUBQUERY "
				+ "WHERE "+ bounceFilter.getSQL() +" AND " + filter.getSql() + ";", 0); 
              
                executor.execute(s);
                
                s =   new WorkerThread("SELECT COUNT(*) AS Frequency, * FROM "
				+ "(SELECT IMPRESSIONS.*, CLICKS.* FROM IMPRESSIONS"
				+ " INNER JOIN CLICKS ON IMPRESSIONS.ID=CLICKS.ID"
				+ " GROUP BY CLICKS.DATE, CLICKS.ID) AS SUBQUERY"
				+ " WHERE " + filter.getSql() + ";", 1); 
                
                executor.execute(s);
               
                s = new WorkerThread( "SELECT COUNT(*) AS Frequency, * "
				+ "FROM (SELECT IMPRESSIONS.*, SERVER.* FROM "
				+ "IMPRESSIONS INNER JOIN SERVER ON IMPRESSIONS.ID=SERVER.ID "
				+ "GROUP BY SERVER.ENTRYDATE, SERVER.ID) AS SUBQUERY "
				+ "WHERE CONVERSION = 1 AND " + filter.getSql() + ";", 2);
           
                executor.execute(s);
                
                s = new WorkerThread("SELECT COUNT(*) AS Frequency, * FROM IMPRESSIONS WHERE " +  filter.getSql() +";"  , 3);
          
                executor.execute(s);
                  s = new WorkerThread("SELECT COUNT(DISTINCT ID) AS Frequency, * FROM"
				+ "(SELECT IMPRESSIONS.*, CLICKS.* FROM IMPRESSIONS"
				+ " INNER JOIN CLICKS ON IMPRESSIONS.ID=CLICKS.ID"
				+ " GROUP BY CLICKS.DATE, CLICKS.ID) AS SUBQUERY"
				+ " WHERE " +  filter.getSql() + ";"  , 4);
              
                executor.execute(s);
                  s = new WorkerThread(  "SELECT COUNT(DISTINCT ID) AS Frequency, * FROM IMPRESSIONS WHERE " +  filter.getSql() +";", 5);
               
                executor.execute(s);
                  s = new WorkerThread( "SELECT CLICKCOST, IMPCOST FROM "
				+ "(SELECT CLICKDATE, SUM(CLICKCOST) AS CLICKCOST FROM "
				+ "(SELECT IMPRESSIONS.*, CLICKS.DATE AS CLICKDATE, CLICKS.ID, CLICKS.COST AS CLICKCOST "
				+ "FROM IMPRESSIONS "
				+ "INNER JOIN CLICKS "
				+ "ON IMPRESSIONS.ID=CLICKS.ID "
				+ "GROUP BY CLICKS.DATE, CLICKS.ID)"
				+ "WHERE " +  filter.getSql().replace("DATE","CLICKDATE") + ") "
				+ "INNER JOIN "
				+ "(SELECT  SUM(COST) AS IMPCOST FROM IMPRESSIONS "
				+ "WHERE " +  filter.getSql() + ")" , 6);
               
                executor.execute(s);
                  s = new WorkerThread( "SELECT SUM(NUMCLICKS), SUM(NUMIMP) FROM "
				+ "(SELECT CLICKDATE, NUMCLICKS, NUMIMP FROM "
				+ "(SELECT strftime('" + filter.getTimeFormatSQL() +"', CLICKDATE) as CLICKDATE, COUNT(*) AS NUMCLICKS FROM "
				+ "(SELECT CLICKS.DATE AS CLICKDATE, IMPRESSIONS.* FROM CLICKS INNER JOIN IMPRESSIONS ON CLICKS.ID=IMPRESSIONS.ID GROUP BY CLICKS.ID, CLICKDATE) "
				+ "WHERE " +  filter.getSql().replace("DATE", "CLICKDATE") + " GROUP BY strftime('" + filter.getTimeFormatSQL() +"', CLICKDATE)) "
				+ "INNER JOIN "
				+ "(SELECT strftime('" + filter.getTimeFormatSQL() +"', DATE) AS DATE, COUNT(*) AS NUMIMP FROM IMPRESSIONS "
				+ "WHERE " +  filter.getSql() + "GROUP BY strftime('" + filter.getTimeFormatSQL() +"', DATE)) "
				+ "ON DATE=CLICKDATE)" , 7);
             
                executor.execute(s);
                  s = new WorkerThread( "SELECT SUM(CLICKCOST), SUM(IMPCOST), SUM(Frequency) FROM "
				+ "(SELECT strftime('" + filter.getTimeFormatSQL() +"', ENTRYDATE) AS ENTRYDATE,COUNT(*) AS Frequency "
				+ "FROM (SELECT IMPRESSIONS.*, SERVER.* FROM "
				+ "IMPRESSIONS INNER JOIN SERVER ON IMPRESSIONS.ID=SERVER.ID "
				+ "GROUP BY SERVER.ENTRYDATE, SERVER.ID) AS SUBQUERY "
				+ "WHERE CONVERSION = 1 AND " +  filter.getSql() + " "
				+ "GROUP BY strftime('" + filter.getTimeFormatSQL() +"', ENTRYDATE))"
				+ "INNER JOIN "
				+ "(SELECT CLICKDATE, CLICKCOST, IMPCOST FROM "
				+ "(SELECT strftime('" + filter.getTimeFormatSQL() +"', CLICKDATE) AS CLICKDATE, SUM(CLICKCOST) AS CLICKCOST FROM "
				+ "(SELECT IMPRESSIONS.*, CLICKS.ID, CLICKS.DATE AS CLICKDATE, CLICKS.COST AS CLICKCOST "
				+ "FROM IMPRESSIONS "
				+ "INNER JOIN CLICKS "
				+ "ON IMPRESSIONS.ID=CLICKS.ID "
				+ "GROUP BY CLICKS.DATE, CLICKS.ID) "
				+ "WHERE " +  filter.getSql().replace("DATE", "CLICKDATE") + " GROUP BY strftime('" + filter.getTimeFormatSQL() +"', CLICKDATE)) "
				+ "INNER JOIN "
				+ "(SELECT strftime('" + filter.getTimeFormatSQL() +"', DATE) AS IMPDATE, SUM(COST) AS IMPCOST FROM IMPRESSIONS "
				+ "WHERE " +  filter.getSql() + " GROUP BY strftime('" + filter.getTimeFormatSQL() +"', DATE)) "
				+ "ON IMPDATE=CLICKDATE) "
				+ "ON CLICKDATE=ENTRYDATE;" , 8);
           
                executor.execute(s);
                  s = new WorkerThread( "SELECT SUM(CLICKCOST), SUM(IMPCOST), SUM(NUMCLICKS) FROM "
				+ "(SELECT strftime('" + filter.getTimeFormatSQL() +"', CLICKDATE) AS CLICKDATE, SUM(CLICKCOST) AS CLICKCOST, COUNT(ID) AS NUMCLICKS FROM "
				+ "(SELECT IMPRESSIONS.*, CLICKS.ID, CLICKS.DATE AS CLICKDATE, CLICKS.COST AS CLICKCOST "
				+ "FROM IMPRESSIONS "
				+ "INNER JOIN CLICKS "
				+ "ON IMPRESSIONS.ID=CLICKS.ID "
				+ "GROUP BY CLICKS.DATE, CLICKS.ID) "
				+ "WHERE " + filter.getSql().replace("DATE", "CLICKDATE")+ " GROUP BY strftime('" + filter.getTimeFormatSQL() +"', CLICKDATE)) "
				+ "INNER JOIN "
				+ "(SELECT strftime('" + filter.getTimeFormatSQL() +"', DATE) AS IMPDATE, SUM(COST) AS IMPCOST FROM IMPRESSIONS "
				+ "WHERE " + filter.getSql()+ " GROUP BY strftime('" + filter.getTimeFormatSQL() +"', DATE)) "
				+ "ON IMPDATE=CLICKDATE", 9);
             
                executor.execute(s);
                 s = new WorkerThread( "SELECT SUM(CLICKCOST), SUM(IMPCOST), SUM(NUMIMPS) FROM"
				+ "(SELECT IMPDATE, CLICKCOST, IMPCOST, NUMIMPS FROM "
				+ "(SELECT strftime('" + filter.getTimeFormatSQL() +"', CLICKDATE) AS CLICKDATE, SUM(CLICKCOST) AS CLICKCOST FROM "
				+ "(SELECT IMPRESSIONS.*, CLICKS.ID, CLICKS.DATE AS CLICKDATE, CLICKS.COST AS CLICKCOST "
				+ "FROM IMPRESSIONS "
				+ "INNER JOIN CLICKS "
				+ "ON IMPRESSIONS.ID=CLICKS.ID "
				+ "GROUP BY CLICKS.DATE, CLICKS.ID) "
				+ "WHERE " + filter.getSql().replace("DATE", "CLICKDATE")+ " GROUP BY strftime('" + filter.getTimeFormatSQL() +"', CLICKDATE)) "
				+ "INNER JOIN "
				+ "(SELECT strftime('" + filter.getTimeFormatSQL() +"', DATE) AS IMPDATE, SUM(COST) AS IMPCOST, COUNT(ID) AS NUMIMPS FROM IMPRESSIONS "
				+ "WHERE " + filter.getSql()+ " GROUP BY strftime('" + filter.getTimeFormatSQL() +"', DATE)) "
				+ "ON IMPDATE=CLICKDATE)" , 10);
            
                executor.execute(s);
                 s = new WorkerThread( "SELECT SUM(NUMCLICKS), SUM(NUMBOUNCES) FROM "
				+ "(SELECT strftime('" + filter.getTimeFormatSQL() +"', CLICKDATE) as CLICKDATE, COUNT(ID) AS NUMCLICKS FROM "
				+ "(SELECT CLICKS.DATE AS CLICKDATE, IMPRESSIONS.* FROM CLICKS INNER JOIN IMPRESSIONS ON CLICKS.ID=IMPRESSIONS.ID GROUP BY CLICKS.ID, CLICKDATE) "
				+ "WHERE " + filter.getSql().replace("DATE", "CLICKDATE")+ " GROUP BY strftime('" + filter.getTimeFormatSQL() +"', CLICKDATE)) "
				+ "INNER JOIN "
				+ "(SELECT strftime('" + filter.getTimeFormatSQL() +"', ENTRYDATE) AS DATE, COUNT(*) AS NUMBOUNCES FROM "
				+ "(SELECT IMPRESSIONS.*, SERVER.* FROM IMPRESSIONS "
				+ "INNER JOIN SERVER ON IMPRESSIONS.ID=SERVER.ID "
				+ "GROUP BY SERVER.ENTRYDATE, SERVER.ID) AS SUBQUERY "
				+ "WHERE "+bounceFilter.getSQL()+" AND "+ filter.getSql().replace("DATE", "ENTRYDATE")
				+ " GROUP BY strftime('" + filter.getTimeFormatSQL() +"', ENTRYDATE)) "
				+ "ON DATE=CLICKDATE GROUP BY DATE" , 11);
               
                executor.execute(s);
                
         }
 
        public class WorkerThread implements Runnable {
          private String query;
          private int index =0;
       
              
        public WorkerThread(String qry, int i){
              query = qry;
              index = i;
        }
       
	@Override
	public void run() {
              DecimalFormatSymbols symbols = new DecimalFormat().getDecimalFormatSymbols();
		DecimalFormat intFormatter = new DecimalFormat("#" + symbols.getGroupingSeparator() + "###");
		DecimalFormat decFormatter = new DecimalFormat("#" + symbols.getGroupingSeparator() + "###" + symbols.getDecimalSeparator() + "00");
		String currency = "£";
                Connection connection = null;
             
             
                   try {
                       if(connection == null || connection.isClosed()) {
				Class.forName("org.sqlite.JDBC");
			        connection = DriverManager.getConnection("jdbc:sqlite:" + filter.getCampaign() + ".db");
                       
	 
		  
                 ResultSet results = connection.createStatement().executeQuery( query);
                 if (results.next()) {
                     switch (index) {
                                  
                        case 7:
                            table.get(7).setResults(iFilter,String.format("%.3f%%", results.getInt(1)/results.getFloat(2)*100));
                            break;
                        case 6:
                            table.get(6).setResults(iFilter, currency+decFormatter.format(results.getFloat(1)+results.getFloat(2)));
                           
                            break;
                        case 8:
                            table.get(8).setResults(iFilter, currency+decFormatter.format((results.getFloat(1) + results.getFloat(2)) / results.getInt(3)));;
                            break;
                        case 9:
                             table.get(9).setResults(iFilter, currency+decFormatter.format((results.getFloat(1) + results.getFloat(2)) / results.getInt(3)));
                            break;
                        case 10:
                             table.get(10).setResults(iFilter, currency+decFormatter.format(((results.getFloat(1)+results.getFloat(2))/results.getFloat(3))*1000));
                            break;
                        case 11:
                            table.get(11).setResults(iFilter,String.format("%.1f%%", results.getInt(2)/results.getFloat(1)*100));
                            break;
                        default:
                            table.get(index).setResults(iFilter, intFormatter.format(results.getDouble(1)));
                            break;
                           
                        
                     }
                 }
                 results.close();
                 tableView.refresh();
                 tableView.scrollToColumnIndex(iFilter );
                 connection.close();
                 }
                     } catch (ClassNotFoundException exception) {
				System.err.println("SQLite JDBC Library no found!");
				System.exit(1);
                    }
                     catch (SQLException exception) {
				System.err.println("SQLite Open error");
				System.exit(1);
                     }
		
           
            
	}
	
	 
	 
        }
}
	 
           
		 
               

		