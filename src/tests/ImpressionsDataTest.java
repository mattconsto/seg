package tests;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.SQLException;

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
public class ImpressionsDataTest extends TestCase{
	Filter filter;
	
	@Override
	public void setUp()
	{
		DatabaseConnection.setDbfile("TestData.db");
		filter = new Filter();
		filter.setGender(FXCollections.observableArrayList("Any"));
		filter.setAge(FXCollections.observableArrayList("Any"));
		filter.setIncome(FXCollections.observableArrayList("Any"));
		filter.setContext(FXCollections.observableArrayList("Any"));
	}
	
	@Test
	public void testFirstMinuteTotal() throws SQLException
	{
		ImpressionsGraphConstructor impressionsConstructor = new ImpressionsGraphConstructor(filter);
		
		ObservableList<XYChart.Data<String, Number>> data = impressionsConstructor.fetchGraph().getData();
		assertEquals(1440, data.get(0).getYValue());
	}

	
	@Test
	public void testFirstHourTotal() throws SQLException
	{
		ImpressionsGraphConstructor impressionsConstructor = new ImpressionsGraphConstructor(filter);
		
		ObservableList<XYChart.Data<String, Number>> data = impressionsConstructor.fetchGraph().getData();
		assertEquals(2880, data.get(0).getYValue());
	}

	@Test
	public void testFirstDayTotal() throws SQLException
	{
		ImpressionsGraphConstructor impressionsConstructor = new ImpressionsGraphConstructor(filter);
		
		ObservableList<XYChart.Data<String, Number>> data = impressionsConstructor.fetchGraph().getData();
		assertEquals(4320, data.get(0).getYValue());
	}
	
	@Test
	public void testFirstWeekTotal() throws SQLException
	{
		ImpressionsGraphConstructor impressionsConstructor = new ImpressionsGraphConstructor(filter);
		
		ObservableList<XYChart.Data<String, Number>> data = impressionsConstructor.fetchGraph().getData();
		assertEquals(5760, data.get(0).getYValue());
	}
	
	@Test
	public void testFirstHourGender() throws SQLException
	{
		filter.setGender(FXCollections.observableArrayList("Female"));
		ImpressionsGraphConstructor impressionsConstructor = new ImpressionsGraphConstructor(filter);
		
		ObservableList<XYChart.Data<String, Number>> data = impressionsConstructor.fetchGraph().getData();
		assertEquals(1440, data.get(0).getYValue());
		
		filter.setGender(FXCollections.observableArrayList("Male"));
		impressionsConstructor = new ImpressionsGraphConstructor(filter);
		
		data = impressionsConstructor.fetchGraph().getData();
		assertEquals(1440, data.get(0).getYValue());
	}
	
	@Test
	public void testFirstHourAge() throws SQLException
	{
		filter.setAge(FXCollections.observableArrayList("Less than 25"));
		ImpressionsGraphConstructor impressionsConstructor = new ImpressionsGraphConstructor(filter);
		
		ObservableList<XYChart.Data<String, Number>> data = impressionsConstructor.fetchGraph().getData();
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
	}
	
	@Test
	public void testFirstHourIncome() throws SQLException
	{
		filter.setIncome(FXCollections.observableArrayList("Low"));
		ImpressionsGraphConstructor impressionsConstructor = new ImpressionsGraphConstructor(filter);
		
		ObservableList<XYChart.Data<String, Number>> data = impressionsConstructor.fetchGraph().getData();
		assertEquals(960, data.get(0).getYValue());
		
		filter.setIncome(FXCollections.observableArrayList("Medium"));
		impressionsConstructor = new ImpressionsGraphConstructor(filter);
		
		data = impressionsConstructor.fetchGraph().getData();
		assertEquals(960, data.get(0).getYValue());
		
		filter.setIncome(FXCollections.observableArrayList("High"));
		impressionsConstructor = new ImpressionsGraphConstructor(filter);
		
		data = impressionsConstructor.fetchGraph().getData();
		assertEquals(960, data.get(0).getYValue());
	}
	
	@Test
	public void testFirstHourContext() throws SQLException
	{
		filter.setContext(FXCollections.observableArrayList("News"));
		ImpressionsGraphConstructor impressionsConstructor = new ImpressionsGraphConstructor(filter);
		
		ObservableList<XYChart.Data<String, Number>> data = impressionsConstructor.fetchGraph().getData();
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
		assertEquals(480, data.size());
		
		filter.setContext(FXCollections.observableArrayList("Travel"));
		impressionsConstructor = new ImpressionsGraphConstructor(filter);
		
		data = impressionsConstructor.fetchGraph().getData();
		assertEquals(480, data.size());
	}
}
