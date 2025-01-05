package com.example.comp2001_aistenphillips;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PendingRequestAdapter extends RecyclerView.Adapter<PendingRequestAdapter.ViewHolder> {
    private Context context;
    private List<HolidayRequest> requests;
    private EmployeeRequestHelper employeeRequestsHelper;

    //Constructor to initialise the adapter with context and list of requests
    public PendingRequestAdapter(Context context, List<HolidayRequest> requests) {
        this.context = context;
        this.requests = requests;
        this.employeeRequestsHelper = new EmployeeRequestHelper(new DatabaseHelper(context));
    }

    //ViewHolder class to hold the views for each item in the RecyclerView
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pending_request_item, parent, false); //Inflate the layout for each item in the RecyclerView. This layout is defined in pending_request_item.xml
        return new ViewHolder(view);                                                                                //Create a new ViewHolder with the inflated view
    }

    //Binds the data to the views in the ViewHolder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HolidayRequest request = requests.get(position);                            //Get the holiday request for the given position

                                                                                    //Set the values in the ViewHolder
        holder.usernameTextView.setText(request.getEmployeeEmail());
        holder.startDateTextView.setText(request.getStartDate());
        holder.endDateTextView.setText(request.getEndDate());
        holder.statusTextView.setText(request.getStatus());

        //APPROVE BUTTON
        holder.approveButton.setOnClickListener(v -> {
            if (employeeRequestsHelper.updateHolidayRequestStatus(request.getId(), "approved")) {     //Update the request status in the database using the helper
                requests.remove(holder.getAdapterPosition());                                               //Remove the request from the list
                notifyItemRemoved(holder.getAdapterPosition());                                             //Notify the adapter that the item has been removed
            }
        });

        //DECLINE BUTTON
        holder.declineButton.setOnClickListener(v -> {
                                                                    //Get data from the request
            String employeeEmail = request.getEmployeeEmail();
            String startDate = request.getStartDate();
            String endDate = request.getEndDate();

            int requestedDays = calculateRequestedDays(startDate, endDate);
            HolidayAllowanceHelper holidayHelper = new HolidayAllowanceHelper(new DatabaseHelper(context));                                     //Create a HolidayHelper
            int remainingDays = holidayHelper.getRemainingDays(employeeEmail);                                                                  //Call getRemainingDays method using HolidayHelper to get the remaining days for the employee
            boolean allowanceUpdated = holidayHelper.updateRemainingDays(employeeEmail, remainingDays + requestedDays);         //Call updateRemainingDays method to restore request days

            if (allowanceUpdated) {
                if (employeeRequestsHelper.updateHolidayRequestStatus(request.getId(), "declined")) {   //Update the request status in the database using the helper
                    requests.remove(holder.getAdapterPosition());                                              //Remove the request from the list
                    notifyItemRemoved(holder.getAdapterPosition());                                           //Notify the adapter that the item has been removed
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();   //Returns the number of items in the list
    }

    //This class is used to store the views for each item in the RecyclerView
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView, startDateTextView, endDateTextView, statusTextView;
        Button approveButton, declineButton;
        public ViewHolder(View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.userEmail);
            startDateTextView = itemView.findViewById(R.id.StartDate);
            endDateTextView = itemView.findViewById(R.id.EndDate);
            statusTextView = itemView.findViewById(R.id.Status);
            approveButton = itemView.findViewById(R.id.approveButton);
            declineButton = itemView.findViewById(R.id.declineButton);
        }
    }

    //This method is used to calculate the number of days between the start and end date of the holiday request
    private int calculateRequestedDays(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());     //Creates a SimpleDateFormat object to parse the dates
        try {
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            long difference = end.getTime() - start.getTime();              //Calculates the difference between the start and end dates in milliseconds
            return (int) TimeUnit.MILLISECONDS.toDays(difference) + 1;      //Returns the number of days between the start and end dates + 1
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
