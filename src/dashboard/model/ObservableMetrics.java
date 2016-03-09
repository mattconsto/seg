
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

	@SuppressWarnings("unused")
	private void setResult(String title) {
		this.result.set(title);
	}

	public ObservableValue<String> resultProperty() {
		return result;
	}

	public String getDescription() {
		return description.get();
	}

	@SuppressWarnings("unused")
	private void setDescription(String description) {
		this.description.set(description);
	}

	public ObservableValue<String> descriptionProperty() {
		return description;
	}
}
