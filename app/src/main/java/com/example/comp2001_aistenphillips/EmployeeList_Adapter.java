package com.example.comp2001_aistenphillips;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class EmployeeList_Adapter  extends RecyclerView.Adapter<EmployeeList_Adapter.EmployeeViewHolder>  {

    private List<Employee> employeeList;
    private List<Employee> fullemployeeList;
    private RecyclerViewClickListener listener;


    //Constructor to initialise the adapter with a list of employees and click listener
    public EmployeeList_Adapter(List<Employee> employeeList, RecyclerViewClickListener listener){
        if (employeeList == null){
            this.employeeList = new ArrayList<>();      //If employeeList is null, create an empty list
        }else{
            this.employeeList = employeeList;           //Else, use the already created list
        }
        this.fullemployeeList = new ArrayList<>(this.employeeList);     //Populate fullEmployeeList with the same data as employeeList
        this.listener = listener;                                       //Set the listener to handle item clicks
    }

    //Getter to return the full employee list
    public List<Employee> getFullemployeeList(){
        return fullemployeeList;
    }


    public void setEmployeeList(List<Employee> employees){
        this.fullemployeeList = new ArrayList<>(employees);
        this.employeeList = new ArrayList<>(employees);
        notifyDataSetChanged();
    }

    //Method to reset the employee list
    public void resetEmployeeList() {
        this.employeeList = new ArrayList<>(this.fullemployeeList);     //Reset the employeeList using the fullEmployeeList
        notifyDataSetChanged();                                         //Notify the RecyclerView of the data reset
    }

    //Method to filter the employee list based on a query
    public void filterEmployeeList(String query) {
        List<Employee> filteredList = new ArrayList<>();        //Create a new list to hold the filtered employees
        if (query.isEmpty()) {
            filteredList.addAll(this.fullemployeeList);         //If the query is empty, add all employees to the filtered list
        } else {
            for (Employee employee : fullemployeeList) {                    //Iterate through the fullEmployeeList
                if (String.valueOf(employee.getId()).contains(query)) {     //If the employee ID is queried, add it to the filtered list
                    filteredList.add(employee);
                }
            }
        }
        this.employeeList = filteredList;           //Update the filtered list with the new filtered data
        notifyDataSetChanged();                     //Notify the RecyclerView of the data change
    }


    public  class EmployeeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView employeeId, employeeName;                                                                  //this is where we would create all the TextViews we want to display for each item
        public EmployeeViewHolder(View itemView) {
            super(itemView);                                                                                 //passes itemView into RecycleView.ViewHolder
                                                                                                             //gets the data that is needed for the current item. The Views are stored in a layout xml (employee_list)
            employeeId = itemView.findViewById(R.id.employeeId);
            employeeName = itemView.findViewById(R.id.employeeName);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            listener.onClick(view, getAdapterPosition());
        }
    }

    @Override                                                                      //Use Override to provide custom implementation for creating new ViewHolder objects
    public EmployeeViewHolder onCreateViewHolder(ViewGroup parent, int viewType){                       //returns EmployeeViewHolder which is a custom implementation
                                                                                                        //onCreateViewHolder - called to create a new ViewHolder to display items
                                                                                                        //ViewGroup - the view that the new objects will be attached to (The recycleView)
                                                                                                        //int viewType - useful if I want to have multiple view types in the same RecycleView

        View EmployeeView = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_list_item, parent, false);            //View EmployeeView - reference for new layout
                                                                                                                                                //LayoutInflater - class used to convert XML layout into a View Object
                                                                                                                                                //from(parent.getContext()) - provides the context of the recycleView or the activity
                                                                                                                                                //inflate(R.layout.employees_list, parent, false) - provides the UI structure, the recycleView to attach the UI,
                                                                                                                                                // and false(not be immediately attached)
        return new EmployeeViewHolder(EmployeeView);
    }

    @Override                                                                           //Use Override to define how data from employeeList is bound to the ViewHolder
    public void onBindViewHolder(EmployeeViewHolder holder, int position) {                             //onBindViewHolder - to bind data to the ViewHolder for a specific position in the list
                                                                                                        //EmployeeViewHolder - the holder being referenced
                                                                                                        //int position - represents the index of the item that is currently being displayed in the recycleView

        Employee CurrentEmployee = employeeList.get(position);                                          //CurrentEmployee -  holds the data for the displayed item
                                                                                                        //employeeList.get(position) - gets the current displayed item

        holder.employeeId.setText(String.valueOf(CurrentEmployee.getId()));
        holder.employeeName.setText(CurrentEmployee.getFirstname() + " " + CurrentEmployee.getLastname());          //this is where you would get all the data to display. This ensures the RecyclerView displays the correct data for each item in the list.
    }


    @Override                                                                //Use Override to get a customised list count. This will mean we can get the exact amount required
    public int getItemCount() {
        return employeeList != null ? employeeList.size() : 0;              //employeeList != null -  This checks if the employeeList != null.
    }                                                                       // ? employeeList.size() : 0 - returns size of employee list or 0 if null

    public void updateEmployeeList(List<Employee> newEmployeeList) {        //This is used to replace the existing employeeList (If API gets updated)
        this.employeeList = newEmployeeList;
        this.fullemployeeList = new ArrayList<>(newEmployeeList);
        notifyDataSetChanged();                                             //Notify the RecycleView that employeeList has changed
    }
    public interface RecyclerViewClickListener{
        void onClick(View view, int position);
    }
}
