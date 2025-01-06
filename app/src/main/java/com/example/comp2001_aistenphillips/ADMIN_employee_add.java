package com.example.comp2001_aistenphillips;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Spinner;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import org.mindrot.jbcrypt.BCrypt;

//ADMIN EMPLOYEE ADD
public class ADMIN_employee_add extends AppCompatActivity {

    private EditText addFirstname, addLastname, addEmail, addJoiningDate, addDepartment, addSalary, addPassword;
    private Spinner accountTypeSpinner;
    private ImageView saveEmployeeBtn, backBtn;
    private int accountType;
    private DatabaseHelper dbHelper;
    private LoginUsersHelper loginHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_employee_add);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        //Call the setupAccountTypeSpinner method to set up the account type spinner
        setupAccountTypeSpinner();

        //Set up Helpers
        dbHelper = new DatabaseHelper(ADMIN_employee_add.this);
        loginHelper = new LoginUsersHelper(dbHelper);

        //BACK BUTTON
        backBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ADMIN_employee_add.this, ADMIN_dashboard.class);
            startActivity(intent);
        });

        //SAVE BUTTON
        saveEmployeeBtn.setOnClickListener(v -> {

            //Send the following to the API
            String firstname = addFirstname.getText().toString();
            String lastname = addLastname.getText().toString();
            String joiningdate = addJoiningDate.getText().toString();
            String department = addDepartment.getText().toString();
            String salary = addSalary.getText().toString();

            //Send the following to the Database
            String password = addPassword.getText().toString();
            String selectedAccountType = accountTypeSpinner.getSelectedItem().toString();

            //Send to both
            String email = addEmail.getText().toString();


            //CHECKS
            try {
                float fSalary = Float.parseFloat(salary);

            } catch (NumberFormatException e) {
                Toast.makeText(ADMIN_employee_add.this, "Invalid salary format", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedAccountType.equals("Select Account Type")) {
                Toast.makeText(ADMIN_employee_add.this, "Please select an account type", Toast.LENGTH_SHORT).show();
                return;
            } else if (firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || joiningdate.isEmpty() || department.isEmpty() || salary.isEmpty() || password.isEmpty()) {
                Toast.makeText(ADMIN_employee_add.this, "All Fields must be filled out", Toast.LENGTH_SHORT).show();
                return;
            } else if (loginHelper.isUsernameTaken(email)) {
                Toast.makeText(ADMIN_employee_add.this, "This email is already registered", Toast.LENGTH_SHORT).show();
                return;
            } else {
                Employee newEmployee = new Employee();      //Create a new Employee object and fill it with the inputted data
                newEmployee.setFirstname(firstname);
                newEmployee.setLastname(lastname);
                newEmployee.setEmail(email);
                newEmployee.setJoiningDate(joiningdate);
                newEmployee.setDepartment(department);
                newEmployee.setSalary(Float.parseFloat(salary));

                if (selectedAccountType.equals("Admin")) {              //Set the accountType based on the selected account type
                    accountType = 1;
                } else if (selectedAccountType.equals("Employee")) {
                    accountType = 0;
                }

                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());                  //Hash the password using BCrypt and save it to the database
                boolean isInserted = loginHelper.addUser(accountType, email, hashedPassword);
                if (isInserted) {
                    Log.d("UsersTable", "User added successfully");
                } else {
                    Log.d("UsersTable", "Failed to add user");
                }

                addEmployee(newEmployee);             //Call the addEmployee method to add the employee to the API

            }
        });
    }

    private void init(){                                            //Holds all the necessary variables and assigns them to objects in the XML file
        saveEmployeeBtn= findViewById(R.id.save_btn);
        backBtn= findViewById(R.id.back_btn);
        addFirstname = findViewById(R.id.add_employee_firstname);
        addLastname = findViewById(R.id.add_employee_lastname);
        addEmail = findViewById(R.id.add_employee_email);
        addJoiningDate = findViewById(R.id.add_employee_joiningDate);
        addDepartment = findViewById(R.id.add_employee_department);
        addSalary = findViewById(R.id.add_employee_salary);
        addPassword = findViewById(R.id.add_employee_password);
        accountTypeSpinner = findViewById(R.id.account_type_spinner);
    }

    private void setupAccountTypeSpinner(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.new_account_type, android.R.layout.simple_spinner_item);                    //Create an ArrayAdapter using the string array and a default spinner layout

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accountTypeSpinner.setAdapter(adapter);
        accountTypeSpinner.setSelection(0);

        accountTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                String selectedItem = parent.getItemAtPosition(position).toString();
                if(selectedItem.equals("Admin")){
                    accountType = 1;
                }else if(selectedItem.equals("Employee")){
                    accountType = 0;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    //Method to add an employee to the API
    private void addEmployee(Employee employee) {
        API_Service.addEmployee(ADMIN_employee_add.this, employee, new API_Service.EmployeeAddCallback() {  //Call the addEmployee method in the API_Service class
            @Override
            public void onSuccess(String successMessage) {
                Toast.makeText(ADMIN_employee_add.this, successMessage, Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(ADMIN_employee_add.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }




}