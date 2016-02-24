package dashboard.controller;

import java.sql.Connection;
import java.sql.SQLException;

import javafx.scene.chart.XYChart;

import dashboard.model.DatabaseConnection;

public abstract class GraphConstructor {
	public XYChart.Series<String, Number> fetchGraph() throws SQLException {
		return generateGraph(DatabaseConnection.getConnection());
	}

	protected abstract XYChart.Series<String, Number> generateGraph(Connection conn) throws SQLException;
}
