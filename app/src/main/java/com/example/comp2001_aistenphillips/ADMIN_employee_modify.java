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
import java.text.ParseException;
import java.text.SimpleDateFormat;

//ADMIN EMPLOYEE MODIFY
public class ADMIN_employee_modify extends AppCompatActivity {

    private ImageView backBtn, deleteBtn, saveBtn;
    private Employee selectedEmployee;
    private EmployeeList_Adapter employeeAdapter;
    private TextView employeeIDtext;
    private EditText employeeFirstName, employeeLastName, employeeEmail, employeeJoiningDate, employeeDepartment, employeeSalary, employeeLeaves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_employee_modify);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();

        backBtn.setOnClickListener(v->{
            Intent intent = new Intent(ADMIN_employee_modify.this, ADMIN_employees.class);
            startActivity(intent);
        });

        //Retrieve the data from the intent
        int employeeID = getIntent().getIntExtra("employee ID: ", -1);
        String firstname = getIntent().getStringExtra("employee_firstname");
        String lastname = getIntent().getStringExtra("employee_lastname");
        String email = getIntent().getStringExtra("employee_email");
        String joiningDate = getIntent().getStringExtra("employee_joiningDate");
        String department = getIntent().getStringExtra("employee_department");
        double salary = getIntent().getDoubleExtra("employee_salary", 0.0);
        int leaves = getIntent().getIntExtra("employee_leaves", 30);

        //Set the retrieved data into the layout to display
        employeeIDtext.setText("EMPLOYEE: " + String.valueOf(employeeID));
        employeeFirstName.setText(firstname );
        employeeLastName.setText(lastname);
        employeeEmail.setText(email);
        employeeJoiningDate.setText(joiningDate);
        employeeDepartment.setText(department);
        employeeSalary.setText(String.valueOf(salary));
        employeeLeaves.setText(String.valueOf(leaves));

        //Call the fetchEmployeebyId method
        fetchEmployeebyId(employeeID);

        //DELETE BUTTON
        deleteBtn.setOnClickListener(v -> {
            if (selectedEmployee != null) {
                deleteEmployee(selectedEmployee);           //Call the deleteEmployee method to delete the employee
                Intent Deleteintent = new Intent(ADMIN_employee_modify.this, ADMIN_employees.class);
                startActivity(Deleteintent);
            }
        });

        //SAVE BUTTON
        saveBtn.setOnClickListener(v ->{
            if(selectedEmployee != null){                                               //Set each field to the inputted data
                String updatedFirstName = employeeFirstName.getText().toString();
                String updatedLastName = employeeLastName.getText().toString();
                String updatedEmail = employeeEmail.getText().toString();
                String updatedJoiningDate = employeeJoiningDate.getText().toString();
                String updatedDepartment = employeeDepartment.getText().toString();
                String updatedSalary = employeeSalary.getText().toString();
                String updatedLeaves = employeeLeaves.getText().toString();

                //CHECKS
                if (updatedFirstName.isEmpty() || updatedLastName.isEmpty() || updatedEmail.isEmpty() || updatedJoiningDate.isEmpty() || updatedDepartment.isEmpty() || updatedSalary.isEmpty() || updatedLeaves.isEmpty()){
                    Toast.makeText(ADMIN_employee_modify.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
                else if (!ValidDateFormat(updatedJoiningDate)){
                    Toast.makeText(ADMIN_employee_modify.this, "Joining date must be in the format YYYY-MM-DD", Toast.LENGTH_SHORT).show();
                }
                else {
                    selectedEmployee.setFirstname(updatedFirstName);
                    selectedEmployee.setLastname(updatedLastName);
                    selectedEmployee.setEmail(updatedEmail);
                    selectedEmployee.setJoiningDate(updatedJoiningDate);
                    selectedEmployee.setDepartment(updatedDepartment);
                    selectedEmployee.setSalary(Float.parseFloat(updatedSalary));
                    selectedEmployee.setLeaves(Integer.parseInt(updatedLeaves));
                    modifyEmployee(selectedEmployee);                                           //Call the modifyEmployee method to modify the SelectedEmployee
                }
            }
        });


    }

    //Holds all the necessary variables and assigns them to objects in the XML file
    private void init(){
        backBtn = findViewById(R.id.back_btn);
        deleteBtn = findViewById(R.id.delete_btn);
        saveBtn = findViewById(R.id.save_btn);

        employeeIDtext  = findViewById(R.id.employee_ID);
        employeeFirstName = findViewById(R.id.employee_firstname);
        employeeLastName = findViewById(R.id.employee_lastname);
        employeeEmail = findViewById(R.id.employee_email);
        employeeJoiningDate = findViewById(R.id.employee_joiningDate);
        employeeDepartment = findViewById(R.id.employee_department);
        employeeSalary = findViewById(R.id.employee_salary);
        employeeLeaves = findViewById(R.id.employee_leaves);

    }
    //FETCH BY ID
    //This method is used to fetch an employee by their ID from the API
    private void fetchEmployeebyId(int employeeID) {
        API_Service.getEmployeeById(ADMIN_employee_modify.this, employeeID, new API_Service.EmployeeIdResponseListener() {
            @Override
            public void onSuccess(Employee employee) {
                selectedEmployee = employee;
                TextView employeeIdTextView = findViewById(R.id.employee_ID);
                employeeIdTextView.setText(String.valueOf(employee.getId()));
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(ADMIN_employee_modify.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //DELETE EMPLOYEE
    //This method is used to delete an employee from the API and calls the deleteEmployee method from the API_Service
    private void deleteEmployee(Employee employee){
        API_Service.deleteEmployee(ADMIN_employee_modify.this, employee, new API_Service.EmployeeDeleteCallback() {
            @Override
            public void onSuccess(String successMessage) {
                Toast.makeText(ADMIN_employee_modify.this, successMessage, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onError(String errorMessage) {
                Toast.makeText(ADMIN_employee_modify.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    //VALID DATE FORMAT
    //This method is used to check if the date is in the correct format
    public static boolean ValidDateFormat(String date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        sdf.setLenient(false);
        try{
            sdf.parse(date);
            return true;
        }catch(ParseException e){
            return false;
        }
    }

    //MODIFY EMPLOYEE
    //This method is used to modify an employee in the API and calls the modifyEmployee method from the API_Service
    private void modifyEmployee(Employee employee){
        API_Service.modifyEmployee(ADMIN_employee_modify.this, employee, new API_Service.EmployeeModified() {
            @Override
            public void onSuccess(String successMessage) {
                Toast.makeText(ADMIN_employee_modify.this, successMessage, Toast.LENGTH_SHORT).show();
                finish();
            }
            @Override
            public void onError(String errorMessage) {
                Toast.makeText(ADMIN_employee_modify.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}