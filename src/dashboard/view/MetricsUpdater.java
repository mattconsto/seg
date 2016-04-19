package dashboard.view;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import javafx.collections.ObservableList;
import dashboard.model.BounceFilter;
import dashboard.model.DatabaseConnection;
import dashboard.model.Filter;
import dashboard.model.ObservableMetrics;
import javafx.scene.control.TableView;

public class MetricsUpdater implements Runnable {
	private ObservableList<ObservableMetrics> table;
	private TableView<ObservableMetrics> tableView;
	private Filter                            filter;
	private BounceFilter bounceFilter;
	private boolean                           running = false;
	private int iFilter;

	public MetricsUpdater(ObservableList<ObservableMetrics> table, Filter filter, BounceFilter bounceFilter, int index,
			TableView<ObservableMetrics> t) {
		this.tableView = t;
		this.table  = table;
		this.filter = filter;
		this.bounceFilter = bounceFilter;
		this.iFilter = index;
	}

	@Override
	public void run() {
		try {
			running = true;
			updateMetricsTable();
		} catch (SQLException e) {
			System.err.println("Failed to update");
		}
	}
	
	public void stop() {
		running = false;
	}
	
	private void updateMetricsTable() throws SQLException {
		DecimalFormat intFormatter = new DecimalFormat("#,###");
		DecimalFormat decFormatter = new DecimalFormat("#,###.00");
		String currency = "£";
		
		DatabaseConnection.setDbfile(filter.getCampaign() + ".db");
		Connection conn = DatabaseConnection.getConnection();
		//table.clear();
		ResultSet results = conn.createStatement().executeQuery("SELECT COUNT(*) AS Frequency, * FROM "
				+ "(SELECT IMPRESSIONS.*, SERVER.* FROM IMPRESSIONS "
				+ "INNER JOIN SERVER ON IMPRESSIONS.ID=SERVER.ID "
				+ "GROUP BY SERVER.ENTRYDATE, SERVER.ID) AS SUBQUERY "
				+ "WHERE "+ bounceFilter.getSQL() +" AND " + filter.getSql() + ";");

		if (results.next()) table.get(0).setResults(iFilter, intFormatter.format(results.getDouble(1)));
		
		if(!running) {
			results.close();
			return;
		}

		results = conn.createStatement().executeQuery("SELECT COUNT(*) AS Frequency, * FROM"
				+ "(SELECT IMPRESSIONS.*, CLICKS.* FROM IMPRESSIONS"
				+ " INNER JOIN CLICKS ON IMPRESSIONS.ID=CLICKS.ID"
				+ " GROUP BY CLICKS.DATE, CLICKS.ID) AS SUBQUERY"
				+ " WHERE " + filter.getSql() + ";");

		//if (results.next()) table.add(new ObservableMetrics("Clicks",results.getString(1)));
		if (results.next()) table.get(1).setResults(iFilter, intFormatter.format(results.getDouble(1)));
		
		if(!running) {
			results.close();
			return;
		}

		results = conn.createStatement().executeQuery("SELECT COUNT(*) AS Frequency, * "
				+ "FROM (SELECT IMPRESSIONS.*, SERVER.* FROM "
				+ "IMPRESSIONS INNER JOIN SERVER ON IMPRESSIONS.ID=SERVER.ID "
				+ "GROUP BY SERVER.ENTRYDATE, SERVER.ID) AS SUBQUERY "
				+ "WHERE CONVERSION = 1 AND " + filter.getSql() + ";");
                if (results.next()) table.get(2).setResults(iFilter, intFormatter.format(results.getDouble(1)));
		
		//if (results.next()) table.add(new ObservableMetrics("Conversions",results.getString(1)));
		
		if(!running) {
			results.close();
			return;
		}

		results = conn.createStatement().executeQuery("SELECT COUNT(*) AS Frequency, * FROM IMPRESSIONS WHERE " +  filter.getSql() +";");
		
		if (results.next()) table.get(3).setResults(iFilter, intFormatter.format(results.getDouble(1)));
		
		//if (results.next()) table.add(new ObservableMetrics("Impressions",results.getString(1)));
		
		if(!running) {
			results.close();
			return;
		}

		results = conn.createStatement().executeQuery("SELECT COUNT(DISTINCT ID) AS Frequency, * FROM"
				+ "(SELECT IMPRESSIONS.*, CLICKS.* FROM IMPRESSIONS"
				+ " INNER JOIN CLICKS ON IMPRESSIONS.ID=CLICKS.ID"
				+ " GROUP BY CLICKS.DATE, CLICKS.ID) AS SUBQUERY"
				+ " WHERE " +  filter.getSql() + ";");

		//if (results.next()) table.add(new ObservableMetrics("Unique Clicks",results.getString(1)));
		if (results.next()) table.get(4).setResults(iFilter, intFormatter.format(results.getDouble(1)));
		
		if(!running) {
			results.close();
			return;
		}
		
		results = conn.createStatement().executeQuery("SELECT COUNT(DISTINCT ID) AS Frequency, * FROM IMPRESSIONS WHERE " +  filter.getSql() +";");

		//if (results.next()) table.add(new ObservableMetrics("Unique Impressions",results.getString(1)));
		if (results.next()) table.get(5).setResults(iFilter, intFormatter.format(results.getDouble(1)));
		
		if(!running) {
			results.close();
			return;
		}

		results = conn.createStatement().executeQuery("SELECT CLICKCOST, IMPCOST FROM "
				+ "(SELECT CLICKDATE, SUM(CLICKCOST) AS CLICKCOST FROM "
				+ "(SELECT IMPRESSIONS.*, CLICKS.DATE AS CLICKDATE, CLICKS.ID, CLICKS.COST AS CLICKCOST "
				+ "FROM IMPRESSIONS "
				+ "INNER JOIN CLICKS "
				+ "ON IMPRESSIONS.ID=CLICKS.ID "
				+ "GROUP BY CLICKS.DATE, CLICKS.ID)"
				+ "WHERE " +  filter.getSql().replace("DATE","CLICKDATE") + ") "
				+ "INNER JOIN "
				+ "(SELECT  SUM(COST) AS IMPCOST FROM IMPRESSIONS "
				+ "WHERE " +  filter.getSql() + ")");
		
		//if (results.next()) table.add(new ObservableMetrics("Total Cost",Float.toString(results.getFloat(1)+results.getFloat(2))));
		if (results.next()) table.get(6).setResults(iFilter, currency+decFormatter.format(results.getFloat(1)+results.getFloat(2)));
		
		if(!running) {
			results.close();
			return;
		}
		
		results = conn.createStatement().executeQuery("SELECT SUM(NUMCLICKS), SUM(NUMIMP) FROM "
				+ "(SELECT CLICKDATE, NUMCLICKS, NUMIMP FROM "
				+ "(SELECT strftime('" + filter.getTimeFormatSQL() +"', CLICKDATE) as CLICKDATE, COUNT(*) AS NUMCLICKS FROM "
				+ "(SELECT CLICKS.DATE AS CLICKDATE, IMPRESSIONS.* FROM CLICKS INNER JOIN IMPRESSIONS ON CLICKS.ID=IMPRESSIONS.ID GROUP BY CLICKS.ID, CLICKDATE) "
				+ "WHERE " +  filter.getSql().replace("DATE", "CLICKDATE") + " GROUP BY strftime('" + filter.getTimeFormatSQL() +"', CLICKDATE)) "
				+ "INNER JOIN "
				+ "(SELECT strftime('" + filter.getTimeFormatSQL() +"', DATE) AS DATE, COUNT(*) AS NUMIMP FROM IMPRESSIONS "
				+ "WHERE " +  filter.getSql() + "GROUP BY strftime('" + filter.getTimeFormatSQL() +"', DATE)) "
				+ "ON DATE=CLICKDATE)");

		//if (results.next()) table.add(new ObservableMetrics("CTR",Float.toString(results.getInt(1)/results.getFloat(2)*100)+"%"));
		if (results.next()) table.get(7).setResults(iFilter,String.format("%.3f%%", results.getInt(1)/results.getFloat(2)*100));
		if(!running) {
			results.close();
			return;
		}
		
		results = conn.createStatement().executeQuery("SELECT SUM(CLICKCOST), SUM(IMPCOST), SUM(Frequency) FROM "
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
				+ "ON CLICKDATE=ENTRYDATE;");
		
		//if (results.next()) table.add(new ObservableMetrics("CPA",Float.toString((results.getFloat(1) + results.getFloat(2)) / results.getInt(3))));
		if (results.next()) table.get(8).setResults(iFilter, currency+decFormatter.format((results.getFloat(1) + results.getFloat(2)) / results.getInt(3)));
		
		if(!running) {
			results.close();
			return;
		}
		
		results = conn.createStatement().executeQuery("SELECT SUM(CLICKCOST), SUM(IMPCOST), SUM(NUMCLICKS) FROM "
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
				+ "ON IMPDATE=CLICKDATE");
		
//		if (results.next()) table.add(new ObservableMetrics("CPC",Float.toString((results.getFloat(1) + results.getFloat(2)) / results.getInt(3))));
		if (results.next()) table.get(9).setResults(iFilter, currency+decFormatter.format((results.getFloat(1) + results.getFloat(2)) / results.getInt(3)));
		
		if(!running) {
			results.close();
			return;
		}
		
		results = conn.createStatement().executeQuery("SELECT SUM(CLICKCOST), SUM(IMPCOST), SUM(NUMIMPS) FROM"
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
				+ "ON IMPDATE=CLICKDATE)");
		
		//if (results.next()) table.add(new ObservableMetrics("CPM",Float.toString(((results.getFloat(1)+results.getFloat(2))/results.getFloat(3))*1000)));
		if (results.next()) table.get(10).setResults(iFilter, currency+decFormatter.format(((results.getFloat(1)+results.getFloat(2))/results.getFloat(3))*1000));
		
		if(!running) {
			results.close();
			return;
		}
		
		results = conn.createStatement().executeQuery("SELECT SUM(NUMCLICKS), SUM(NUMBOUNCES) FROM "
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
				+ "ON DATE=CLICKDATE GROUP BY DATE");
		
		//if (results.next()) table.add(new ObservableMetrics("Bounce Rate",String.format("%.1f%%", results.getInt(2)/results.getFloat(1)*100)));
		if (results.next()) table.get(11).setResults(iFilter,String.format("%.1f%%", results.getInt(2)/results.getFloat(1)*100));
		
		results.close();

		//  tableView.getColumns().get(iFilter +1).setVisible(true);
		tableView.refresh();
	}
}
