package dashboard.controller;

import java.sql.Connection;
import java.sql.SQLException;

import javafx.scene.chart.XYChart;

import dashboard.model.DatabaseConnection;

public abstract class GraphConstructor {

	String filterGender;
	String filterAge;
	String filterIncome;
	String filterContext;
	String filterTime;

	public GraphConstructor(String gender, String age, String income, String context, String time) {
		switch(gender) {
		case "Male":
			break;
		case "Female":
			break;
		default:
			break;
		}
		switch(age) {
		case "Less than 25":
			break;
		case "25 to 34":
			break;
		case "35 to 44":
			break;
		case "45 to 54":
			break;
		case "Greater than 55":
			break;
		default:
			break;
		}
		switch(income) {
		case "Low":
			break;
		case "Medium":
			break;
		case "High":
			break;
		default:
			break;
		}
		switch(context) {
		case "News":
			this.filterContext = "CONTEXT=0";
			break;
		case "Shopping":
			this.filterContext = "CONTEXT=1";
			break;
		case "Social Media":
			this.filterContext = "CONTEXT=2";
			break;
		case "Blog":
			this.filterContext = "CONTEXT=3";
			break;
		case "Hobbies":
			this.filterContext = "CONTEXT=4";
			break;
		case "Travel":
			this.filterContext = "CONTEXT=5";
			break;
		default:
			this.filterContext = "'1'='1'";
			break;
		}
	}

	public XYChart.Series<String, Number> fetchGraph() throws SQLException {
		return generateGraph(DatabaseConnection.getConnection());
	}

	protected abstract XYChart.Series<String, Number> generateGraph(Connection conn) throws SQLException;
}
