package dashboard.view;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class HoveredThresholdNode extends StackPane {

	public HoveredThresholdNode(int priorValue, int value, int series) {
		setPrefSize(15, 15);

		final Label label = createDataThresholdLabel(priorValue, value, series);

		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				setOpacity(1);
				getChildren().setAll(label);
				setCursor(Cursor.HAND);
				toFront();
			}
		});
		setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				getChildren().clear();
				setCursor(Cursor.CROSSHAIR);
			}
		});
	}

	private Label createDataThresholdLabel(int priorValue, int value, int series) {
		final Label label = new Label(value + "");
		label.getStyleClass().addAll("default-color" + series, "chart-line-symbol", "chart-series-line");
		label.setStyle("-fx-font-size: 16;");
		label.setTextFill(Color.BLACK);
		label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
		return label;
	}
}