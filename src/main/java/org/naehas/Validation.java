package org.naehas;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;

public class Validation {

    public static boolean validateEmployee(APIGatewayProxyRequestEvent apiGatewayRequest, Context context){

        Employee employee = Utility.convertStringToObj(apiGatewayRequest.getBody(), context);
        return validateId(employee.getEmpId(), context)
                && validateName(employee.getEmpName(), context)
                && validateWorkType(employee.getWorkType(), context)
                && validateWage(employee.getWageBeforeTax(), employee.getWorkType(), context);
    }

    public static boolean validateName(String empName, Context context) {

        if (empName.length() < 5) {
            context.getLogger().log("Employee name cannot be less than 5 characters!");
            return false;
        } else if (!(empName.trim().contains(" "))) {
            context.getLogger().log("Employee name must have at least one space!");
            return false;
        }
        return true;
    }

    // validate ID
    public static boolean validateId(int empId,Context context) {
        if (empId < 0) {
            context.getLogger().log("Employee ID must be a positive integer!");
            return false;
        }

        return true;

    }

    public static boolean validateWorkType(String workType, Context context) {
        if (workType.equals("T") || workType.equals("C") || workType.contains("F")) {
            return true;
        } else {
            context.getLogger().log("Work Type can only be (T, C, F)!");
            return false;
        }
    }

    // validate Wage
    public static boolean validateWage(double wage, String workType, Context context) {
        switch (workType) {
            case "T":
                if (wage > 90) {
                    context.getLogger().log("The hourly pay cannot exceed 90.00 but can be 0!");
                    return false;
                }
                break;
            case "C":
                if (wage < 1000 || wage > 3500) {
                    context.getLogger().log("The bi-weekly pay cannot be below 1000.00 or more than 3500.00!");
                    return false;
                }
                break;
            case "F":
                if (wage < 3000) {
                    context.getLogger().log("The monthly pay cannot be less than 3000.00!");
                    return false;
                }
                break;
        }
        return true;
    }

}
