package dashboard.view;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class HoveredThresholdNode extends StackPane {
	public HoveredThresholdNode(int priorValue, int value) {
		setPrefSize(50, 50);

		final Label label = createDataThresholdLabel(priorValue, value);
		final Border border = getBorder();
		final Background background = getBackground();

		setBackground(Background.EMPTY);
		setBorder(Border.EMPTY);

		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				setBackground(background);
				setBorder(border);
				getChildren().setAll(label);
				setCursor(Cursor.HAND);
				toFront();
			}
		});
		setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				setBackground(Background.EMPTY);
				setBorder(Border.EMPTY);
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