package tests;

import java.sql.SQLException;
import java.util.Date;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import junit.framework.TestCase;

import org.junit.Test;

import dashboard.controller.UniqueImpressionsGraphConstructor;
import dashboard.model.DatabaseConnection;
import dashboard.model.Filter;

public class UniqueImpressionsDataTest extends TestCase {
	Filter filter;

	@Override
	public void setUp() {
		DatabaseConnection.setDbfile("TestData.db");
		filter = new Filter();
	}

	@Test
	public void testFirstMinuteTotal() {
		try {
			UniqueImpressionsGraphConstructor impressionsConstructor = new UniqueImpressionsGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = impressionsConstructor.fetchGraph().getData();
			assertEquals(180, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourTotal() {
		try {
			UniqueImpressionsGraphConstructor impressionsConstructor = new UniqueImpressionsGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = impressionsConstructor.fetchGraph().getData();
			assertEquals(180, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstDayTotal() {
		try {
			UniqueImpressionsGraphConstructor impressionsConstructor = new UniqueImpressionsGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = impressionsConstructor.fetchGraph().getData();
			assertEquals(180, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstWeekTotal() {
		try {
			UniqueImpressionsGraphConstructor impressionsConstructor = new UniqueImpressionsGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = impressionsConstructor.fetchGraph().getData();
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
			UniqueImpressionsGraphConstructor impressionsConstructor = new UniqueImpressionsGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = impressionsConstructor.fetchGraph().getData();
			assertEquals(90, data.get(0).getYValue());

			filter.setGender(FXCollections.observableArrayList("Male"));
			impressionsConstructor = new UniqueImpressionsGraphConstructor(filter);

			data = impressionsConstructor.fetchGraph().getData();
			assertEquals(90, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourAge() {
		try {
			filter.setAge(FXCollections.observableArrayList("Less than 25"));
			UniqueImpressionsGraphConstructor impressionsConstructor = new UniqueImpressionsGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = impressionsConstructor.fetchGraph().getData();
			assertEquals(36, data.get(0).getYValue());

			filter.setAge(FXCollections.observableArrayList("25 to 34"));
			impressionsConstructor = new UniqueImpressionsGraphConstructor(filter);

			data = impressionsConstructor.fetchGraph().getData();
			assertEquals(36, data.get(0).getYValue());

			filter.setAge(FXCollections.observableArrayList("35 to 44"));
			impressionsConstructor = new UniqueImpressionsGraphConstructor(filter);

			data = impressionsConstructor.fetchGraph().getData();
			assertEquals(36, data.get(0).getYValue());

			filter.setAge(FXCollections.observableArrayList("45 to 54"));
			impressionsConstructor = new UniqueImpressionsGraphConstructor(filter);

			data = impressionsConstructor.fetchGraph().getData();
			assertEquals(36, data.get(0).getYValue());

			filter.setAge(FXCollections.observableArrayList("Greater than 55"));
			impressionsConstructor = new UniqueImpressionsGraphConstructor(filter);

			data = impressionsConstructor.fetchGraph().getData();
			assertEquals(36, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourIncome() {
		try {
			filter.setIncome(FXCollections.observableArrayList("Low"));
			UniqueImpressionsGraphConstructor impressionsConstructor = new UniqueImpressionsGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = impressionsConstructor.fetchGraph().getData();
			assertEquals(60, data.get(0).getYValue());

			filter.setIncome(FXCollections.observableArrayList("Medium"));
			impressionsConstructor = new UniqueImpressionsGraphConstructor(filter);

			data = impressionsConstructor.fetchGraph().getData();
			assertEquals(60, data.get(0).getYValue());

			filter.setIncome(FXCollections.observableArrayList("High"));
			impressionsConstructor = new UniqueImpressionsGraphConstructor(filter);

			data = impressionsConstructor.fetchGraph().getData();
			assertEquals(60, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstHourContext() {
		try {
			filter.setContext(FXCollections.observableArrayList("News"));
			UniqueImpressionsGraphConstructor impressionsConstructor = new UniqueImpressionsGraphConstructor(filter);

			ObservableList<XYChart.Data<Date, Number>> data = impressionsConstructor.fetchGraph().getData();
			assertEquals(30, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Shopping"));
			impressionsConstructor = new UniqueImpressionsGraphConstructor(filter);

			data = impressionsConstructor.fetchGraph().getData();
			assertEquals(30, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Social Media"));
			impressionsConstructor = new UniqueImpressionsGraphConstructor(filter);

			data = impressionsConstructor.fetchGraph().getData();
			assertEquals(30, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Blog"));
			impressionsConstructor = new UniqueImpressionsGraphConstructor(filter);

			data = impressionsConstructor.fetchGraph().getData();
			assertEquals(30, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Hobbies"));
			impressionsConstructor = new UniqueImpressionsGraphConstructor(filter);

			data = impressionsConstructor.fetchGraph().getData();
			assertEquals(30, data.get(0).getYValue());

			filter.setContext(FXCollections.observableArrayList("Travel"));
			impressionsConstructor = new UniqueImpressionsGraphConstructor(filter);

			data = impressionsConstructor.fetchGraph().getData();
			assertEquals(30, data.get(0).getYValue());
		} catch (SQLException sqle) {
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}
}
