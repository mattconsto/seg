/*
 
 */
package dashboard.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.IndexedCheckModel;

 

public class Filter {

	 boolean genderEnabled = true;
        boolean ageEnabled = true;
        List <String> gender ;
        List <String> age ;
	ObservableList <String> income; 
	ObservableList <String> context;
	LocalDate dateTo;
    LocalDate dateFrom;
    String contextSQL;
    String ageSQL;
    String incomeSQL;
    String genderSQL;
    String dateSQL;


    public String getDateSQL() {
        return dateSQL;
    }

    public void setDateSQL(String dateSQL) {
        this.dateSQL = dateSQL;
    }

   

    public List<String> getGender() {
        return gender;
    }

     /**
     *
     * @param gender
     */
    public void setGender(ObservableList<String> gender) {
        
        this.gender.clear();
        for (String s : gender)
            this.gender.add (s);
        setGenderSQL();
    }
     private void setAnyGender(CheckComboBox<String> c )
     {
           c.getItems().clear();
           c.getItems().addAll("Any","Female","Male");
           c.getCheckModel().clearChecks(); 
           c.getCheckModel().check(0); 
           c.getCheckModel().check("Any");
     }
     public void setGender(CheckComboBox<String> c) {
        if (!genderEnabled)
            return;
        genderEnabled = false;
        IndexedCheckModel<String> genderModel = c.getCheckModel();
        if (genderModel.getCheckedIndices().isEmpty()) {
            genderModel.check(0); 
        }
        else if (genderModel.isChecked(0)) {
            if (gender.contains("Any")) { // already checked 
                if (genderModel.getCheckedIndices().size() == genderModel.getItemCount()) // they are all ticked so 
                    setAnyGender(c);
                else if (genderModel.getCheckedIndices().size() > 1)
                    genderModel.clearCheck(0);
            }
            else 
                setAnyGender(c);
        }
        else if (genderModel.getCheckedIndices().size() == genderModel.getItemCount()-1) { // they are all ticked so 
            setAnyGender(c);
        }
        setGender(genderModel.getCheckedItems());
        genderEnabled = true;
    }
   /**
     *
     * @param age
     * 
     */
    public void setAge(ObservableList<String> age) {
       
        this.age.clear();
        for (String s : age)
            this.age.add (s);
        setAgeSQL();
    }
     private void setAnyAge(CheckComboBox<String> c )
     {
           c.getItems().clear();
           c.getItems().addAll("Any","Less than 25","25 to 34","35 to 44","45 to 54","Greater than 55");
           c.getCheckModel().clearChecks(); 
           c.getCheckModel().check(0); 
           c.getCheckModel().check("Any");
     }
     public void setAge(CheckComboBox<String> c) {
        if (!ageEnabled)
            return;
        ageEnabled = false;
        IndexedCheckModel<String> genderModel = c.getCheckModel();
        if (genderModel.getCheckedIndices().isEmpty()) {
            genderModel.check(0); 
        }
        else if (genderModel.isChecked(0)) {
            if (age.contains("Any")) { // already checked 
                if (genderModel.getCheckedIndices().size() == genderModel.getItemCount()) // they are all ticked so 
                    setAnyAge(c);
                else if (genderModel.getCheckedIndices().size() > 1)
                    genderModel.clearCheck(0);
            }
            else 
                setAnyAge(c);
        }
        else if (genderModel.getCheckedIndices().size() == genderModel.getItemCount()-1) { // they are all ticked so 
            setAnyAge(c);
        }
        setAge(genderModel.getCheckedItems());
        ageEnabled = true;
    }
    public List<String> getAge() {
        return age;
    }

    

    public ObservableList<String> getIncome() {
        return income;
    }

    public void setIncome(ObservableList<String> income) {
        this.income = income;
        setIncomeSQL();
    }

    public ObservableList<String> getContext() {
        return context;
    }
    
    private void setGenderSQL() {
        genderSQL = "";
        if (gender.isEmpty() || gender.contains("Any") || (gender.contains("Female") && gender.contains("Male"))){
            genderSQL = "1";
        }
        else if (gender.contains("Male"))
            genderSQL = "gender = 0";
        else
            genderSQL = "gender = 1";   
    }
    
    private void setIncomeSQL() {
		incomeSQL = "";
		if (income.isEmpty() || income.contains("Any")) {
			incomeSQL = "1";
		}
		else {
		    for (String s: income) {
		        if (!incomeSQL.isEmpty())
		            incomeSQL += " OR ";
		        switch(s) {
		        case "Low":
		                incomeSQL += "INCOME=0";
		                break;
		        case "Medium":
		                incomeSQL += "INCOME=1";
		                break;
		        case "High":
		                incomeSQL += "INCOME=2";
		                break;
		        default:
		            incomeSQL = "1";
		                break;
		        }
		    }
		}
    }
 
      
      
    private void setAgeSQL()
    {
        ageSQL = "";
        if (age.isEmpty() || age.contains("Any")) {
            ageSQL = "1";
        }
        else {
            for (String s: age) {
                if (!ageSQL.isEmpty())
                    ageSQL += " OR ";
                switch(s) {
                    case "Less than 25":
                            ageSQL += "AGE=0";
                            break;
                    case "25 to 34":
                            ageSQL += "AGE=1";
                            break;
                    case "35 to 44":
                            ageSQL += "AGE=2";
                            break;
                    case "45 to 54":
                            ageSQL += "AGE=3";
                            break;
                    case "Greater than 55":
                            ageSQL += "AGE=4";
                            break;
                  
                    default:
                            ageSQL += "1 = 1";
                            break;
                }
            }
        }
    }
    	 
     
    public String getSql()
    {
        return "(" + contextSQL + ") and (" + ageSQL + ") and (" + incomeSQL + ") and (" + genderSQL + ") and " + dateSQL;
        
    }
    public String getIncomeSQL() {
        return "(" + incomeSQL +")";
    }
     public String getGenderSQL() {
         return "(" + genderSQL + ")";
    }      
    public String getContextSQL()
    {
        return "(" + contextSQL + ")";
    }
    public String getAgeSQL()
    {
        return "(" + ageSQL + ")";
    }
    private void setContextSQL()
    {
        contextSQL = "";
        if (context.isEmpty() || context.contains("Any") ) {
            contextSQL = "1 = 1";
        }
        else {
            for (String s: context) {
                if (!contextSQL.isEmpty())
                    contextSQL += " OR ";
                switch(s) {
                    case "News":
                            contextSQL += "CONTEXT=0";
                            break;
                    case "Shopping":
                            contextSQL += "CONTEXT=1";
                            break;
                    case "Social Media":
                            contextSQL += "CONTEXT=2";
                            break;
                    case "Blog":
                            contextSQL += "CONTEXT=3";
                            break;
                    case "Hobbies":
                            contextSQL += "CONTEXT=4";
                            break;
                    case "Travel":
                            contextSQL += "CONTEXT=5";
                            break;
                    default:
                            contextSQL += "1 = 1";
                            break;
                    }
                }
            }
           
    	 
    }

    public void setContext(ObservableList<String> context) {
        this.context = context;
        setContextSQL();
        
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
        setDateSQL();
    }
    
    public void setDateSQL() {
    	StringBuilder dateQuery = new StringBuilder("(");
    	
    	if(dateFrom != null)
    		dateQuery.append("DATE >= '" + dateFrom.toString() + " 00:00:00'");
    	else
    		dateQuery.append("1");
    	
    	dateQuery.append(" AND ");
    	
    	if(dateTo != null)
    		dateQuery.append("DATE < '" + dateTo.toString() + " 24:00:00'");
    	else
    		dateQuery.append("1");
    	
    	dateQuery.append(")");
    	
    	dateSQL = dateQuery.toString();
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
        setDateSQL();
    }
    
    //TODO: Either make this do something or delete it
    public void setDateFromSQL() {
    	
    }
    
    /* Default constructor sets all fields to 'any', and leaves date range unrestricted */
    public Filter()
    {
         gender = new ArrayList();
         gender.add("Any");
         age = new ArrayList();
         age.add("Any");
    	 
        setIncome(FXCollections.observableArrayList("Any"));
        setContext(FXCollections.observableArrayList("Any"));
        setDateSQL();
                 
                
    }
}
