package tests;

import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import junit.framework.TestCase;

import org.junit.Test;

import dashboard.controller.ConversionGraphConstructor;
import dashboard.model.DatabaseConnection;
import dashboard.model.Filter;

public class ConversionDataTest extends TestCase{
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
			ConversionGraphConstructor conversionConstructor = new ConversionGraphConstructor(filter);
			
			ObservableList<XYChart.Data<String, Number>> data = conversionConstructor.fetchGraph().getData();
			assertEquals(180, data.get(0).getYValue());
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
			ConversionGraphConstructor conversionConstructor = new ConversionGraphConstructor(filter);
			
			ObservableList<XYChart.Data<String, Number>> data = conversionConstructor.fetchGraph().getData();
			assertEquals(360, data.get(0).getYValue());
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
			ConversionGraphConstructor conversionConstructor = new ConversionGraphConstructor(filter);
			
			ObservableList<XYChart.Data<String, Number>> data = conversionConstructor.fetchGraph().getData();
			assertEquals(540, data.get(0).getYValue());
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
			ConversionGraphConstructor conversionConstructor = new ConversionGraphConstructor(filter);
			
			ObservableList<XYChart.Data<String, Number>> data = conversionConstructor.fetchGraph().getData();
			assertEquals(720, data.get(0).getYValue());
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
			ConversionGraphConstructor conversionConstructor = new ConversionGraphConstructor(filter);
			
			ObservableList<XYChart.Data<String, Number>> data = conversionConstructor.fetchGraph().getData();
			assertEquals(180, data.get(0).getYValue());
			
			filter.setGender(FXCollections.observableArrayList("Male"));
			conversionConstructor = new ConversionGraphConstructor(filter);
			
			data = conversionConstructor.fetchGraph().getData();
			assertEquals(180, data.get(0).getYValue());
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
			ConversionGraphConstructor conversionConstructor = new ConversionGraphConstructor(filter);
			
			ObservableList<XYChart.Data<String, Number>> data = conversionConstructor.fetchGraph().getData();
			assertEquals(72, data.get(0).getYValue());
			
			filter.setAge(FXCollections.observableArrayList("25 to 34"));
			conversionConstructor = new ConversionGraphConstructor(filter);
			
			data = conversionConstructor.fetchGraph().getData();
			assertEquals(72, data.get(0).getYValue());
			
			filter.setAge(FXCollections.observableArrayList("35 to 44"));
			conversionConstructor = new ConversionGraphConstructor(filter);
			
			data = conversionConstructor.fetchGraph().getData();
			assertEquals(72, data.get(0).getYValue());
			
			filter.setAge(FXCollections.observableArrayList("45 to 54"));
			conversionConstructor = new ConversionGraphConstructor(filter);
			
			data = conversionConstructor.fetchGraph().getData();
			assertEquals(72, data.get(0).getYValue());
			
			filter.setAge(FXCollections.observableArrayList("Greater than 55"));
			conversionConstructor = new ConversionGraphConstructor(filter);
			
			data = conversionConstructor.fetchGraph().getData();
			assertEquals(72, data.get(0).getYValue());
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
			ConversionGraphConstructor conversionConstructor = new ConversionGraphConstructor(filter);
			
			ObservableList<XYChart.Data<String, Number>> data = conversionConstructor.fetchGraph().getData();
			assertEquals(120, data.get(0).getYValue());
			
			filter.setIncome(FXCollections.observableArrayList("Medium"));
			conversionConstructor = new ConversionGraphConstructor(filter);
			
			data = conversionConstructor.fetchGraph().getData();
			assertEquals(120, data.get(0).getYValue());
			
			filter.setIncome(FXCollections.observableArrayList("High"));
			conversionConstructor = new ConversionGraphConstructor(filter);
			
			data = conversionConstructor.fetchGraph().getData();
			assertEquals(120, data.get(0).getYValue());
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
			ConversionGraphConstructor conversionConstructor = new ConversionGraphConstructor(filter);
			
			ObservableList<XYChart.Data<String, Number>> data = conversionConstructor.fetchGraph().getData();
			assertEquals(60, data.get(0).getYValue());
			
			filter.setContext(FXCollections.observableArrayList("Shopping"));
			conversionConstructor = new ConversionGraphConstructor(filter);
			
			data = conversionConstructor.fetchGraph().getData();
			assertEquals(60, data.get(0).getYValue());
			
			filter.setContext(FXCollections.observableArrayList("Social Media"));
			conversionConstructor = new ConversionGraphConstructor(filter);
			
			data = conversionConstructor.fetchGraph().getData();
			assertEquals(60, data.get(0).getYValue());
			
			filter.setContext(FXCollections.observableArrayList("Blog"));
			conversionConstructor = new ConversionGraphConstructor(filter);
			
			data = conversionConstructor.fetchGraph().getData();
			assertEquals(60, data.get(0).getYValue());
			
			filter.setContext(FXCollections.observableArrayList("Hobbies"));
			conversionConstructor = new ConversionGraphConstructor(filter);
			
			data = conversionConstructor.fetchGraph().getData();
			assertEquals(60, data.get(0).getYValue());
			
			filter.setContext(FXCollections.observableArrayList("Travel"));
			conversionConstructor = new ConversionGraphConstructor(filter);
			
			data = conversionConstructor.fetchGraph().getData();
			assertEquals(60, data.get(0).getYValue());
		}
		catch(SQLException sqle)
		{
			System.err.println(sqle.getMessage());
			fail("SQL error");
		}
	}
}
