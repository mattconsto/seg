package tests;

import java.sql.SQLException;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import junit.framework.TestCase;

import org.junit.Test;

import dashboard.controller.CPAGraphConstructor;
import dashboard.model.DatabaseConnection;
import dashboard.model.Filter;

public class CPADataTest extends TestCase {
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
			
			CPAGraphConstructor cpaConstructor = new CPAGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = cpaConstructor.fetchGraph().getData();
			assertEquals(48f, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourTotal() {
		try {
			filter.setTime("Hours");
			
			CPAGraphConstructor cpaConstructor = new CPAGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = cpaConstructor.fetchGraph().getData();
			assertEquals(48f, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstDayTotal() {
		try {
			filter.setTime("Days");
			
			CPAGraphConstructor cpaConstructor = new CPAGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = cpaConstructor.fetchGraph().getData();
			assertEquals(48f, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstWeekTotal() {
		try {
			filter.setTime("Weeks");
			
			CPAGraphConstructor cpaConstructor = new CPAGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = cpaConstructor.fetchGraph().getData();
			assertEquals(48f, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}
	
	@Test
	public void testInvalidTimeTotal() {
		try {
			filter.setTime("Other");
			
			CPAGraphConstructor cpaConstructor = new CPAGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = cpaConstructor.fetchGraph().getData();
			assertEquals(48f, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourGender() {
		try {
			filter.setGender(FXCollections.observableArrayList("Female"));
			CPAGraphConstructor cpaConstructor = new CPAGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = cpaConstructor.fetchGraph().getData();
			assertEquals(48f, data.get(0).getYValue());

			filter.setGender(FXCollections.observableArrayList("Male"));
			cpaConstructor = new CPAGraphConstructor(filter);

			data = cpaConstructor.fetchGraph().getData();
			assertEquals(48f, data.get(0).getYValue());
			
			//Test invalid gender
			filter.setGender(FXCollections.observableArrayList("Invalid"));
			cpaConstructor = new CPAGraphConstructor(filter);

			data = cpaConstructor.fetchGraph().getData();
			assertEquals(48f, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourAge() {
		try {
			filter.setAge(FXCollections.observableArrayList("Less than 25"));
			CPAGraphConstructor cpaConstructor = new CPAGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = cpaConstructor.fetchGraph().getData();
			assertEquals(48f, data.get(0).getYValue());

			filter.setAge(FXCollections.observableArrayList("25 to 34"));
			cpaConstructor = new CPAGraphConstructor(filter);

			data = cpaConstructor.fetchGraph().getData();
			assertEquals(48f, data.get(0).getYValue());

			filter.setAge(FXCollections.observableArrayList("35 to 44"));
			cpaConstructor = new CPAGraphConstructor(filter);

			data = cpaConstructor.fetchGraph().getData();
			assertEquals(48f, data.get(0).getYValue());

			filter.setAge(FXCollections.observableArrayList("45 to 54"));
			cpaConstructor = new CPAGraphConstructor(filter);

			data = cpaConstructor.fetchGraph().getData();
			assertEquals(48f, data.get(0).getYValue());

			filter.setAge(FXCollections.observableArrayList("Greater than 55"));
			cpaConstructor = new CPAGraphConstructor(filter);

			data = cpaConstructor.fetchGraph().getData();
			assertEquals(48f, data.get(0).getYValue());
			
			//Test invalid age
			filter.setAge(FXCollections.observableArrayList("Invalid"));
			cpaConstructor = new CPAGraphConstructor(filter);

			data = cpaConstructor.fetchGraph().getData();
			assertEquals(48f, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourIncome() {
		try {
			filter.setIncome(FXCollections.observableArrayList("Low"));
			CPAGraphConstructor cpaConstructor = new CPAGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = cpaConstructor.fetchGraph().getData();
			assertEquals(48f, data.get(0).getYValue());

			filter.setIncome(FXCollections.observableArrayList("Medium"));
			cpaConstructor = new CPAGraphConstructor(filter);

			data = cpaConstructor.fetchGraph().getData();
			assertEquals(48f, data.get(0).getYValue());

			filter.setIncome(FXCollections.observableArrayList("High"));
			cpaConstructor = new CPAGraphConstructor(filter);

			data = cpaConstructor.fetchGraph().getData();
			assertEquals(48f, data.get(0).getYValue());
			
			//Test invalid income
			filter.setIncome(FXCollections.observableArrayList("Income"));
			cpaConstructor = new CPAGraphConstructor(filter);

			data = cpaConstructor.fetchGraph().getData();
			assertEquals(48f, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourContext() {
		try {
			filter.setContext(FXCollections.observableArrayList("News"));
			CPAGraphConstructor cpaConstructor = new CPAGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = cpaConstructor.fetchGraph().getData();
			assertEquals(48f, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Shopping"));
			cpaConstructor = new CPAGraphConstructor(filter);

			data = cpaConstructor.fetchGraph().getData();
			assertEquals(48f, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Social Media"));
			cpaConstructor = new CPAGraphConstructor(filter);

			data = cpaConstructor.fetchGraph().getData();
			assertEquals(48f, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Blog"));
			cpaConstructor = new CPAGraphConstructor(filter);

			data = cpaConstructor.fetchGraph().getData();
			assertEquals(48f, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Hobbies"));
			cpaConstructor = new CPAGraphConstructor(filter);

			data = cpaConstructor.fetchGraph().getData();
			assertEquals(48f, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Travel"));
			cpaConstructor = new CPAGraphConstructor(filter);

			data = cpaConstructor.fetchGraph().getData();
			assertEquals(48f, data.get(0).getYValue());
			
			//Test invalid context
			filter.setContext(FXCollections.observableArrayList("Invalid"));
			cpaConstructor = new CPAGraphConstructor(filter);

			data = cpaConstructor.fetchGraph().getData();
			assertEquals(48f, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}
}
