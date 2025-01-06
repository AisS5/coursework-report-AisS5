package com.example.comp2001_aistenphillips;

import static com.example.comp2001_aistenphillips.Salary_Service.updateSalaries;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ADMIN_employees extends AppCompatActivity {

    public ImageView backButton, APIhealthCheckBtn, updateAll;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private EmployeeList_Adapter employeeAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_employees);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();




        //BACK BUTTON
        backButton.setOnClickListener(v->{
            Intent intent = new Intent(ADMIN_employees.this, ADMIN_dashboard.class);
            startActivity(intent);
        });

        APIhealthCheckBtn.setOnClickListener(v-> checkApiHealth());     //Call the checkApiHealth method when the APIHealthCheck is clicked
        updateAll.setOnClickListener(v-> updateAllEmployee());          //Call the updateAllEmployee method when the updateAll is clicked


        searchView.clearFocus();                                                //Clear the focus from the SearchView. Causes the keyboard to disappear
        recyclerView.setLayoutManager(new LinearLayoutManager(this));    //Set the layout manager for the RecyclerView


        employeeAdapter = new EmployeeList_Adapter(new ArrayList<>(), (view, position) -> {                 //Sets the EmployeeList_Adapter with an empty list and a click listener for each item

            Employee clickEmployee = employeeAdapter.getFullemployeeList().get(position);                       //Get the employee object at the clicked position in the list
            Intent intent = new Intent(ADMIN_employees.this, ADMIN_employee_modify.class);
                                                                                                                //Pass the employee details to the next activity using putExtra. This is used to display the clicked employee's details
            intent.putExtra("employee ID: ", clickEmployee.getId());
            intent.putExtra("employee_firstname", clickEmployee.getFirstname());
            intent.putExtra("employee_lastname", clickEmployee.getLastname());
            intent.putExtra("employee_email", clickEmployee.getEmail());
            intent.putExtra("employee_department", clickEmployee.getDepartment());
            intent.putExtra("employee_salary", clickEmployee.getSalary());
            intent.putExtra("employee_joiningDate", clickEmployee.getJoiningDate());
            startActivity(intent);
        });

        recyclerView.setAdapter(employeeAdapter);                //Set the RecyclerView to display the employee list

        //FETCH ALL EMPLOYEES
        API_Service.getAllEmployees(this, new API_Service.EmployeeResponseListener() {      //Call the API service to fetch all employees
            @Override
            public void onSuccess(List<Employee> employees) {
                employeeAdapter.updateEmployeeList(employees);                                          //Update the employee list in the adapter with the data fetched from the API
                Log.d("ADMIN_employeeList", "Employee list loaded: " + employees.size());
            }
            @Override
            public void onError(String errorMessage) {
                Toast.makeText(ADMIN_employees.this, "Error getting employees: Do an API health check", Toast.LENGTH_SHORT).show();
                Log.e("ADMIN_employeeList", "Error: " + errorMessage);
            }

        });
        //SEARCH VIEW
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {    //Set up the listener for the search query in the SearchView. This is used to filter the employee list based on the query
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterlist(query);                              //Call the filter method
                return true;
            }
            @Override
            public boolean onQueryTextChange(String fullList) {
                if (fullList.isEmpty()) {                      //If the search bar is empty, reset the employee list to the full list
                    employeeAdapter.resetEmployeeList();
                } else {
                    filterlist(fullList);                      //If the query is not empty, filter the employee list based on the new query
                }
                return true;
            }
        });
    }

    //Holds all the necessary variables and assigns them to objects in the XML file
    private void init(){
        backButton = findViewById(R.id.back_btn);
        APIhealthCheckBtn = findViewById(R.id.api_health_check);
        recyclerView = findViewById(R.id.employee_recycler_view);
        searchView = findViewById(R.id.employee_search_view);
        updateAll = findViewById(R.id.updateAll);
    }

    //FILTER LIST
    //This method is used to filter the employee list based on the query
    private void filterlist(String query) {
        query = query.trim();                               //Remove any whitespace from the query
        if (query.isEmpty()) {
            employeeAdapter.resetEmployeeList();
        } else {
            employeeAdapter.filterEmployeeList(query);      //If the query is not empty, filter the employee list based on the query
        }
    }

    //CHECK API HEALTH
    //This method is used to check the health of the API by calling the healthCheck method from API_Service
    private void checkApiHealth() {
        API_Service.healthCheck(ADMIN_employees.this, new API_Service.HealthCheckCallBack() {
            @Override
            public void onSuccess(String successMessage) {
                Toast.makeText(ADMIN_employees.this, "Healthy: " + successMessage, Toast.LENGTH_SHORT).show();
                Log.e("HEALTH-CHECK", "Health Check Success: " + successMessage);
            }
            @Override
            public void onError(String errorMessage) {
                Toast.makeText(ADMIN_employees.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                Log.e("HEALTH-CHECK", "Health Check Error: " + errorMessage);
            }
        });

    }

    //UPDATE ALL EMPLOYEES
    //This method goes through all the employees in the API and uses updateSalaries to update Employee salaries
    private void updateAllEmployee(){
        updateSalaries(this, new API_Service.SalaryUpdateCallback(){
            @Override
            public void onSuccess(String successMessage) {

            }
            @Override
            public void onError(String errorMessage) {
                Toast.makeText(ADMIN_employees.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

    }
}