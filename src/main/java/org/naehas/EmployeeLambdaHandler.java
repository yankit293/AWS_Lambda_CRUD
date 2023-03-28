package org.naehas;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.io.*;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;

/**
 * Hello world!
 *
 */
public class EmployeeLambdaHandler implements RequestStreamHandler
{
    private final String DYNAMO_TABLE = "Employees";
    @SuppressWarnings("unchecked")
    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(output);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        JSONParser parser = new JSONParser();
        JSONObject responseObj = new JSONObject();
        JSONObject responseBody = new JSONObject();

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDB dynamoDB = new DynamoDB(client);
        int empId;
        Item resItem = null;
        try{
            JSONObject reqObj = (JSONObject)parser.parse(reader);
            if(reqObj.get("pathParameters")!=null){
                JSONObject pps = (JSONObject) reqObj.get("pathParameters");
                if(pps.get("empId")!=null){
                    empId = Integer.parseInt(pps.get("empId").toString());
                    resItem = dynamoDB.getTable(DYNAMO_TABLE).getItem("empId", empId);
                }
            } else if (reqObj.get("queryStringParameters")!=null) {
                JSONObject qps = (JSONObject) reqObj.get("queryStringParameters");
                if(qps.get("empId")!=null){
                    empId = Integer.parseInt(qps.get("empId").toString());
                    resItem = dynamoDB.getTable(DYNAMO_TABLE).getItem("empId", empId);
                }
            }
            if(resItem != null){
                Employee employee = new Employee(resItem.toJSON());
                responseBody.put("Employee", employee);
                responseObj.put("statusCode", 200);
            }else{
                responseBody.put("message", "No Employee Found!");
                responseObj.put("statusCode", 404);
            }
            responseObj.put("body", responseBody.toString());
        }catch(Exception e){
                context.getLogger().log("ERROR :" + e.getMessage());
        }
        writer.write(responseObj.toString());
        reader.close();
        writer.close();
    }
    @SuppressWarnings("unchecked")
    public void handlePutRequest(InputStream input, OutputStream output, Context context) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(output);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        JSONParser parser = new JSONParser();
        JSONObject responseObj = new JSONObject();
        JSONObject responseBody = new JSONObject();
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDB dynamoDB = new DynamoDB(client);
        try{
            JSONObject reqObj = (JSONObject) parser.parse(reader);
            if(reqObj.get("body")!=null){
                Employee employee = new Employee(reqObj.get("body").toString());
                dynamoDB.getTable(DYNAMO_TABLE)
                        .putItem(new PutItemSpec().withItem(new Item()
                        .withNumber("empId", employee.getEmpId())
                        .withString("empName", employee.getEmpName())
                        .withString("workType", employee.getWorkType())
                        .withNumber("wageBeforeTax", employee.getWageBeforeTax())
                        .withNumber("wageAfterTax", employee.getWageAfterTax())));
                responseBody.put("message", "New Employee Created/updated");
                responseObj.put("statusCode", 200);
                responseObj.put("body", responseBody.toString());
            }
        } catch(Exception e){
            responseObj.put("statusCode", 400);
            responseObj.put("error :", e);
        }
        writer.write(responseObj.toString());
        reader.close();
        writer.close();
    }
    @SuppressWarnings("unchecked")
    public void handleDeleteRequest(InputStream input, OutputStream output, Context context) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(output);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        JSONParser parser = new JSONParser();
        JSONObject responseObj = new JSONObject();
        JSONObject responseBody = new JSONObject();

        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDB dynamoDB = new DynamoDB(client);

        try {
            JSONObject reqObj = (JSONObject) parser.parse(reader);
            if(reqObj.get("pathParameters")!=null){
                JSONObject pps = (JSONObject) reqObj.get("pathParameters");
                if(pps.get("empId")!=null){
                    int empId = Integer.parseInt(pps.get("empId").toString());
                    dynamoDB.getTable(DYNAMO_TABLE).deleteItem("empId", empId);
                }
            }
            responseBody.put("message", "Employee Deleted!");
            responseObj.put("body",responseBody.toString());

        } catch (Exception e) {
            responseObj.put("statusCode", 400);
            responseObj.put("error", e);
        }
        writer.write(responseObj.toString());
        reader.close();
        writer.close();
    }
}
