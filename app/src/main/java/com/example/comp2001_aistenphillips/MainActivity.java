package com.example.comp2001_aistenphillips;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;

//LOGIN PAGE

public class MainActivity extends AppCompatActivity {

    private Button loginBtn;
    private TextInputEditText usernameEditText, passwordEditText;
    private DatabaseHelper dbhelper;
    private LoginUsersHelper loginHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        init();
                                                                        //Set up Helpers
        dbhelper = new DatabaseHelper(this);
        loginHelper = new LoginUsersHelper(dbhelper);

        loginBtn.setOnClickListener(v -> handleLogin());                //Call the handleLogin method when the login button is clicked


    }

    private void init(){                                               //Holds all the necessary variables and assigns them to objects in the XML file
        loginBtn = findViewById(R.id.loginButton);
        usernameEditText = findViewById(R.id.username_edittext);
        passwordEditText = findViewById(R.id.password_edittext);


    }
    private void handleLogin(){
        String username = usernameEditText.getText().toString().trim();                     //Get the inputted username and password
        String password = passwordEditText.getText().toString().trim();

        if(username.isEmpty() || password.isEmpty()) {                                                              //Check if either field is empty
            Toast.makeText(MainActivity.this, "Please enter both fields", Toast.LENGTH_SHORT).show();
            return;
        }
        if(loginHelper.validateUser(username, password)){                   //Check if the username and password are valid using the validateUser method
            int accountType = loginHelper.getUserType(username);            //Get the accountType of the logged in user

            if(accountType == -1){
                Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences UsersharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);        //Save the username of the logged in user to SharedPreferences
            SharedPreferences.Editor editor = UsersharedPreferences.edit();
            editor.putString("username", username);
            editor.apply();

            //ADMIN account Type
            if(accountType == 1){
                SharedPreferences AdminsharedPreferences = getSharedPreferences("AdminPreferences", MODE_PRIVATE);
                boolean AdminNotificationsEnabled = AdminsharedPreferences.getBoolean("notificationsEnabled", true);    //Check if notifications are enabled for the ADMIN
                if (!AdminNotificationsEnabled) {
                    Log.d("NOTIFICATIONS", "Notifications are disabled");
                }
                else{
                    boolean notificationsPending = AdminsharedPreferences.getBoolean("notificationsPending", false);    //Check if there are any pending notifications for the ADMIN
                    if (notificationsPending) {
                        NotificationsHelper notificationsHelper = new NotificationsHelper(new DatabaseHelper(this));         //If there are pending notifications. Send a notification
                        notificationsHelper.sendAdminNotification(this, "Pending Holiday Request");

                        SharedPreferences.Editor AdminEditor = AdminsharedPreferences.edit();
                        AdminEditor.putBoolean("notificationsPending", false);                                                      //Set the notificationsPending to false to avoid resending notifications
                        AdminEditor.apply();
                    }
                }
                Intent adminIntent = new Intent(MainActivity.this, ADMIN_dashboard.class);                            //Send the logged in User to ADMIN_Dashboard
                startActivity(adminIntent);

            }
            else{
                SharedPreferences EmployeesharedPreferences = getSharedPreferences("EmployeePreferences", MODE_PRIVATE);
                boolean EmployeeNotificationsEnabled = EmployeesharedPreferences.getBoolean("notificationsEnabled", true);

                if (!EmployeeNotificationsEnabled) {
                    Log.d("NOTIFICATIONS", "Notifications are disabled");
                }
                else{

                }


                Intent employeeIntent = new Intent(MainActivity.this, EMPLOYEE_dashboard.class);
                startActivity(employeeIntent);
            }
            finish();
        }
        else{
            Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }
    }


}