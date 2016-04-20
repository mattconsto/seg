package dashboard.view;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class HoveredThresholdNode extends StackPane {
	
	final private LineChart chart;
	
	public HoveredThresholdNode(int priorValue, int value, LineChart myChart) {
		setPrefSize(10, 10);
		chart = myChart;
		
		final Label label = createDataThresholdLabel(priorValue, value);
		final Border border = getBorder();
		final Background background = getBackground();
		
		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if(!chart.getCreateSymbols())
				{
					setBackground(background);
					setBorder(border);
				}
				getChildren().setAll(label);
				setCursor(Cursor.HAND);
				toFront();
			}
		});
		setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if(!chart.getCreateSymbols())
				{
					setBackground(Background.EMPTY);
					setBorder(Border.EMPTY);
				}
				getChildren().clear();
				setCursor(Cursor.CROSSHAIR);
			}
		});
	}

	private Label createDataThresholdLabel(int priorValue, int value) {
		final Label label = new Label(value + "");
		label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
		label.setStyle("-fx-font-size: 16;");
		label.setTextFill(Color.BLACK);
		label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
		return label;
	}
}