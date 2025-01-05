package com.example.comp2001_aistenphillips;
public class HolidayRequest {
    private int id;
    private String employeeEmail;
    private String startDate;
    private String endDate;
    private String status;

    public HolidayRequest(int id, String employeeEmail, String startDate, String endDate, String status) {      //This is used to create a new HolidayRequest object after fetching data from the database
        this.id = id;
        this.employeeEmail = employeeEmail;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
