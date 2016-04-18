package tests;

import java.sql.SQLException;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import junit.framework.TestCase;

import org.junit.Test;

import dashboard.controller.BounceGraphConstructor;
import dashboard.controller.ImpressionsGraphConstructor;
import dashboard.model.BounceFilter;
import dashboard.model.DatabaseConnection;
import dashboard.model.Filter;

public class BounceDataTest extends TestCase {
	Filter filter;
	BounceFilter bounceFilter;
	
	@Override
	public void setUp() {
		DatabaseConnection.setDbfile("TestData.db");
		filter = new Filter();
		filter.setCampaign("TestData");
		bounceFilter = new BounceFilter();
		bounceFilter.setPageLimit(1);
	}

	@Test
	public void testFirstMinuteTotal() {
		try {
			filter.setTime("Minutes");
			
			BounceGraphConstructor bounceConstructor = new BounceGraphConstructor(filter, bounceFilter);

			ObservableList<XYChart.Data<Date, Number>> data = bounceConstructor.fetchGraph().getData();
			assertEquals(360, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourTotal() {
		try {
			filter.setTime("Hours");
			
			BounceGraphConstructor bounceConstructor = new BounceGraphConstructor(filter, bounceFilter);

			ObservableList<XYChart.Data<Date, Number>> data = bounceConstructor.fetchGraph().getData();
			assertEquals(720, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstDayTotal() {
		try {
			filter.setTime("Days");
			
			BounceGraphConstructor bounceConstructor = new BounceGraphConstructor(filter, bounceFilter);

			ObservableList<XYChart.Data<Date, Number>> data = bounceConstructor.fetchGraph().getData();
			assertEquals(1080, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstWeekTotal() {
		try {
			filter.setTime("Weeks");
			
			BounceGraphConstructor bounceConstructor = new BounceGraphConstructor(filter, bounceFilter);

			ObservableList<XYChart.Data<Date, Number>> data = bounceConstructor.fetchGraph().getData();
			assertEquals(1440, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}
	
	/*
	 * Test the edge-case where an invalid time granuality is set.
	 * Currently assuming that default goes to hours
	 */
	@Test
	public void testInvalidTimeTotal() {
		try {
			filter.setTime("Other");
			
			BounceGraphConstructor bounceConstructor = new BounceGraphConstructor(filter, bounceFilter);

			ObservableList<XYChart.Data<Date, Number>> data = bounceConstructor.fetchGraph().getData();
			assertEquals(720, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourGender() {
		try {
			filter.setGender(FXCollections.observableArrayList("Female"));
			BounceGraphConstructor bounceConstructor = new BounceGraphConstructor(filter, bounceFilter);

			ObservableList<XYChart.Data<Date, Number>> data = bounceConstructor.fetchGraph().getData();
			assertEquals(360, data.get(0).getYValue());

			filter.setGender(FXCollections.observableArrayList("Male"));
			bounceConstructor = new BounceGraphConstructor(filter, bounceFilter);

			data = bounceConstructor.fetchGraph().getData();
			assertEquals(360, data.get(0).getYValue());
			
			//Test invalid gender
			filter.setGender(FXCollections.observableArrayList("Invalid"));
			bounceConstructor = new BounceGraphConstructor(filter, bounceFilter);

			data = bounceConstructor.fetchGraph().getData();
			assertEquals(720, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourAge() {
		try {
			filter.setAge(FXCollections.observableArrayList("Less than 25"));
			BounceGraphConstructor bounceConstructor = new BounceGraphConstructor(filter, bounceFilter);

			ObservableList<XYChart.Data<Date, Number>> data = bounceConstructor.fetchGraph().getData();
			assertEquals(144, data.get(0).getYValue());

			filter.setAge(FXCollections.observableArrayList("25 to 34"));
			bounceConstructor = new BounceGraphConstructor(filter, bounceFilter);

			data = bounceConstructor.fetchGraph().getData();
			assertEquals(144, data.get(0).getYValue());

			filter.setAge(FXCollections.observableArrayList("35 to 44"));
			bounceConstructor = new BounceGraphConstructor(filter, bounceFilter);

			data = bounceConstructor.fetchGraph().getData();
			assertEquals(144, data.get(0).getYValue());

			filter.setAge(FXCollections.observableArrayList("45 to 54"));
			bounceConstructor = new BounceGraphConstructor(filter, bounceFilter);

			data = bounceConstructor.fetchGraph().getData();
			assertEquals(144, data.get(0).getYValue());

			filter.setAge(FXCollections.observableArrayList("Greater than 55"));
			bounceConstructor = new BounceGraphConstructor(filter, bounceFilter);

			data = bounceConstructor.fetchGraph().getData();
			assertEquals(144, data.get(0).getYValue());
			
			//Test invalid age
			filter.setAge(FXCollections.observableArrayList("Invalid"));
			bounceConstructor = new BounceGraphConstructor(filter, bounceFilter);

			data = bounceConstructor.fetchGraph().getData();
			assertEquals(720, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourIncome() {
		try {
			filter.setIncome(FXCollections.observableArrayList("Low"));
			BounceGraphConstructor bounceConstructor = new BounceGraphConstructor(filter, bounceFilter);

			ObservableList<XYChart.Data<Date, Number>> data = bounceConstructor.fetchGraph().getData();
			assertEquals(240, data.get(0).getYValue());

			filter.setIncome(FXCollections.observableArrayList("Medium"));
			bounceConstructor = new BounceGraphConstructor(filter, bounceFilter);

			data = bounceConstructor.fetchGraph().getData();
			assertEquals(240, data.get(0).getYValue());

			filter.setIncome(FXCollections.observableArrayList("High"));
			bounceConstructor = new BounceGraphConstructor(filter, bounceFilter);

			data = bounceConstructor.fetchGraph().getData();
			assertEquals(240, data.get(0).getYValue());
			
			//Test invalid Income
			filter.setIncome(FXCollections.observableArrayList("Invalid"));
			bounceConstructor = new BounceGraphConstructor(filter, bounceFilter);

			data = bounceConstructor.fetchGraph().getData();
			assertEquals(720, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourContext() {
		try {
			filter.setContext(FXCollections.observableArrayList("News"));
			BounceGraphConstructor bounceConstructor = new BounceGraphConstructor(filter, bounceFilter);

			ObservableList<XYChart.Data<Date, Number>> data = bounceConstructor.fetchGraph().getData();
			assertEquals(120, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Shopping"));
			bounceConstructor = new BounceGraphConstructor(filter, bounceFilter);

			data = bounceConstructor.fetchGraph().getData();
			assertEquals(120, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Social Media"));
			bounceConstructor = new BounceGraphConstructor(filter, bounceFilter);

			data = bounceConstructor.fetchGraph().getData();
			assertEquals(120, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Blog"));
			bounceConstructor = new BounceGraphConstructor(filter, bounceFilter);

			data = bounceConstructor.fetchGraph().getData();
			assertEquals(120, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Hobbies"));
			bounceConstructor = new BounceGraphConstructor(filter, bounceFilter);

			data = bounceConstructor.fetchGraph().getData();
			assertEquals(120, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Travel"));
			bounceConstructor = new BounceGraphConstructor(filter, bounceFilter);

			data = bounceConstructor.fetchGraph().getData();
			assertEquals(120, data.get(0).getYValue());
			
			//Test invalid context
			filter.setContext(FXCollections.observableArrayList("Invalid"));
			bounceConstructor = new BounceGraphConstructor(filter, bounceFilter);

			data = bounceConstructor.fetchGraph().getData();
			assertEquals(720, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}
}
