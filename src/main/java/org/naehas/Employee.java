package org.naehas;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Employees")
public class Employee {
    /*
     * Instance variables: • Employee Name - String • Employee ID - int • Work Type-
     * char • Wage - double
     */
    @DynamoDBAttribute(attributeName = "empName")
    private String empName;
    @DynamoDBHashKey(attributeName = "empId")
    private int empId;
    @DynamoDBAttribute(attributeName = "workType")
    private String workType;
    @DynamoDBAttribute(attributeName = "wageBeforeTax")
    private double wageBeforeTax;
    @DynamoDBAttribute(attributeName = "wageAfterTax")
    private double wageAfterTax;

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
    public static double calculateTaxWage(String workType, double wage) {
        CalculateWageAfterTax c = (w, r) -> w - (w*r)/100;
        switch (workType) {
            case "T":
                return c.calculateTax(wage, 15);
            case "C":
                return c.calculateTax(wage, 18);
            case "F":
                double benifits = wage * 10 / 100;
                return c.calculateTax(wage, 20) - benifits;

        }
        return -1;
    }
}
