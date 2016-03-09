package dashboard.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public final class ObservableMetrics {

	private final SimpleStringProperty result;
	private final SimpleStringProperty description;

	public ObservableMetrics(String desc, String res) {
		this.result = new SimpleStringProperty(res);
		this.description = new SimpleStringProperty(desc);
	}

	public String getResult() {
		return result.get();
	}

	public ObservableValue<String> resultProperty() {
		return result;
	}

	public String getDescription() {
		return description.get();
	}

	public ObservableValue<String> descriptionProperty() {
		return description;
	}
}
