package tests;

import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import junit.framework.TestCase;

import org.junit.Test;

import dashboard.controller.BounceRateGraphConstructor;
import dashboard.model.DatabaseConnection;
import dashboard.model.Filter;

public class BounceRateDataTest extends TestCase{
Filter filter;
	
	@Override
	public void setUp()
	{
		DatabaseConnection.setDbfile("TestData.db");
		filter = new Filter();
	}
	
	@Test
	public void testFirstMinuteTotal()
	{
		try
		{
			BounceRateGraphConstructor bounceConstructor = new BounceRateGraphConstructor(filter);
			
			ObservableList<XYChart.Data<String, Number>> data = bounceConstructor.fetchGraph().getData();
			assertEquals(0.5, data.get(0).getYValue());
		}
		catch(SQLException sqle)
		{
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	
	@Test
	public void testFirstHourTotal()
	{
		try
		{
			BounceRateGraphConstructor bounceConstructor = new BounceRateGraphConstructor(filter);
			
			ObservableList<XYChart.Data<String, Number>> data = bounceConstructor.fetchGraph().getData();
			assertEquals(0.5, data.get(0).getYValue());
		}
		catch(SQLException sqle)
		{
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}

	@Test
	public void testFirstDayTotal()
	{
		try
		{
			BounceRateGraphConstructor bounceConstructor = new BounceRateGraphConstructor(filter);
			
			ObservableList<XYChart.Data<String, Number>> data = bounceConstructor.fetchGraph().getData();
			assertEquals(0.5, data.get(0).getYValue());
		}
		catch(SQLException sqle)
		{
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}
	
	@Test
	public void testFirstWeekTotal()
	{
		try
		{
			BounceRateGraphConstructor bounceConstructor = new BounceRateGraphConstructor(filter);
			
			ObservableList<XYChart.Data<String, Number>> data = bounceConstructor.fetchGraph().getData();
			assertEquals(0.5, data.get(0).getYValue());
		}
		catch(SQLException sqle)
		{
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}
	
	@Test
	public void testFirstHourGender()
	{
		try
		{
			filter.setGender(FXCollections.observableArrayList("Female"));
			BounceRateGraphConstructor bounceConstructor = new BounceRateGraphConstructor(filter);
			
			ObservableList<XYChart.Data<String, Number>> data = bounceConstructor.fetchGraph().getData();
			assertEquals(0.5, data.get(0).getYValue());
			
			filter.setGender(FXCollections.observableArrayList("Male"));
			bounceConstructor = new BounceRateGraphConstructor(filter);
			
			data = bounceConstructor.fetchGraph().getData();
			assertEquals(0.5, data.get(0).getYValue());
		}
		catch(SQLException sqle)
		{
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}
	
	@Test
	public void testFirstHourAge()
	{
		try
		{
			filter.setAge(FXCollections.observableArrayList("Less than 25"));
			BounceRateGraphConstructor bounceConstructor = new BounceRateGraphConstructor(filter);
			
			ObservableList<XYChart.Data<String, Number>> data = bounceConstructor.fetchGraph().getData();
			assertEquals(0.5, data.get(0).getYValue());
			
			filter.setAge(FXCollections.observableArrayList("25 to 34"));
			bounceConstructor = new BounceRateGraphConstructor(filter);
			
			data = bounceConstructor.fetchGraph().getData();
			assertEquals(0.5, data.get(0).getYValue());
			
			filter.setAge(FXCollections.observableArrayList("35 to 44"));
			bounceConstructor = new BounceRateGraphConstructor(filter);
			
			data = bounceConstructor.fetchGraph().getData();
			assertEquals(0.5, data.get(0).getYValue());
			
			filter.setAge(FXCollections.observableArrayList("45 to 54"));
			bounceConstructor = new BounceRateGraphConstructor(filter);
			
			data = bounceConstructor.fetchGraph().getData();
			assertEquals(0.5, data.get(0).getYValue());
			
			filter.setAge(FXCollections.observableArrayList("Greater than 55"));
			bounceConstructor = new BounceRateGraphConstructor(filter);
			
			data = bounceConstructor.fetchGraph().getData();
			assertEquals(0.5, data.get(0).getYValue());
		}
		catch(SQLException sqle)
		{
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}
	
	@Test
	public void testFirstHourIncome()
	{
		try
		{
			filter.setIncome(FXCollections.observableArrayList("Low"));
			BounceRateGraphConstructor bounceConstructor = new BounceRateGraphConstructor(filter);
			
			ObservableList<XYChart.Data<String, Number>> data = bounceConstructor.fetchGraph().getData();
			assertEquals(0.5, data.get(0).getYValue());
			
			filter.setIncome(FXCollections.observableArrayList("Medium"));
			bounceConstructor = new BounceRateGraphConstructor(filter);
			
			data = bounceConstructor.fetchGraph().getData();
			assertEquals(0.5, data.get(0).getYValue());
			
			filter.setIncome(FXCollections.observableArrayList("High"));
			bounceConstructor = new BounceRateGraphConstructor(filter);
			
			data = bounceConstructor.fetchGraph().getData();
			assertEquals(0.5, data.get(0).getYValue());
		}
		catch(SQLException sqle)
		{
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}
	
	@Test
	public void testFirstHourContext()
	{
		try
		{
			filter.setContext(FXCollections.observableArrayList("News"));
			BounceRateGraphConstructor bounceConstructor = new BounceRateGraphConstructor(filter);
			
			ObservableList<XYChart.Data<String, Number>> data = bounceConstructor.fetchGraph().getData();
			assertEquals(0.5, data.get(0).getYValue());
			
			filter.setContext(FXCollections.observableArrayList("Shopping"));
			bounceConstructor = new BounceRateGraphConstructor(filter);
			
			data = bounceConstructor.fetchGraph().getData();
			assertEquals(0.5, data.get(0).getYValue());
			
			filter.setContext(FXCollections.observableArrayList("Social Media"));
			bounceConstructor = new BounceRateGraphConstructor(filter);
			
			data = bounceConstructor.fetchGraph().getData();
			assertEquals(0.5, data.get(0).getYValue());
			
			filter.setContext(FXCollections.observableArrayList("Blog"));
			bounceConstructor = new BounceRateGraphConstructor(filter);
			
			data = bounceConstructor.fetchGraph().getData();
			assertEquals(0.5, data.get(0).getYValue());
			
			filter.setContext(FXCollections.observableArrayList("Hobbies"));
			bounceConstructor = new BounceRateGraphConstructor(filter);
			
			data = bounceConstructor.fetchGraph().getData();
			assertEquals(0.5, data.get(0).getYValue());
			
			filter.setContext(FXCollections.observableArrayList("Travel"));
			bounceConstructor = new BounceRateGraphConstructor(filter);
			
			data = bounceConstructor.fetchGraph().getData();
			assertEquals(0.5, data.get(0).getYValue());
		}
		catch(SQLException sqle)
		{
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}
}
