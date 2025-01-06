package com.example.comp2001_aistenphillips;


import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import java.lang.reflect.Type;
import java.util.List;




import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONException;
import org.json.JSONObject;


public class API_Service {              //This class will include methods to sending and receiving requests from the API


    private static final String BASE_URL = "http://10.224.41.11/comp2000";  //Create a static final to reference the API
    //final - cannot be changed once it is assigned
    private static RequestQueue requestQueue;                               //RequestQueue requestQueue will be used to manage GET / POST / PUT / DELETE requests. (RequestQueue is from volley library)
    private static final Gson gson = new Gson();                            //Creates a gson. Used to deserialize or serialize JSON data. Serialize is used to convert Java into JSON and is used when sending data.
    // deserialize is used to convert the JSON string back into java
    // (Gson is from Gson library)




    private static void initQueue(Context context) {
        if (requestQueue == null) {                                                         //if requestQueue is null that means it has not been initialised and we can create a new RequestQueue


            requestQueue = Volley.newRequestQueue(context.getApplicationContext());         //context.getApplicationContext() - used getApp... to ensure that we are using application context, which is useful as it is recommended for creating a
            //newRequestQueue because it avoids memory leaks that could occur if activity context is used
        }
    }




    //Method to Retrieve a list of all employees
    public interface EmployeeResponseListener {
        void onSuccess(List<Employee> employees);
        void onError(String errorMessage);
    }
    public static void getAllEmployees(Context context, final EmployeeResponseListener listener) {


        initQueue(context);                                                                              //Ensures that the queue is set up to handle network requests


        String url = BASE_URL + "/employees";                                                            //Modify the BASE_URL endpoint


        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,        //JsonArrayRequest - Volley class that allows us to make GET  requests (Expecting a JSON array)
                //Request.Method.GET - used to reference the GET function
                //url - the url to retrieving employee data


                new Response.Listener<JSONArray>() {                                    //handles the success response from the server
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Type listType = new TypeToken<List<Employee>>() {}.getType();               //Creates a new TypeToken because Gson requires a specific type to parse the JSON into a list of Employee objects


                            List<Employee> employees = gson.fromJson(response.toString(), listType);    //This uses Gson to deserialize the JSONArray into List Employee
                            //response.toString() - converts JSONArray into a string format that Gson can process
                            //listType - target type is a list of Employee objects


                            listener.onSuccess(employees);                                              //If parse was successful, call the onSuccess method


                        } catch (Exception e) {                                                    //If an error occurs during parsing
                            listener.onError("Failed to retrieve employee data");      //call the onError method and pass in the error message
                        }
                    }
                },


                new Response.ErrorListener() {                                                  //Handles the error responses from the server
                    @Override
                    public void onErrorResponse(VolleyError error) {                                //VolleyError contains information about the error that occurred
                        listener.onError(error.getMessage());                                       //This will send the error message
                    }
                });


        requestQueue.add(request);                                                              //Adds the request to requestQueue
    }






    //Method to get employee by ID
    public interface EmployeeIdResponseListener {
        void onSuccess(Employee employee);                              // Success callback with a single employee
        void onError(String errorMessage);                              // Error callback
    }
    public static void getEmployeeById(Context context, int employeeId, final EmployeeIdResponseListener listener){


        initQueue(context);
        String url = BASE_URL + "/employees/get/"+ employeeId;


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                Employee employee = gson.fromJson(response.toString(), Employee.class);
                Log.d("API_Response", "getEmployeeByID: Request success: " + response.toString());
                listener.onSuccess(employee);


            } catch (Exception e) {
                listener.onError("Failed to parse employee data: " + e.getMessage());
            }


        },  error -> {
            listener.onError("Request failed: " + error.getMessage());
            Log.e("API_Response", "getEmployeeByID: Request failed: " + error.getMessage());
        });
        requestQueue.add(request);
    }








    //METHOD to add an Employee to the API
    public interface EmployeeAddCallback {                                      //Used to handle callback events from ADD employee
        void onSuccess(String successMessage);                                  //Returns a success message
        void onError(String errorMessage);                                      //Returns a fail message
    }
    public static void addEmployee(Context context, Employee employee, final EmployeeAddCallback callback){


        initQueue(context);
        String url =BASE_URL + "/employees/add";
        JSONObject employeeJSON = new JSONObject();                             //Create a new Employee to send via JSON


        try{                                                                            //Try input these fields
            employeeJSON.put("firstname", employee.getFirstname());
            employeeJSON.put("lastname", employee.getLastname());
            employeeJSON.put("email", employee.getEmail());
            employeeJSON.put("department", employee.getDepartment());
            employeeJSON.put("salary", employee.getSalary());
            employeeJSON.put("joiningdate", employee.getJoiningDate());


        }
        catch (JSONException e) {
            callback.onError("Failed to create JSON Object: " + e.getMessage() );
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, employeeJSON, response -> {
            try {
                String message = response.getString("message");
                Log.d("API_Response", "addEmployee : Request success: " + response);
                callback.onSuccess(message);


            }
            catch (JSONException e) {
                Log.e("API_Response", "addEmployee : Request failed: " + e.getMessage());
                callback.onError("Message Failed: " + e.getMessage());
            }
        },
                error -> {
                    callback.onError("Request failed: " + error.getMessage());
                    Log.e("API_Response", "addEmployee : Request failed: " + error.getMessage());
                });
        requestQueue.add(request);
    }




    //METHOD to Delete Employee
    public interface EmployeeDeleteCallback {
        void onSuccess(String successMessage);
        void onError(String errorMessage);
    }
    public static void deleteEmployee(Context context, Employee employee, EmployeeDeleteCallback callback){


        initQueue(context);
        String url =BASE_URL + "/employees/delete/"  + employee.getId() ;
        JSONObject employeeJSON = new JSONObject();


        try{
            employeeJSON.put("id", employee.getId());


        }
        catch (JSONException e) {
            callback.onError("Failed to create JSON Object: " + e.getMessage());
            Log.e("API_Response", "deleteEmployee : Request failed: " + e.getMessage());
            return;
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, url, employeeJSON, response -> {
            try {
                String message = response.getString("message");
                callback.onSuccess(message);


            }
            catch (JSONException e) {
                callback.onError("Message Failed: " + e.getMessage());
            }
        },
                error -> {
                    callback.onError("Request failed: " + error.getMessage());
                    Log.e("API_Response", "deleteEmployee : Request failed: " + error.getMessage());
                });




        requestQueue.add(request);
    }




    //METHOD to modify employees
    public interface EmployeeModified {
        void onSuccess(String successMessage);
        void onError(String errorMessage);
    }
    public static void modifyEmployee(Context context, Employee employee, EmployeeModified modified){


        initQueue(context);
        String url =BASE_URL + "/employees/edit/"  + employee.getId();
        JSONObject employeeJSON = new JSONObject();


        try{
            employeeJSON.put("firstname", employee.getFirstname());
            employeeJSON.put("lastname", employee.getLastname());
            employeeJSON.put("email", employee.getEmail());
            employeeJSON.put("department", employee.getDepartment());
            employeeJSON.put("salary", employee.getSalary());
            employeeJSON.put("joiningdate", employee.getJoiningDate());
            employeeJSON.put("leaves", employee.getLeaves());
        }
        catch (JSONException e) {
            modified.onError("Failed to create JSON Object: " + e.getMessage());
            return;
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, employeeJSON, response -> {
            try {
                String message = response.getString("message");
                modified.onSuccess(message);


            } catch (JSONException e) {
                modified.onError("Message Failed: " + e.getMessage());
            }


        },
                error -> {
                    modified.onError("Request failed: " + error.getMessage());
                    Log.e("API_Response", "modifyEmployee : Request failed: " + error.getMessage());
                });
        requestQueue.add(request);
    }




    public interface SalaryUpdateCallback {
        void onSuccess(String successMessage);
        void onError(String errorMessage);
    }
    public static void updateEmployeeSalary(Context context, int employeeId, double newSalary, SalaryUpdateCallback callback){


        initQueue(context);
        String url = BASE_URL + "/employees/edit/" + employeeId;
        JSONObject employeeJSON = new JSONObject();
        try{
            employeeJSON.put("salary", newSalary);
        }
        catch (JSONException e) {
            callback.onError("Failed to create JSON Object: " + e.getMessage());
            return;
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, employeeJSON, response -> {
            try {
                String message = response.getString("message");
                callback.onSuccess(message);


            } catch (JSONException e) {
                callback.onError("Message Failed: " + e.getMessage());
            }


        },
                error -> {
                    callback.onError("Request failed: " + error.getMessage());
                    Log.e("API_Response", "modifyEmployee : Request failed: " + error.getMessage());
                });


        requestQueue.add(request);


    }






    //METHOD to do a health check on the API
    public interface HealthCheckCallBack {
        void onSuccess(String successMessage);
        void onError(String errorMessage);
    }
    public static void healthCheck(Context context, final HealthCheckCallBack callback){


        initQueue(context);
        String url = BASE_URL + "/health";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                String statusMessage = response.getString("status");
                callback.onSuccess(statusMessage);
            } catch (JSONException e) {
                callback.onError("Failed to get Health check: " + e.getMessage());
            }
        }, error -> {
            callback.onError("Request failed: " + error.getMessage());
            Log.e("API_Response", "healthCheck : Request failed: " + error.getMessage());
        });
        requestQueue.add(request);
    }
}
