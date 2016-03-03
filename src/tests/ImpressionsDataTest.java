package tests;
import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
	private static Connection connection = null;
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
		ImpressionsGraphConstructor impressionsConstructor = new ImpressionsGraphConstructor(filter);
		
		ObservableList<XYChart.Data<String, Number>> data = impressionsConstructor.fetchGraph().getData();
		assertEquals(39, data.get(0).getYValue());
	}

	
	@Test
	public void testFirstHourTotal() throws SQLException
	{
		ImpressionsGraphConstructor impressionsConstructor = new ImpressionsGraphConstructor(filter);
		
		ObservableList<XYChart.Data<String, Number>> data = impressionsConstructor.fetchGraph().getData();
		assertEquals(2126, data.get(0).getYValue());
	}

	@Test
	public void testFirstDayTotal() throws SQLException
	{
		ImpressionsGraphConstructor impressionsConstructor = new ImpressionsGraphConstructor(filter);
		
		ObservableList<XYChart.Data<String, Number>> data = impressionsConstructor.fetchGraph().getData();
		assertEquals(22049, data.get(0).getYValue());
	}
	
	@Test
	public void testFirstWeekTotal() throws SQLException
	{
		ImpressionsGraphConstructor impressionsConstructor = new ImpressionsGraphConstructor(filter);
		
		ObservableList<XYChart.Data<String, Number>> data = impressionsConstructor.fetchGraph().getData();
		assertEquals(233947, data.get(0).getYValue());
	}
	
	@Test
	public void testFirstHourGender() throws SQLException
	{
		filter.setGender(FXCollections.observableArrayList("Female"));
		ImpressionsGraphConstructor impressionsConstructor = new ImpressionsGraphConstructor(filter);
		
		ObservableList<XYChart.Data<String, Number>> data = impressionsConstructor.fetchGraph().getData();
		assertEquals(1433, data.get(0).getYValue());
		
		filter.setGender(FXCollections.observableArrayList("Male"));
		impressionsConstructor = new ImpressionsGraphConstructor(filter);
		
		data = impressionsConstructor.fetchGraph().getData();
		assertEquals(693, data.get(0).getYValue());
	}
	
	@Test
	public void testFirstHourAge() throws SQLException
	{
		filter.setAge(FXCollections.observableArrayList("Less than 25"));
		ImpressionsGraphConstructor impressionsConstructor = new ImpressionsGraphConstructor(filter);
		
		ObservableList<XYChart.Data<String, Number>> data = impressionsConstructor.fetchGraph().getData();
		assertEquals(415, data.get(0).getYValue());
		
		filter.setAge(FXCollections.observableArrayList("25 to 34"));
		impressionsConstructor = new ImpressionsGraphConstructor(filter);
		
		data = impressionsConstructor.fetchGraph().getData();
		assertEquals(551, data.get(0).getYValue());
		
		filter.setAge(FXCollections.observableArrayList("35 to 44"));
		impressionsConstructor = new ImpressionsGraphConstructor(filter);
		
		data = impressionsConstructor.fetchGraph().getData();
		assertEquals(520, data.get(0).getYValue());
		
		filter.setAge(FXCollections.observableArrayList("45 to 54"));
		impressionsConstructor = new ImpressionsGraphConstructor(filter);
		
		data = impressionsConstructor.fetchGraph().getData();
		assertEquals(369, data.get(0).getYValue());
		
		filter.setAge(FXCollections.observableArrayList("Greater than 55"));
		impressionsConstructor = new ImpressionsGraphConstructor(filter);
		
		data = impressionsConstructor.fetchGraph().getData();
		assertEquals(271, data.get(0).getYValue());
	}
	
	@Test
	public void testFirstHourIncome() throws SQLException
	{
		filter.setIncome(FXCollections.observableArrayList("Low"));
		ImpressionsGraphConstructor impressionsConstructor = new ImpressionsGraphConstructor(filter);
		
		ObservableList<XYChart.Data<String, Number>> data = impressionsConstructor.fetchGraph().getData();
		assertEquals(646, data.get(0).getYValue());
		
		filter.setIncome(FXCollections.observableArrayList("Medium"));
		impressionsConstructor = new ImpressionsGraphConstructor(filter);
		
		data = impressionsConstructor.fetchGraph().getData();
		assertEquals(1051, data.get(0).getYValue());
		
		filter.setIncome(FXCollections.observableArrayList("High"));
		impressionsConstructor = new ImpressionsGraphConstructor(filter);
		
		data = impressionsConstructor.fetchGraph().getData();
		assertEquals(429, data.get(0).getYValue());
	}
	
	@Test
	public void testFirstHourContext() throws SQLException
	{
		
	}
}
