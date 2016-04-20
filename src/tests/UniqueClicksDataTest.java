package tests;

import java.sql.SQLException;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import junit.framework.TestCase;

import org.junit.Test;

import dashboard.controller.UniqueClicksGraphConstructor;
import dashboard.model.DatabaseConnection;
import dashboard.model.Filter;

public class UniqueClicksDataTest extends TestCase {
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
			
			UniqueClicksGraphConstructor clicksConstructor = new UniqueClicksGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = clicksConstructor.fetchGraph().getData();
			assertEquals(180, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourTotal() {
		try {
			filter.setTime("Hours");
			
			UniqueClicksGraphConstructor clicksConstructor = new UniqueClicksGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = clicksConstructor.fetchGraph().getData();
			assertEquals(180, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstDayTotal() {
		try {
			filter.setTime("Days");
			
			UniqueClicksGraphConstructor clicksConstructor = new UniqueClicksGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = clicksConstructor.fetchGraph().getData();
			assertEquals(180, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstWeekTotal() {
		try {
			filter.setTime("Weeks");
			
			UniqueClicksGraphConstructor clicksConstructor = new UniqueClicksGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = clicksConstructor.fetchGraph().getData();
			assertEquals(180, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}
	
	@Test
	public void testInvalidTimeTotal() {
		try {
			filter.setTime("Other");
			
			UniqueClicksGraphConstructor clicksConstructor = new UniqueClicksGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = clicksConstructor.fetchGraph().getData();
			assertEquals(180, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourGender() {
		try {
			filter.setGender(FXCollections.observableArrayList("Female"));
			UniqueClicksGraphConstructor clicksConstructor = new UniqueClicksGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = clicksConstructor.fetchGraph().getData();
			assertEquals(90, data.get(0).getYValue());

			filter.setGender(FXCollections.observableArrayList("Male"));
			clicksConstructor = new UniqueClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(90, data.get(0).getYValue());
			
			//Test invalid gender
			filter.setGender(FXCollections.observableArrayList("Invalid"));
			clicksConstructor = new UniqueClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(180, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourAge() {
		try {
			filter.setAge(FXCollections.observableArrayList("Less than 25"));
			UniqueClicksGraphConstructor clicksConstructor = new UniqueClicksGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = clicksConstructor.fetchGraph().getData();
			assertEquals(36, data.get(0).getYValue());

			filter.setAge(FXCollections.observableArrayList("25 to 34"));
			clicksConstructor = new UniqueClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(36, data.get(0).getYValue());

			filter.setAge(FXCollections.observableArrayList("35 to 44"));
			clicksConstructor = new UniqueClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(36, data.get(0).getYValue());

			filter.setAge(FXCollections.observableArrayList("45 to 54"));
			clicksConstructor = new UniqueClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(36, data.get(0).getYValue());

			filter.setAge(FXCollections.observableArrayList("Greater than 55"));
			clicksConstructor = new UniqueClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(36, data.get(0).getYValue());
			
			//Test invalid age
			filter.setAge(FXCollections.observableArrayList("Invalid"));
			clicksConstructor = new UniqueClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(180, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourIncome() {
		try {
			filter.setIncome(FXCollections.observableArrayList("Low"));
			UniqueClicksGraphConstructor clicksConstructor = new UniqueClicksGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = clicksConstructor.fetchGraph().getData();
			assertEquals(60, data.get(0).getYValue());

			filter.setIncome(FXCollections.observableArrayList("Medium"));
			clicksConstructor = new UniqueClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(60, data.get(0).getYValue());

			filter.setIncome(FXCollections.observableArrayList("High"));
			clicksConstructor = new UniqueClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(60, data.get(0).getYValue());
			
			//Test invalid income
			filter.setIncome(FXCollections.observableArrayList("Invalid"));
			clicksConstructor = new UniqueClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(180, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourContext() {
		try {
			filter.setContext(FXCollections.observableArrayList("News"));
			UniqueClicksGraphConstructor clicksConstructor = new UniqueClicksGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = clicksConstructor.fetchGraph().getData();
			assertEquals(30, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Shopping"));
			clicksConstructor = new UniqueClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(30, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Social Media"));
			clicksConstructor = new UniqueClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(30, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Blog"));
			clicksConstructor = new UniqueClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(30, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Hobbies"));
			clicksConstructor = new UniqueClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(30, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Travel"));
			clicksConstructor = new UniqueClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(30, data.get(0).getYValue());
			
			//Test invalid context
			filter.setContext(FXCollections.observableArrayList("Invalid"));
			clicksConstructor = new UniqueClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(180, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}
}
