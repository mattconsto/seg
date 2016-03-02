/*
 
 */
package dashboard.model;

import javafx.collections.ObservableList;

 

public class Filter {

        ObservableList <String> gender ;
        ObservableList <String> age ;
        ObservableList <String> income; 
        ObservableList <String> context;
        String dateTo;
        String dateFrom;
        String contextSQL = "(1 = 1)";
        String ageSQL = "(1 = 1)";
        String incomeSQL =  "(1 = 1)";
        String genderSQL =  "(1 = 1)";
        

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
        
        if (gender.isEmpty() || gender.contains("Any") || (gender.contains("Female") && gender.contains("Male"))){
            genderSQL = "1 = 1";
        }
        else if (gender.contains("Male("))
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
                    incomeSQL = "1=1";
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
        return "(" + contextSQL + ") and (" + ageSQL + ")";
        
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
        ageSQL = "";
        if (context.isEmpty() || context.contains("Any") ) {
            ageSQL = "1 = 1";
        }
        else {
            for (String s: context) {
                if (!ageSQL.isEmpty())
                    ageSQL += " OR ";
                switch(s) {
                    case "News":
                            ageSQL += "CONTEXT=0";
                            break;
                    case "Shopping":
                            ageSQL += "CONTEXT=1";
                            break;
                    case "Social Media":
                            ageSQL += "CONTEXT=2";
                            break;
                    case "Blog":
                            ageSQL += "CONTEXT=3";
                            break;
                    case "Hobbies":
                            ageSQL += "CONTEXT=4";
                            break;
                    case "Travel":
                            ageSQL += "CONTEXT=5";
                            break;
                    default:
                            ageSQL += "1 = 1";
                            break;
                    }
                }
            }
           
    	 
    }

    public void setContext(ObservableList<String> context) {
        this.context = context;
        setContextSQL();
        
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }
    public Filter() {
        
    }
  
   
}
