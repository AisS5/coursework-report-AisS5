package com.example.comp2001_aistenphillips;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.android.material.textfield.TextInputEditText;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class EMPLOYEE_holiday_request extends AppCompatActivity {

    private ImageView cancelBtn, saveBtn;
    private TextInputEditText startDatePicker, endDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_holiday_request);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        //CANCEL BUTTON
        cancelBtn.setOnClickListener(v->{
            Intent intent = new Intent(EMPLOYEE_holiday_request.this, EMPLOYEE_dashboard.class);
            startActivity(intent);
        });

        startDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();                          //Creates a Calendar object to get the current date
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(           //Creates a DatePickerDialog object to show the date picker dialog
                        EMPLOYEE_holiday_request.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                startDatePicker.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);         //Sets the text of the StartDate to the selected date
                            }
                        },
                        year, month, day);
                datePickerDialog.show();        //Shows the date picker dialog
            }
        });
        endDatePicker.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(

                        EMPLOYEE_holiday_request.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                endDatePicker.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });

        //SAVE BUTTON
        saveBtn.setOnClickListener(v ->{
            String start = startDatePicker.getText().toString();        //Gets the text from the StartDate and EndDate
            String end = endDatePicker.getText().toString();

            String username = getLoggedInUsername();                    //Gets the username of the currently logged in user
            if (username == null) {
                Toast.makeText(EMPLOYEE_holiday_request.this, "No user logged in", Toast.LENGTH_SHORT).show();
                return;
            }

            //HELPERS
            HolidayAllowanceHelper holidayHelper = new HolidayAllowanceHelper(new DatabaseHelper(this));
            EmployeeRequestHelper employeeHelper = new EmployeeRequestHelper(new DatabaseHelper(this));

            int requestedDays = calculateRequestedDays(start, end);                                                     //Calls the calculateRequestedDays method
            int remainingDays = holidayHelper.getRemainingDays(username);                                               //Calls the getRemainingDays method

            if (remainingDays >= requestedDays) {
                boolean requestSubmitted = employeeHelper.addHolidayRequest(username, start, end, requestedDays);               //Calls the addHolidayRequest method
                if (requestSubmitted) {
                    boolean allowanceUpdated = holidayHelper.updateRemainingDays(username, remainingDays - requestedDays);      //If requestSubmitted is true, calls the updateRemainingDays method
                    if (allowanceUpdated) {
                        SharedPreferences sharedPreferences = getSharedPreferences("AdminPreferences", MODE_PRIVATE);                    //If allowanceUpdated is true, updates the shared preferences
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("notificationsPending", true);                                                                       //Sets the notificationsPending to true
                        editor.apply();

                        Toast.makeText(EMPLOYEE_holiday_request.this, "Holiday request submitted successfully.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EMPLOYEE_holiday_request.this, EMPLOYEE_dashboard.class);
                        startActivity(intent);

                    } else {
                        Toast.makeText(EMPLOYEE_holiday_request.this, "Error updating holiday allowance.", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(EMPLOYEE_holiday_request.this, "Failed to submit holiday request.", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(EMPLOYEE_holiday_request.this, "Not enough allowance.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Holds all the necessary variables and assigns them to objects in the XML file
    private void init(){
        startDatePicker = findViewById(R.id.start_date_picker);
        endDatePicker = findViewById(R.id.end_date_picker);
        saveBtn = findViewById(R.id.save_btn);
        cancelBtn = findViewById(R.id.cancel_btn);
    }

    //CALCULATE REQUESTED DAYS
    //This method calculates the number of days between the start and end dates of the holiday request
    private int calculateRequestedDays(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());     //Create a SimpleDateFormat object to parse the dates
        try {
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            long difference = end.getTime() - start.getTime();                                      //Calculates the difference between the start and end dates in milliseconds
            return (int) TimeUnit.MILLISECONDS.toDays(difference) + 1;                              //Returns the number of days between the start and end dates + 1
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    //GET LOGGED IN USERNAME
    //This method retrieves the username of the currently logged in user from shared preferences
    private String getLoggedInUsername() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", MODE_PRIVATE);
        return sharedPreferences.getString("username", null);
    }
}