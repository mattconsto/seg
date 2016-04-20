package tests;

import java.sql.SQLException;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import junit.framework.TestCase;

import org.junit.Test;

import dashboard.controller.TotalCostGraphConstructor;
import dashboard.model.DatabaseConnection;
import dashboard.model.Filter;

public class TotalCostDataTest extends TestCase {
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
			
			TotalCostGraphConstructor totalCostConstructor = new TotalCostGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = totalCostConstructor.fetchGraph().getData();
			assertEquals(8640, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourTotal() {
		try {
			filter.setTime("Hours");
			
			TotalCostGraphConstructor totalCostConstructor = new TotalCostGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = totalCostConstructor.fetchGraph().getData();
			assertEquals(17280, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstDayTotal() {
		try {
			filter.setTime("Days");
			
			TotalCostGraphConstructor totalCostConstructor = new TotalCostGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = totalCostConstructor.fetchGraph().getData();
			assertEquals(25920, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstWeekTotal() {
		try {
			filter.setTime("Weeks");
			
			TotalCostGraphConstructor totalCostConstructor = new TotalCostGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = totalCostConstructor.fetchGraph().getData();
			assertEquals(34560, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}
	
	@Test
	public void testInvalidTimeTotal() {
		try {
			filter.setTime("Other");
			
			TotalCostGraphConstructor totalCostConstructor = new TotalCostGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = totalCostConstructor.fetchGraph().getData();
			assertEquals(17280, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}


	@Test
	public void testFirstHourGender() {
		try {
			filter.setGender(FXCollections.observableArrayList("Female"));
			TotalCostGraphConstructor totalCostConstructor = new TotalCostGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = totalCostConstructor.fetchGraph().getData();
			assertEquals(8640, data.get(0).getYValue());

			filter.setGender(FXCollections.observableArrayList("Male"));
			totalCostConstructor = new TotalCostGraphConstructor(filter);

			data = totalCostConstructor.fetchGraph().getData();
			assertEquals(8640, data.get(0).getYValue());
			
			//Test invalid gender
			filter.setGender(FXCollections.observableArrayList("Invalid"));
			totalCostConstructor = new TotalCostGraphConstructor(filter);

			data = totalCostConstructor.fetchGraph().getData();
			assertEquals(17280, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourAge() {
		try {
			filter.setAge(FXCollections.observableArrayList("Less than 25"));
			TotalCostGraphConstructor totalCostConstructor = new TotalCostGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = totalCostConstructor.fetchGraph().getData();
			assertEquals(3456, data.get(0).getYValue());

			filter.setAge(FXCollections.observableArrayList("25 to 34"));
			totalCostConstructor = new TotalCostGraphConstructor(filter);

			data = totalCostConstructor.fetchGraph().getData();
			assertEquals(3456, data.get(0).getYValue());

			filter.setAge(FXCollections.observableArrayList("35 to 44"));
			totalCostConstructor = new TotalCostGraphConstructor(filter);

			data = totalCostConstructor.fetchGraph().getData();
			assertEquals(3456, data.get(0).getYValue());

			filter.setAge(FXCollections.observableArrayList("45 to 54"));
			totalCostConstructor = new TotalCostGraphConstructor(filter);

			data = totalCostConstructor.fetchGraph().getData();
			assertEquals(3456, data.get(0).getYValue());

			filter.setAge(FXCollections.observableArrayList("Greater than 55"));
			totalCostConstructor = new TotalCostGraphConstructor(filter);

			data = totalCostConstructor.fetchGraph().getData();
			assertEquals(3456, data.get(0).getYValue());
			
			//Test invalid age
			filter.setAge(FXCollections.observableArrayList("Invalid"));
			totalCostConstructor = new TotalCostGraphConstructor(filter);

			data = totalCostConstructor.fetchGraph().getData();
			assertEquals(17280, data.get(0).getYValue());
			
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourIncome() {
		try {
			filter.setIncome(FXCollections.observableArrayList("Low"));
			TotalCostGraphConstructor totalCostConstructor = new TotalCostGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = totalCostConstructor.fetchGraph().getData();
			assertEquals(5760, data.get(0).getYValue());

			filter.setIncome(FXCollections.observableArrayList("Medium"));
			totalCostConstructor = new TotalCostGraphConstructor(filter);

			data = totalCostConstructor.fetchGraph().getData();
			assertEquals(5760, data.get(0).getYValue());

			filter.setIncome(FXCollections.observableArrayList("High"));
			totalCostConstructor = new TotalCostGraphConstructor(filter);

			data = totalCostConstructor.fetchGraph().getData();
			assertEquals(5760, data.get(0).getYValue());
			
			//Test invalid income
			filter.setIncome(FXCollections.observableArrayList("Invalid"));
			totalCostConstructor = new TotalCostGraphConstructor(filter);

			data = totalCostConstructor.fetchGraph().getData();
			assertEquals(17280, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourContext() {
		try {
			filter.setContext(FXCollections.observableArrayList("News"));
			TotalCostGraphConstructor totalCostConstructor = new TotalCostGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = totalCostConstructor.fetchGraph().getData();
			assertEquals(2880, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Shopping"));
			totalCostConstructor = new TotalCostGraphConstructor(filter);

			data = totalCostConstructor.fetchGraph().getData();
			assertEquals(2880, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Social Media"));
			totalCostConstructor = new TotalCostGraphConstructor(filter);

			data = totalCostConstructor.fetchGraph().getData();
			assertEquals(2880, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Blog"));
			totalCostConstructor = new TotalCostGraphConstructor(filter);

			data = totalCostConstructor.fetchGraph().getData();
			assertEquals(2880, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Hobbies"));
			totalCostConstructor = new TotalCostGraphConstructor(filter);

			data = totalCostConstructor.fetchGraph().getData();
			assertEquals(2880, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Travel"));
			totalCostConstructor = new TotalCostGraphConstructor(filter);

			data = totalCostConstructor.fetchGraph().getData();
			assertEquals(2880, data.get(0).getYValue());
			
			//Test invalid context
			filter.setContext(FXCollections.observableArrayList("Invalid"));
			totalCostConstructor = new TotalCostGraphConstructor(filter);

			data = totalCostConstructor.fetchGraph().getData();
			assertEquals(17280, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}
}
