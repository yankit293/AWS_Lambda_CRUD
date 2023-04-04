package org.naehas;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;


public class PayRollLambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {



    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {

        EmployeeService employeeService = new EmployeeService();
        switch (request.getHttpMethod()) {

            case "POST":
                return employeeService.saveEmployee(request, context);

            case "GET":
                if (request.getPathParameters() != null) {
                    if(request.getPathParameters().get("wage") != null){
                        return employeeService.getEmployeesWithHigherWageThan(request, context);
                    }
                    return employeeService.getEmployeeById(request, context);
                }
                return employeeService.getEmployees(request, context);
            case "DELETE":
                if (request.getPathParameters() != null) {
                    return employeeService.deleteEmployeeById(request, context);
                }
            default:
                throw new Error("Unsupported Methods:::" + request.getHttpMethod());

        }
    }
}
