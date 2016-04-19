package dashboard.view;

import javafx.scene.Node;
import javafx.scene.control.Button;

public class FocusedButton extends Button {
	public FocusedButton() {
		super();
		bindFocusToDefault();
	}

	public FocusedButton(String text) {
		super(text);
		bindFocusToDefault();
	}

	public FocusedButton(String text, Node graphic) {
		super(text, graphic);
		bindFocusToDefault();
	}

	private void bindFocusToDefault() {
		defaultButtonProperty().bind(focusedProperty());
	}
}