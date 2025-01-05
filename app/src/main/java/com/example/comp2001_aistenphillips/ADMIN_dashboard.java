package com.example.comp2001_aistenphillips;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//ADMIN DASHBOARD

public class ADMIN_dashboard extends AppCompatActivity {

    private ImageView employeeListBtn, addEmployeeBtn, holidayRequestsBtn, notificationsBtn;
    private Button logoutBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        //Set up all the buttons and their intents
        employeeListBtn.setOnClickListener(v->{
            Intent intent = new Intent(ADMIN_dashboard.this, ADMIN_employees.class);
            startActivity(intent);
        });

        addEmployeeBtn.setOnClickListener(v ->{
            Intent intent= new Intent(ADMIN_dashboard.this, ADMIN_employee_add.class);
            startActivity(intent);
        });

        holidayRequestsBtn.setOnClickListener(v ->{
            Intent intent= new Intent(ADMIN_dashboard.this, ADMIN_holiday_requests.class);
            startActivity(intent);
        });

        notificationsBtn.setOnClickListener(v->{
            Intent intent= new Intent(ADMIN_dashboard.this, ADMIN_notifications.class);
            startActivity(intent);
        });

        logoutBtn.setOnClickListener(v ->{
            Intent intent= new Intent(ADMIN_dashboard.this, MainActivity.class);
            startActivity(intent);
        });


    }

    private void init(){                                                              //Holds all the necessary variables and assigns them to objects in the XML file
        employeeListBtn = findViewById(R.id.ADMIN_employee_list_icon);
        addEmployeeBtn = findViewById(R.id.ADMIN_employee_add_icon);
        holidayRequestsBtn = findViewById(R.id.ADMIN_holiday_request_icon);
        notificationsBtn = findViewById(R.id.ADMIN_notifications_icon);
        logoutBtn = findViewById(R.id.logout_btn);
    }

}