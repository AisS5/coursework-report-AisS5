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

//EMPLOYEE DASHBOARD
public class EMPLOYEE_dashboard extends AppCompatActivity {
    private ImageView myInfoBtn, holidayBookBtn, notificationsBtn;
    private Button logoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_employee_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        //Set up all the buttons and their intents
        myInfoBtn.setOnClickListener(v->{
            Intent intent = new Intent(EMPLOYEE_dashboard.this, EMPLOYEE_my_info.class);
            startActivity(intent);
        });

        holidayBookBtn.setOnClickListener(v ->{
            Intent intent= new Intent(EMPLOYEE_dashboard.this, EMPLOYEE_holiday_request.class);
            startActivity(intent);
        });

        notificationsBtn.setOnClickListener(v->{
            Intent intent= new Intent(EMPLOYEE_dashboard.this, EMPLOYEE_notifications.class);
            startActivity(intent);
        });

        logoutBtn.setOnClickListener(v ->{
            Intent intent= new Intent(EMPLOYEE_dashboard.this, MainActivity.class);
            startActivity(intent);
        });

    }

    //Holds all the necessary variables and assigns them to objects in the XML file
    private void init(){
        myInfoBtn = findViewById(R.id.EMPLOYEE_myInfo_icon);
        holidayBookBtn = findViewById(R.id.EMPLOYEE_holiday_icon);
        notificationsBtn = findViewById(R.id.EMPLOYEE_notifications_icon);
        logoutBtn = findViewById(R.id.logout_btn);
    }
}