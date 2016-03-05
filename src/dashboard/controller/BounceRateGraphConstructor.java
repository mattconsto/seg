package dashboard.controller;

import java.sql.Connection;
import java.sql.SQLException;

import javafx.scene.chart.XYChart.Series;
import dashboard.model.Filter;

public class BounceRateGraphConstructor extends GraphConstructor{

	public BounceRateGraphConstructor(Filter filter) {
		super(filter);
	}

	@Override
	protected Series<String, Number> generateGraph(Connection conn)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

}
