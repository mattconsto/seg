package dashboard.model;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;

public final class ObservableMetrics {
    private final List<SimpleStringProperty> results;
    private final SimpleStringProperty description;
    private final SimpleBooleanProperty select;

    public SimpleBooleanProperty getSelect() {
        return select;
    }
    

    public ObservableMetrics(String desc) {	
        this.select = new SimpleBooleanProperty(false);
        this.results = new ArrayList();
        for (int i = 0; i < 10; i++)
            this.results.add(new SimpleStringProperty("0"));
        this.description = new SimpleStringProperty(desc);
    }
    public void setResults(int iCol, String res) {    
       //this.results.add(iCol, new SimpleStringProperty(res));
        this.results.set(iCol, new SimpleStringProperty(res));
    }
    public String getResults(int i) {
            return results.get(i).get();
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
    private List<SimpleStringProperty> SimpleStringProperty(String res) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
