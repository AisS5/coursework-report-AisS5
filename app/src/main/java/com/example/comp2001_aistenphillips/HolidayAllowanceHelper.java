package com.example.comp2001_aistenphillips;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.concurrent.TimeUnit;


//HolidayAllowanceHelper.java  handles the database for the holiday requests. Its purpose is to
//keep a track of each Employees holiday allowance
public class HolidayAllowanceHelper {

    //Structure the TABLE and COLUMNS for the database
    public static final String ALLOWANCE_TABLE = "Holiday_Allowance";
    public static final String ID_COL = "id";
    public static final String EMAIL_COL = "user_email";
    public static final String REMAINING_DAYS_COL = "allowance";
    public static final String YEAR_COL = "year";

    public static final String CREATE_HOLIDAY_ALLOWANCE_TABLE =
            "CREATE TABLE " + ALLOWANCE_TABLE + " (" +
                    ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    EMAIL_COL + " TEXT NOT NULL, " +
                    REMAINING_DAYS_COL + " INTEGER NOT NULL, " +
                    YEAR_COL + " INTEGER NOT NULL, " +
                    "FOREIGN KEY(" + EMAIL_COL + ") REFERENCES " + LoginUsersHelper.USERS_TABLE + "(username));";


    private final DatabaseHelper dbHelper;
    public HolidayAllowanceHelper(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }


    //ADD holiday allowance
    //This method is used to create an allowance for the Employee. Its used when ADMINS creating a new Employee
    public boolean addHolidayAllowance(String email) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();                 //Open the database in Write mode
        ContentValues values = new ContentValues();                         //Creates a ContentValue object to hold the data that will be inserted into the TABLE
        values.put(EMAIL_COL, email);
        values.put(REMAINING_DAYS_COL, 30);
        values.put(YEAR_COL, 1);

        long result = db.insert(ALLOWANCE_TABLE, null, values);     //Inserts the values into the ALLOWANCE_TABLE. Result is the ID of the row inserted
        return result != -1;                                                        //If result is not defaulted to -1 then return true. Else return false
    }

    //GET remaining days
    //This method is used to get the remaining days of holiday allowance for the given Employee email
    public int getRemainingDays(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(ALLOWANCE_TABLE, new String[]{REMAINING_DAYS_COL},                     //Go through the ALLOWANCE_TABLE and get the remaining days for the given email
                EMAIL_COL + " = ?", new String[]{email},
                null, null, null);

        if (cursor.moveToFirst()) {                                                                     // Check if the query returned at least one result
            int remainingDays = cursor.getInt(cursor.getColumnIndexOrThrow(REMAINING_DAYS_COL));        // Retrieve the value of REMAINING_DAYS_COL from the current row
            cursor.close();
            return remainingDays;
        }
        cursor.close();         //If no matching row was found. Return 0
        return 0;
    }

    //UPDATE remaining days
    //Method used to update the remaining days for the given Employee
    public boolean updateRemainingDays(String email, int newRemainingDays) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(REMAINING_DAYS_COL, newRemainingDays);

        int rowsUpdated = db.update(ALLOWANCE_TABLE, values,
                EMAIL_COL + " = ?", new String[]{email});

        return rowsUpdated > 0;
    }






}

