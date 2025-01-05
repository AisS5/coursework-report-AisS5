package com.example.comp2001_aistenphillips;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EMPLOYEE_my_info extends AppCompatActivity {

    private TextView employeeEmail, employeeJoiningDate, employeeDepartment, employeeSalary, employeeLeaves;
    private EditText employeeFirstname, employeeLastname, employeeid;
    private ImageView saveBtn, backBtn, fetchInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_my_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        //BACK BUTTON
        backBtn.setOnClickListener(v->{
            Intent intent = new Intent(EMPLOYEE_my_info.this, EMPLOYEE_dashboard.class);
            startActivity(intent);
        });

        //FETCH BUTTON
        fetchInfo.setOnClickListener(v -> {
            String Employeeid = employeeid.getText().toString();            //Get the Employee ID
            try {
                int EmployeeidInt = Integer.parseInt(Employeeid);           //Convert the Employee ID to an integer
                fetchEmployeebyId(EmployeeidInt);                           //Call the fetchEmployeebyId method

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Please enter a valid numeric Employee ID", Toast.LENGTH_SHORT).show();
            }
        });

        //SAVE BUTTON
        saveBtn.setOnClickListener(v ->{
            if (!saveBtn.isEnabled()) {
                Toast.makeText(this, "Please provide your ID first", Toast.LENGTH_SHORT).show();
                return;
            }
            String EmployeeFirstname = employeeFirstname.getText().toString();      //Get the Employee information
            String EmployeeLastname = employeeLastname.getText().toString();

            //CHECKS
            if (EmployeeFirstname.isEmpty() || EmployeeLastname.isEmpty()) {
                Toast.makeText(this, "Firstname and Lastname cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }
                                                                                            //Create an Employee object with the new data and call the modifyEmployee method
            Employee updatedEmployee = new Employee();
            updatedEmployee.setFirstname(EmployeeFirstname);
            updatedEmployee.setLastname(EmployeeLastname);
            updatedEmployee.setEmail(employeeEmail.getText().toString());
            updatedEmployee.setDepartment(employeeDepartment.getText().toString());
            updatedEmployee.setSalary(Float.parseFloat(employeeSalary.getText().toString()));
            updatedEmployee.setJoiningDate(employeeJoiningDate.getText().toString());
            updatedEmployee.setLeaves(Integer.parseInt(employeeLeaves.getText().toString()));
            modifyEmployee(updatedEmployee);
        });
    }

    //Holds all the necessary variables and assigns them to objects in the XML file
    private void init(){
        employeeid = findViewById(R.id.employee_id);
        employeeFirstname = findViewById(R.id.employee_firstname);
        employeeLastname = findViewById(R.id.employee_lastname);
        employeeEmail = findViewById(R.id.employee_email);
        employeeJoiningDate = findViewById(R.id.employee_joiningDate);
        employeeDepartment = findViewById(R.id.employee_department);
        employeeSalary = findViewById(R.id.employee_salary);
        employeeLeaves = findViewById(R.id.employee_leaves);
        saveBtn = findViewById(R.id.save_btn);
        backBtn = findViewById(R.id.back_btn);
        fetchInfo = findViewById(R.id.fetchInfo);
        saveBtn.setEnabled(false);                          //Disable the save button until the fetch button is pressed
    }

    //FETCH EMPLOYEE ID
    //This method fetches the employee information from the API using the Employee ID
    private void fetchEmployeebyId(int employeeID) {
        API_Service.getEmployeeById(EMPLOYEE_my_info.this, employeeID, new API_Service.EmployeeIdResponseListener() {
            @Override
            public void onSuccess(Employee employee) {
                employeeFirstname.setText(employee.getFirstname());
                employeeLastname.setText(employee.getLastname());
                employeeEmail.setText(employee.getEmail());
                employeeJoiningDate.setText(employee.getJoiningDate());
                employeeDepartment.setText(employee.getDepartment());
                employeeSalary.setText(String.valueOf(employee.getSalary()));
                employeeLeaves.setText(String.valueOf(employee.getLeaves()));
                saveBtn.setEnabled(true);                                           //Enable the save button
            }
            @Override
            public void onError(String errorMessage) {
                Toast.makeText(EMPLOYEE_my_info.this, errorMessage, Toast.LENGTH_SHORT).show();
                saveBtn.setEnabled(false);                                          //Keep the save button disabled
            }
        });
    }

    //MODIFY EMPLOYEE
    //This method calls the modify method from the API_Service to update the employee information
    private void modifyEmployee(Employee employee){
        API_Service.modifyEmployee(EMPLOYEE_my_info.this, employee, new API_Service.EmployeeModified() {
            @Override
            public void onSuccess(String successMessage) {
                Toast.makeText(EMPLOYEE_my_info.this, successMessage, Toast.LENGTH_SHORT).show();
                finish();
            }
            @Override
            public void onError(String errorMessage) {
                Toast.makeText(EMPLOYEE_my_info.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}