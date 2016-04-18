package tests;

import java.sql.SQLException;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import junit.framework.TestCase;

import org.junit.Test;

import dashboard.controller.ClicksGraphConstructor;
import dashboard.model.DatabaseConnection;
import dashboard.model.Filter;

public class ClicksDataTest extends TestCase {
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
			
			ClicksGraphConstructor clicksConstructor = new ClicksGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = clicksConstructor.fetchGraph().getData();
			assertEquals(720, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourTotal() {
		try {
			filter.setTime("Hours");
			
			ClicksGraphConstructor clicksConstructor = new ClicksGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = clicksConstructor.fetchGraph().getData();
			assertEquals(1440, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstDayTotal() {
		try {
			filter.setTime("Days");
			
			ClicksGraphConstructor clicksConstructor = new ClicksGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = clicksConstructor.fetchGraph().getData();
			assertEquals(2160, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstWeekTotal() {
		try {
			filter.setTime("Weeks");
			
			ClicksGraphConstructor clicksConstructor = new ClicksGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = clicksConstructor.fetchGraph().getData();
			assertEquals(2880, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}
	
	@Test
	public void testInvalidTimeTotal() {
		try {
			filter.setTime("Other");
			
			ClicksGraphConstructor clicksConstructor = new ClicksGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = clicksConstructor.fetchGraph().getData();
			assertEquals(1440, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourGender() {
		try {
			filter.setGender(FXCollections.observableArrayList("Female"));
			ClicksGraphConstructor clicksConstructor = new ClicksGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = clicksConstructor.fetchGraph().getData();
			assertEquals(720, data.get(0).getYValue());

			filter.setGender(FXCollections.observableArrayList("Male"));
			clicksConstructor = new ClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(720, data.get(0).getYValue());
			
			//Test invalid gender
			filter.setGender(FXCollections.observableArrayList("Invalid"));
			clicksConstructor = new ClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
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
			ClicksGraphConstructor clicksConstructor = new ClicksGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = clicksConstructor.fetchGraph().getData();
			assertEquals(288, data.get(0).getYValue());

			filter.setAge(FXCollections.observableArrayList("25 to 34"));
			clicksConstructor = new ClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(288, data.get(0).getYValue());

			filter.setAge(FXCollections.observableArrayList("35 to 44"));
			clicksConstructor = new ClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(288, data.get(0).getYValue());

			filter.setAge(FXCollections.observableArrayList("45 to 54"));
			clicksConstructor = new ClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(288, data.get(0).getYValue());

			filter.setAge(FXCollections.observableArrayList("Greater than 55"));
			clicksConstructor = new ClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(288, data.get(0).getYValue());
			
			//Test invalid age
			filter.setAge(FXCollections.observableArrayList("Invalid"));
			clicksConstructor = new ClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(1440, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourIncome() {
		try {
			filter.setIncome(FXCollections.observableArrayList("Low"));
			ClicksGraphConstructor clicksConstructor = new ClicksGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = clicksConstructor.fetchGraph().getData();
			assertEquals(480, data.get(0).getYValue());

			filter.setIncome(FXCollections.observableArrayList("Medium"));
			clicksConstructor = new ClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(480, data.get(0).getYValue());

			filter.setIncome(FXCollections.observableArrayList("High"));
			clicksConstructor = new ClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(480, data.get(0).getYValue());
			
			//Test invalid income
			filter.setIncome(FXCollections.observableArrayList("Invalid"));
			clicksConstructor = new ClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(1440, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourContext() {
		try {
			filter.setContext(FXCollections.observableArrayList("News"));
			ClicksGraphConstructor clicksConstructor = new ClicksGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = clicksConstructor.fetchGraph().getData();
			assertEquals(240, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Shopping"));
			clicksConstructor = new ClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(240, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Social Media"));
			clicksConstructor = new ClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(240, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Blog"));
			clicksConstructor = new ClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(240, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Hobbies"));
			clicksConstructor = new ClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(240, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Travel"));
			clicksConstructor = new ClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(240, data.get(0).getYValue());
			
			filter.setContext(FXCollections.observableArrayList("Invalid"));
			clicksConstructor = new ClicksGraphConstructor(filter);

			data = clicksConstructor.fetchGraph().getData();
			assertEquals(1440, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}
}
