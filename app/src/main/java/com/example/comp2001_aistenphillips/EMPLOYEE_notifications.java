package com.example.comp2001_aistenphillips;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EMPLOYEE_notifications extends AppCompatActivity {

    private Button NotificationButton;
    private SharedPreferences sharedPreferences;
    private boolean notificationsEnabled;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_notifications);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();

        notificationsEnabled = sharedPreferences.getBoolean("notificationsEnabled", true);          //Get the notifications status from the shared preferences
        updateButtonText(notificationsEnabled);                                                                  //Call the updateButtonText method

        NotificationButton.setOnClickListener(v -> {
            notificationsEnabled = !notificationsEnabled;                                               //Toggle the notifications status
            SharedPreferences.Editor editor = sharedPreferences.edit();                                 //Edit the shared preferences to save the notifications status
            editor.putBoolean("notificationsEnabled", notificationsEnabled);                            //Save the notifications status in the shared preferences
            editor.apply();

            updateButtonText(notificationsEnabled);                                      //Call the updateButtonText method

            Intent intent = new Intent(EMPLOYEE_notifications.this, EMPLOYEE_dashboard.class);
            startActivity(intent);
        });
    }

    //Holds all the necessary variables and assigns them to objects in the XML file
    private void init(){
        NotificationButton = findViewById(R.id.NotificationButton);
        sharedPreferences = getSharedPreferences("EmployeePreferences", MODE_PRIVATE);
    }

    //UPDATE TEXT
    //This method is used to update the text of the button based on the if notifications are on or off
    private void updateButtonText(boolean notificationsEnabled) {
        if (notificationsEnabled) {
            NotificationButton.setText("Turn Off Notifications");
        } else {
            NotificationButton.setText("Turn On Notifications");
        }
    }
}