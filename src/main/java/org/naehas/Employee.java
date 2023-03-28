package org.naehas;

import com.google.gson.Gson;

public class Employee {
    /*
     * Instance variables: • Employee Name - String • Employee ID - int • Work Type-
     * char • Wage - double
     */
    private String empName;
    private int empId;
    private String workType;
    private double wageBeforeTax;
    private double wageAfterTax;

    public Employee(String empName, int empId, String workType, double wageBeforeTax) {
        this.empName = empName;
        this.empId = empId;
        this.workType = workType;
        this.wageBeforeTax = wageBeforeTax;
        this.wageAfterTax = calculateTaxWage(workType.charAt(0), wageBeforeTax);
    }
    public Employee(String json){
        Gson gson = new Gson();
        Employee tmpEmp = gson.fromJson(json, Employee.class);
        this.empId = tmpEmp.empId;
        this.empName = tmpEmp.empName;
        this.workType = tmpEmp.workType;
        this.wageBeforeTax = tmpEmp.wageBeforeTax;
        this.wageAfterTax = calculateTaxWage(tmpEmp.workType.charAt(0), tmpEmp.wageBeforeTax);
    }

    public String toString(){
        return new Gson().toJson(this);
    }
    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public double getWageAfterTax() {
        return wageAfterTax;
    }

    public void setWageAfterTax(double wageAfterTax) {
        this.wageAfterTax = wageAfterTax;
    }

    public double getWageBeforeTax() {
        return wageBeforeTax;
    }

    public void setWageBeforeTax(double wageBeforeTax) {
        this.wageBeforeTax = wageBeforeTax;
    }
    public static double calculateTaxWage(char workType, double wage) {
        CalculateWageAfterTax c = (w, r) -> w - (w*r)/100;
        if (workType == 'T') {
            return c.calculateTax(wage, 15);
        } else if (workType == 'C') {
            return c.calculateTax(wage, 18);
        } else if (workType == 'F') {
            double benifits = wage * 10 / 100;
            return c.calculateTax(wage, 20) - benifits;

        }
        return -1;
    }
}
