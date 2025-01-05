package com.example.comp2001_aistenphillips;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Company.db";                           //Name of the database
    private static final int DATABASE_VERSION = 1;                                      //Version of the database: Change when the schema for the database changes


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {                                           //Called when the database is created. Creates the tables for the database
        db.execSQL(LoginUsersHelper.CREATE_USERS_TABLE);
        db.execSQL(EmployeeRequestHelper.CREATE_REQUESTS_TABLE);
        db.execSQL(HolidayAllowanceHelper.CREATE_HOLIDAY_ALLOWANCE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {          //Called when the database is upgraded. Drops the tables and creates new ones
        db.execSQL("DROP TABLE IF EXISTS " + LoginUsersHelper.USERS_TABLE);             //This is called if the database version changes
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + EmployeeRequestHelper.REQUESTS_TABLE);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + HolidayAllowanceHelper.ALLOWANCE_TABLE);
        onCreate(db);
    }

}
