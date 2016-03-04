package tests;

import static org.junit.Assert.*;

import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import junit.framework.TestCase;

import org.junit.Test;

import dashboard.controller.ClicksGraphConstructor;
import dashboard.model.DatabaseConnection;
import dashboard.model.Filter;

public class ClicksDataTest extends TestCase{
	Filter filter;
	
	@Override
	public void setUp()
	{
		DatabaseConnection.setDbfile("Test");
		filter = new Filter();
		filter.setGender(FXCollections.observableArrayList("Any"));
		filter.setAge(FXCollections.observableArrayList("Any"));
		filter.setIncome(FXCollections.observableArrayList("Any"));
		filter.setContext(FXCollections.observableArrayList("Any"));
	}
	
	@Test
	public void testFirstMinuteTotal() throws SQLException
	{
		ClicksGraphConstructor impressionsConstructor = new ClicksGraphConstructor(filter);
		
		ObservableList<XYChart.Data<String, Number>> data = impressionsConstructor.fetchGraph().getData();
		assertEquals(2, data.get(0).getYValue());
	}

	
	@Test
	public void testFirstHourTotal() throws SQLException
	{
		ClicksGraphConstructor impressionsConstructor = new ClicksGraphConstructor(filter);
		
		ObservableList<XYChart.Data<String, Number>> data = impressionsConstructor.fetchGraph().getData();
		assertEquals(94, data.get(0).getYValue());
	}

	@Test
	public void testFirstDayTotal() throws SQLException
	{
		ClicksGraphConstructor impressionsConstructor = new ClicksGraphConstructor(filter);
		
		ObservableList<XYChart.Data<String, Number>> data = impressionsConstructor.fetchGraph().getData();
		assertEquals(1079, data.get(0).getYValue());
	}
	
	@Test
	public void testFirstWeekTotal() throws SQLException
	{
		ClicksGraphConstructor impressionsConstructor = new ClicksGraphConstructor(filter);
		
		ObservableList<XYChart.Data<String, Number>> data = impressionsConstructor.fetchGraph().getData();
		assertEquals(11539, data.get(0).getYValue());
	}
	
	@Test
	public void testFirstHourGender() throws SQLException
	{
		filter.setGender(FXCollections.observableArrayList("Female"));
		ClicksGraphConstructor impressionsConstructor = new ClicksGraphConstructor(filter);
		
		ObservableList<XYChart.Data<String, Number>> data = impressionsConstructor.fetchGraph().getData();
		assertEquals(1433, data.get(0).getYValue());
		
		filter.setGender(FXCollections.observableArrayList("Male"));
		impressionsConstructor = new ClicksGraphConstructor(filter);
		
		data = impressionsConstructor.fetchGraph().getData();
		assertEquals(693, data.get(0).getYValue());
	}
	
	@Test
	public void testFirstHourAge() throws SQLException
	{
		filter.setAge(FXCollections.observableArrayList("Less than 25"));
		ClicksGraphConstructor impressionsConstructor = new ClicksGraphConstructor(filter);
		
		ObservableList<XYChart.Data<String, Number>> data = impressionsConstructor.fetchGraph().getData();
		assertEquals(415, data.get(0).getYValue());
		
		filter.setAge(FXCollections.observableArrayList("25 to 34"));
		impressionsConstructor = new ClicksGraphConstructor(filter);
		
		data = impressionsConstructor.fetchGraph().getData();
		assertEquals(551, data.get(0).getYValue());
		
		filter.setAge(FXCollections.observableArrayList("35 to 44"));
		impressionsConstructor = new ClicksGraphConstructor(filter);
		
		data = impressionsConstructor.fetchGraph().getData();
		assertEquals(520, data.get(0).getYValue());
		
		filter.setAge(FXCollections.observableArrayList("45 to 54"));
		impressionsConstructor = new ClicksGraphConstructor(filter);
		
		data = impressionsConstructor.fetchGraph().getData();
		assertEquals(369, data.get(0).getYValue());
		
		filter.setAge(FXCollections.observableArrayList("Greater than 55"));
		impressionsConstructor = new ClicksGraphConstructor(filter);
		
		data = impressionsConstructor.fetchGraph().getData();
		assertEquals(271, data.get(0).getYValue());
	}
	
	@Test
	public void testFirstHourIncome() throws SQLException
	{
		filter.setIncome(FXCollections.observableArrayList("Low"));
		ClicksGraphConstructor impressionsConstructor = new ClicksGraphConstructor(filter);
		
		ObservableList<XYChart.Data<String, Number>> data = impressionsConstructor.fetchGraph().getData();
		assertEquals(646, data.get(0).getYValue());
		
		filter.setIncome(FXCollections.observableArrayList("Medium"));
		impressionsConstructor = new ClicksGraphConstructor(filter);
		
		data = impressionsConstructor.fetchGraph().getData();
		assertEquals(1051, data.get(0).getYValue());
		
		filter.setIncome(FXCollections.observableArrayList("High"));
		impressionsConstructor = new ClicksGraphConstructor(filter);
		
		data = impressionsConstructor.fetchGraph().getData();
		assertEquals(429, data.get(0).getYValue());
	}
	
	@Test
	public void testFirstHourContext() throws SQLException
	{
		filter.setContext(FXCollections.observableArrayList("News"));
		ClicksGraphConstructor impressionsConstructor = new ClicksGraphConstructor(filter);
		
		ObservableList<XYChart.Data<String, Number>> data = impressionsConstructor.fetchGraph().getData();
		assertEquals(642, data.get(0).getYValue());
		
		filter.setContext(FXCollections.observableArrayList("Shopping"));
		impressionsConstructor = new ClicksGraphConstructor(filter);
		
		data = impressionsConstructor.fetchGraph().getData();
		assertEquals(575, data.get(0).getYValue());
		
		filter.setContext(FXCollections.observableArrayList("Social Media"));
		impressionsConstructor = new ClicksGraphConstructor(filter);
		
		data = impressionsConstructor.fetchGraph().getData();
		assertEquals(594, data.get(0).getYValue());
		
		filter.setContext(FXCollections.observableArrayList("Blog"));
		impressionsConstructor = new ClicksGraphConstructor(filter);
		
		data = impressionsConstructor.fetchGraph().getData();
		assertEquals(315, data.get(0).getYValue());
		
		filter.setContext(FXCollections.observableArrayList("Hobbies"));
		impressionsConstructor = new ClicksGraphConstructor(filter);
		
		data = impressionsConstructor.fetchGraph().getData();
		assertEquals(0, data.size());
		
		filter.setContext(FXCollections.observableArrayList("Travel"));
		impressionsConstructor = new ClicksGraphConstructor(filter);
		
		data = impressionsConstructor.fetchGraph().getData();
		assertEquals(0, data.size());
	}
}
