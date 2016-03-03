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
		System.out.println("Sup");
		DatabaseConnection.setDbfile("Test");
		filter = new Filter();
		filter.setGender(FXCollections.observableArrayList("Any"));
		filter.setAge(FXCollections.observableArrayList("Any"));
		filter.setIncome(FXCollections.observableArrayList("Any"));
		filter.setContext(FXCollections.observableArrayList("Any"));
	}
	
	@Test
	public void testFirstHourTotal() throws SQLException
	{
		ImpressionsGraphConstructor impressionsConstructor = new ImpressionsGraphConstructor(filter);
		
		ObservableList<XYChart.Data<String, Number>> data = impressionsConstructor.fetchGraph().getData();
		assertEquals(data.get(0).getYValue(),2126);
	}

}
