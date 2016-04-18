package tests;

import java.sql.SQLException;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import junit.framework.TestCase;

import org.junit.Test;

import dashboard.controller.ImpressionsGraphConstructor;
import dashboard.model.DatabaseConnection;
import dashboard.model.Filter;

/*
 * Class tests for accuracy of Impressions series under various filter combinations
 */
public class ImpressionsDataTest extends TestCase {
	Filter filter;

	@Override
	public void setUp() {
		DatabaseConnection.setDbfile("TestData.db");
		filter = new Filter();
		filter.setCampaign("TestData");
	}

	@Test
	public void testFirstMinuteTotal() {
		try {
			filter.setTime("Minutes");
			
			ImpressionsGraphConstructor impressionsConstructor = new ImpressionsGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = impressionsConstructor.fetchGraph().getData();
			assertEquals(1440, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourTotal() {
		try {
			filter.setTime("Hours");
			
			ImpressionsGraphConstructor impressionsConstructor = new ImpressionsGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = impressionsConstructor.fetchGraph().getData();
			assertEquals(2880, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstDayTotal() {
		try {
			filter.setTime("Days");
			
			ImpressionsGraphConstructor impressionsConstructor = new ImpressionsGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = impressionsConstructor.fetchGraph().getData();
			assertEquals(4320, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstWeekTotal() {
		try {
			filter.setTime("Weeks");
			
			ImpressionsGraphConstructor impressionsConstructor = new ImpressionsGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = impressionsConstructor.fetchGraph().getData();
			assertEquals(5760, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}
	
	/*
	 * Test the edge-case where an invalid time granuality is set.
	 * Default case set to hours.
	 */
	@Test
	public void testInvalidTimeTotal() {
		try {
			filter.setTime("Other");
			
			ImpressionsGraphConstructor impressionsConstructor = new ImpressionsGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = impressionsConstructor.fetchGraph().getData();
			assertEquals(2880, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourGender() {
		try {
			filter.setGender(FXCollections.observableArrayList("Female"));
			ImpressionsGraphConstructor impressionsConstructor = new ImpressionsGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = impressionsConstructor.fetchGraph().getData();
			assertEquals(1440, data.get(0).getYValue());

			filter.setGender(FXCollections.observableArrayList("Male"));
			impressionsConstructor = new ImpressionsGraphConstructor(filter);

			data = impressionsConstructor.fetchGraph().getData();
			assertEquals(1440, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourAge() {
		try {
			filter.setAge(FXCollections.observableArrayList("Less than 25"));
			ImpressionsGraphConstructor impressionsConstructor = new ImpressionsGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = impressionsConstructor.fetchGraph().getData();
			assertEquals(576, data.get(0).getYValue());

			filter.setAge(FXCollections.observableArrayList("25 to 34"));
			impressionsConstructor = new ImpressionsGraphConstructor(filter);

			data = impressionsConstructor.fetchGraph().getData();
			assertEquals(576, data.get(0).getYValue());

			filter.setAge(FXCollections.observableArrayList("35 to 44"));
			impressionsConstructor = new ImpressionsGraphConstructor(filter);

			data = impressionsConstructor.fetchGraph().getData();
			assertEquals(576, data.get(0).getYValue());

			filter.setAge(FXCollections.observableArrayList("45 to 54"));
			impressionsConstructor = new ImpressionsGraphConstructor(filter);

			data = impressionsConstructor.fetchGraph().getData();
			assertEquals(576, data.get(0).getYValue());

			filter.setAge(FXCollections.observableArrayList("Greater than 55"));
			impressionsConstructor = new ImpressionsGraphConstructor(filter);

			data = impressionsConstructor.fetchGraph().getData();
			assertEquals(576, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourIncome() {
		try {
			filter.setIncome(FXCollections.observableArrayList("Low"));
			ImpressionsGraphConstructor impressionsConstructor = new ImpressionsGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = impressionsConstructor.fetchGraph().getData();
			assertEquals(960, data.get(0).getYValue());

			filter.setIncome(FXCollections.observableArrayList("Medium"));
			impressionsConstructor = new ImpressionsGraphConstructor(filter);

			data = impressionsConstructor.fetchGraph().getData();
			assertEquals(960, data.get(0).getYValue());

			filter.setIncome(FXCollections.observableArrayList("High"));
			impressionsConstructor = new ImpressionsGraphConstructor(filter);

			data = impressionsConstructor.fetchGraph().getData();
			assertEquals(960, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourContext() {
		try {
			filter.setContext(FXCollections.observableArrayList("News"));
			ImpressionsGraphConstructor impressionsConstructor = new ImpressionsGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = impressionsConstructor.fetchGraph().getData();
			assertEquals(480, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Shopping"));
			impressionsConstructor = new ImpressionsGraphConstructor(filter);

			data = impressionsConstructor.fetchGraph().getData();
			assertEquals(480, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Social Media"));
			impressionsConstructor = new ImpressionsGraphConstructor(filter);

			data = impressionsConstructor.fetchGraph().getData();
			assertEquals(480, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Blog"));
			impressionsConstructor = new ImpressionsGraphConstructor(filter);

			data = impressionsConstructor.fetchGraph().getData();
			assertEquals(480, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Hobbies"));
			impressionsConstructor = new ImpressionsGraphConstructor(filter);

			data = impressionsConstructor.fetchGraph().getData();
			assertEquals(480, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Travel"));
			impressionsConstructor = new ImpressionsGraphConstructor(filter);

			data = impressionsConstructor.fetchGraph().getData();
			assertEquals(480, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}
}
