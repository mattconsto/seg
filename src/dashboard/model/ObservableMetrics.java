package dashboard.model;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public final class ObservableMetrics implements Iterable<SimpleStringProperty> {
	private final List<SimpleStringProperty> results;
	private final SimpleStringProperty description;
	private final SimpleBooleanProperty select;

	public SimpleBooleanProperty getSelect() {
		return select;
	}
	
	public ObservableMetrics(String desc) {	
		this.select = new SimpleBooleanProperty(false);
		this.results = new ArrayList<SimpleStringProperty>();
		for (int i = 0; i < 10; i++)
			this.results.add(new SimpleStringProperty("0"));
		this.description = new SimpleStringProperty(desc);
	}
	public void setResults(int iCol, String res) {
		this.results.set(iCol, new SimpleStringProperty(res));
	}
	public String getResults(int i) {
			return results.get(i).get();
	}
	public Iterator<SimpleStringProperty> iterator() {
		return results.iterator();
	}
	public ObservableValue<String> resultsProperty(int i) {
			return results.get(i);
	}
	public String getDescription() {
		  return description.get();
	}
	public ObservableValue<String> descriptionProperty() {
			return description;
	}
  
	public ObservableValue<Boolean> selectProperty() {
			return select;
	}
}
