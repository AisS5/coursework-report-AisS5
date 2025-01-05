package com.example.comp2001_aistenphillips;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.ImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ADMIN_holiday_requests extends AppCompatActivity {

    private ImageView backBtn;
    private RecyclerView requestsRecyclerView;
    private PendingRequestAdapter requestAdapter;
    private List<HolidayRequest> pendingRequests;
    private EmployeeRequestHelper employeeRequestsHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_holiday_requests);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        init();

        backBtn.setOnClickListener(v->{
            Intent intent = new Intent(ADMIN_holiday_requests.this, ADMIN_dashboard.class);
            startActivity(intent);
        });


        employeeRequestsHelper = new EmployeeRequestHelper(new DatabaseHelper(this));       //Create Helper
        pendingRequests = employeeRequestsHelper.getPendingRequests();                             //Get all pending requests from the database using the Helper

        requestAdapter = new PendingRequestAdapter(this, pendingRequests);                          //Create Adapter with the pending requests
        requestsRecyclerView.setLayoutManager(new LinearLayoutManager(this));               //Set the layout manager for the RecyclerView
        requestsRecyclerView.setAdapter(requestAdapter);                                            //Set the adapter for the RecyclerView to display the pending requests



    }

    //Holds all the necessary variables and assigns them to objects in the XML file
    private void init(){
        requestsRecyclerView = findViewById(R.id.request_recycler_view);
        backBtn = findViewById(R.id.back_btn);
    }



}