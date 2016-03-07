package tests;

import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import junit.framework.TestCase;

import org.junit.Test;

import dashboard.controller.BounceGraphConstructor;
import dashboard.model.DatabaseConnection;
import dashboard.model.Filter;

public class BounceDataTest extends TestCase{
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
			BounceGraphConstructor bounceConstructor = new BounceGraphConstructor(filter);
			
			ObservableList<XYChart.Data<String, Number>> data = bounceConstructor.fetchGraph().getData();
			assertEquals(360, data.get(0).getYValue());
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
			BounceGraphConstructor bounceConstructor = new BounceGraphConstructor(filter);
			
			ObservableList<XYChart.Data<String, Number>> data = bounceConstructor.fetchGraph().getData();
			assertEquals(720, data.get(0).getYValue());
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
			BounceGraphConstructor bounceConstructor = new BounceGraphConstructor(filter);
			
			ObservableList<XYChart.Data<String, Number>> data = bounceConstructor.fetchGraph().getData();
			assertEquals(1080, data.get(0).getYValue());
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
			BounceGraphConstructor bounceConstructor = new BounceGraphConstructor(filter);
			
			ObservableList<XYChart.Data<String, Number>> data = bounceConstructor.fetchGraph().getData();
			assertEquals(1440, data.get(0).getYValue());
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
			BounceGraphConstructor bounceConstructor = new BounceGraphConstructor(filter);
			
			ObservableList<XYChart.Data<String, Number>> data = bounceConstructor.fetchGraph().getData();
			assertEquals(360, data.get(0).getYValue());
			
			filter.setGender(FXCollections.observableArrayList("Male"));
			bounceConstructor = new BounceGraphConstructor(filter);
			
			data = bounceConstructor.fetchGraph().getData();
			assertEquals(360, data.get(0).getYValue());
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
			BounceGraphConstructor bounceConstructor = new BounceGraphConstructor(filter);
			
			ObservableList<XYChart.Data<String, Number>> data = bounceConstructor.fetchGraph().getData();
			assertEquals(144, data.get(0).getYValue());
			
			filter.setAge(FXCollections.observableArrayList("25 to 34"));
			bounceConstructor = new BounceGraphConstructor(filter);
			
			data = bounceConstructor.fetchGraph().getData();
			assertEquals(144, data.get(0).getYValue());
			
			filter.setAge(FXCollections.observableArrayList("35 to 44"));
			bounceConstructor = new BounceGraphConstructor(filter);
			
			data = bounceConstructor.fetchGraph().getData();
			assertEquals(144, data.get(0).getYValue());
			
			filter.setAge(FXCollections.observableArrayList("45 to 54"));
			bounceConstructor = new BounceGraphConstructor(filter);
			
			data = bounceConstructor.fetchGraph().getData();
			assertEquals(144, data.get(0).getYValue());
			
			filter.setAge(FXCollections.observableArrayList("Greater than 55"));
			bounceConstructor = new BounceGraphConstructor(filter);
			
			data = bounceConstructor.fetchGraph().getData();
			assertEquals(144, data.get(0).getYValue());
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
			BounceGraphConstructor bounceConstructor = new BounceGraphConstructor(filter);
			
			ObservableList<XYChart.Data<String, Number>> data = bounceConstructor.fetchGraph().getData();
			assertEquals(240, data.get(0).getYValue());
			
			filter.setIncome(FXCollections.observableArrayList("Medium"));
			bounceConstructor = new BounceGraphConstructor(filter);
			
			data = bounceConstructor.fetchGraph().getData();
			assertEquals(240, data.get(0).getYValue());
			
			filter.setIncome(FXCollections.observableArrayList("High"));
			bounceConstructor = new BounceGraphConstructor(filter);
			
			data = bounceConstructor.fetchGraph().getData();
			assertEquals(240, data.get(0).getYValue());
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
			BounceGraphConstructor bounceConstructor = new BounceGraphConstructor(filter);
			
			ObservableList<XYChart.Data<String, Number>> data = bounceConstructor.fetchGraph().getData();
			assertEquals(120, data.get(0).getYValue());
			
			filter.setContext(FXCollections.observableArrayList("Shopping"));
			bounceConstructor = new BounceGraphConstructor(filter);
			
			data = bounceConstructor.fetchGraph().getData();
			assertEquals(120, data.get(0).getYValue());
			
			filter.setContext(FXCollections.observableArrayList("Social Media"));
			bounceConstructor = new BounceGraphConstructor(filter);
			
			data = bounceConstructor.fetchGraph().getData();
			assertEquals(120, data.get(0).getYValue());
			
			filter.setContext(FXCollections.observableArrayList("Blog"));
			bounceConstructor = new BounceGraphConstructor(filter);
			
			data = bounceConstructor.fetchGraph().getData();
			assertEquals(120, data.get(0).getYValue());
			
			filter.setContext(FXCollections.observableArrayList("Hobbies"));
			bounceConstructor = new BounceGraphConstructor(filter);
			
			data = bounceConstructor.fetchGraph().getData();
			assertEquals(120, data.get(0).getYValue());
			
			filter.setContext(FXCollections.observableArrayList("Travel"));
			bounceConstructor = new BounceGraphConstructor(filter);
			
			data = bounceConstructor.fetchGraph().getData();
			assertEquals(120, data.get(0).getYValue());
		}
		catch(SQLException sqle)
		{
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}
}