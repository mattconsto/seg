/*
 
 */
package dashboard.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javafx.collections.ObservableList;

 

public class Filter {

        ObservableList <String> gender ;
        ObservableList <String> age ;
        ObservableList <String> income; 
        ObservableList <String> context;
        LocalDate dateTo;
        LocalDate dateFrom;
        String contextSQL;
        String ageSQL;
        String incomeSQL;
        String genderSQL;
        String dateSQL;
        

    public ObservableList<String> getGender() {
        return gender;
    }

    public void setGender(ObservableList<String> gender) {
        this.gender = gender;
        setGenderSQL();
    }

    public ObservableList<String> getAge() {
        return age;
    }

    public void setAge(ObservableList<String> age) {
        this.age = age;
        setAgeSQL();
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
            genderSQL = "1 = 1";
        }
        else if (gender.contains("Male"))
            genderSQL = "gender = 0";
        else
            genderSQL = "gender = 1";
         
  }
  private void setIncomeSQL()
  {
    incomeSQL = "";
    if (income.isEmpty() || income.contains("Any")) {
        incomeSQL = "1 = 1";
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
                default:
                    incomeSQL = "1 = 1";
                    break;
            }
        }
    }
  }
 
      
      
    private void setAgeSQL()
    {
        ageSQL = "";
        if (age.isEmpty() || age.contains("Any")) {
            ageSQL = "1 = 1";
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
        if(dateFrom != null)
        	setDateSQL();
    }
    
    public void setDateSQL() {
    	dateSQL = "(";
    	for(int i = 0; i < ChronoUnit.DAYS.between(dateFrom, dateTo)+1; i++ ) {
    		LocalDate date = dateFrom.plusDays(i);
    		dateSQL += (i==0?"":" or ") + "DATE LIKE '%" + date.getYear() + "-" + (date.getMonthValue()<10? "0" + date.getMonthValue():date.getMonthValue())  + "-" +  (date.getDayOfMonth()<10?"0" + date.getDayOfMonth():date.getDayOfMonth())+"%'";
    	}
    	dateSQL +=")";
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
        if(dateTo != null)
        	setDateSQL();
    }
    
    public void setDateFromSQL() {
    	
    }
    public Filter() {
        
    }
  
   
}
