package com.example.comp2001_aistenphillips;

import static com.example.comp2001_aistenphillips.API_Service.updateEmployeeSalary;
import android.content.Context;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
public class Salary_Service {

    //UPDATE SALARIES
    //This Method updates salaries of employees who's joining date is more than 1 year ago
    public static void updateSalaries(Context context, final API_Service.SalaryUpdateCallback callback){

        //Fetch all employees using the getAll method from the API_Service
        API_Service.getAllEmployees(context, new API_Service.EmployeeResponseListener(){
            @Override
            public void onSuccess(List<Employee> employees){
                try{
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);      //Create a SimpleDateFormat object to parse the joiningDates
                    Date currentDate = new Date();                                              //Get the current date and time
                    for (Employee employee : employees){                                        //Iterate through each employee
                        String joiningDateString = employee.getJoiningDate();                       //Get the joiningDate of the employee
                        if(joiningDateString == null || joiningDateString.isEmpty()){
                            continue;
                        }
                        Date joiningDate = dateFormat.parse(joiningDateString);                     //Parse the joiningDate to a Date object
                        long difference = currentDate.getTime() - joiningDate.getTime();            //Calculate the difference between the current date and the joiningDate
                        long differenceDays = TimeUnit.MILLISECONDS.toDays(difference);             //Convert the difference to days

                        if(differenceDays >= 365){                                                  //If the differenceDays is more than 365 that means its been a year since the Employee joined
                            double newSalary = employee.getSalary()  * 1.05;                        //Increase the salary by 5%
                            updateEmployeeSalary(context, employee.getId(), newSalary, callback);   //Call the updateEmployeeSalary method from the API_Service to update the salary
                        }

                    }
                }catch (Exception e) {
                    callback.onError("Error processing employee data: " + e.getMessage());
                    Log.e("Salary_Service", "Error: " + e.getMessage());
                }
            }
            @Override
            public void onError(String errorMessage) {
                callback.onError("Failed to retrieve employee data: " + errorMessage);
            }
        });
    }
}
