package com.example.comp2001_aistenphillips;

import java.util.List;
import java.util.ArrayList;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


//EmployeeRequestHelper.java handles the database for the holiday requests. Its purpose is to
//manage holiday requests

public class EmployeeRequestHelper {


    //Structure the TABLE and COLUMNS for the database
    public static final String REQUESTS_TABLE = "Requests";
    public static final String ID_COL = "id";
    public static final String EMPLOYEE_EMAIL_COL = "employee_email";
    public static final String START_DATE_COL = "start_Date";
    public static final String END_DATE_COL = "End_Date";
    public static final String STATUS_COL = "status";

    public static final String CREATE_REQUESTS_TABLE =
            "CREATE TABLE " + REQUESTS_TABLE + " (" +
                    ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    EMPLOYEE_EMAIL_COL + " TEXT NOT NULL, " +
                    START_DATE_COL + " TEXT NOT NULL, " +
                    END_DATE_COL + " TEXT NOT NULL, " +
                    STATUS_COL + " TEXT NOT NULL DEFAULT 'pending');";



    private DatabaseHelper dbHelper;
    public EmployeeRequestHelper(DatabaseHelper dbHelper){
        this.dbHelper = dbHelper;
    }


    //ADD a holiday request
    public boolean addHolidayRequest(String email, String startDate, String endDate, int requestedDays) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();                                     //Open the database in Write mode
        HolidayAllowanceHelper holidayHelper = new HolidayAllowanceHelper(dbHelper);            //Call for the HolidayAllowanceHelper: This is used to get methods from the HolidayAllowanceHelper
        int remainingDays = holidayHelper.getRemainingDays(email);                              //Get the remainingDays by calling the getRemainingDays method and passing in the given email

        if (remainingDays < requestedDays) {                         //Check to make sure the Employee has enough days to request
            return false;
        }

        ContentValues requestValues = new ContentValues();              //Creates a ContentValue object to hold the data for the new holiday request
        requestValues.put(EMPLOYEE_EMAIL_COL, email);
        requestValues.put(START_DATE_COL, startDate);
        requestValues.put(END_DATE_COL, endDate);
        requestValues.put(STATUS_COL, "pending");

        long requestId = db.insert(REQUESTS_TABLE, null, requestValues);           //Inserts the requestValues into the REQUESTS_TABLE
        if (requestId != -1) {
            holidayHelper.updateRemainingDays(email, remainingDays - requestedDays);     //If request successful, update the remaining request days of the Employee
            return true;                                                                                 //Return True
        }
        return false;                                                                                   //If request failed. Then return false
    }

    //UPDATE holiday request status
    public boolean updateHolidayRequestStatus(int requestId, String status) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STATUS_COL, status);
        int rowsUpdated = db.update(REQUESTS_TABLE, values, ID_COL + " = ?", new String[]{String.valueOf(requestId)});      //Calls the update method to change the status of the row where the ID matches the given ID
        return rowsUpdated > 0;                              //If one or more rows are updated, return true. Else return false
    }


    //GET pending requests
    public List<HolidayRequest> getPendingRequests() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<HolidayRequest> requests = new ArrayList<>();                  //Creates a list of HolidayRequest objects

        Cursor cursor = db.query(REQUESTS_TABLE, null, STATUS_COL + " = ?", new String[]{"pending"}, null, null, null);     //Retrieves all Rows where the status is pending

        if (cursor != null) {                                                                       //Go through each row
            while (cursor.moveToNext()) {                                                           //Gets all the data from the row
                int id = cursor.getInt(cursor.getColumnIndex(ID_COL));
                String email = cursor.getString(cursor.getColumnIndex(EMPLOYEE_EMAIL_COL));
                String startDate = cursor.getString(cursor.getColumnIndex(START_DATE_COL));
                String endDate = cursor.getString(cursor.getColumnIndex(END_DATE_COL));
                String status = cursor.getString(cursor.getColumnIndex(STATUS_COL));
                requests.add(new HolidayRequest(id, email, startDate, endDate, status));            //Creates a new HolidayRequest object and adds it to the list
            }
            cursor.close();
        }
        return requests;                    //Returns the list of pending holiday requests
    }







}
