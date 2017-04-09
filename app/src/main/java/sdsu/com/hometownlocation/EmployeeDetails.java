package sdsu.com.hometownlocation;

/**
 * Created by jayacak on 4/8/17.
 */

public class EmployeeDetails {
    String empName;
    int empAge;
    int empNumber;

    //Default Constructor
    public EmployeeDetails(){}

    //Parameterised Constructor
    public EmployeeDetails(String empName,int empAge,int empNumber){
        this.empName = empName;
        this.empAge = empAge;
        this.empNumber = empNumber;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public int getEmpAge() {
        return empAge;
    }

    public void setEmpAge(int empAge) {
        this.empAge = empAge;
    }

    public int getEmpNumber() {
        return empNumber;
    }

    public void setEmpNumber(int empNumber) {
        this.empNumber = empNumber;
    }
}