package com.example.comp2001_aistenphillips;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.mindrot.jbcrypt.BCrypt;

//LoginUsersHelper.java handles the database for the users. Its purpose is to
//manage hold the login information for each User of the App
public class LoginUsersHelper {

    //Structure the TABLE and COLUMNS for the database
    public static final String USERS_TABLE = "users";
    public static final String ID_COL = "id";
    public static final String ACCOUNT_TYPE_COL = "account_type";
    public static final String USERNAME_COL = "username";
    public static final String HASH_PASSWORD_COL = "password_hash";

    public static final String CREATE_USERS_TABLE =
            "CREATE TABLE " + USERS_TABLE + " (" +
                    ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ACCOUNT_TYPE_COL + " INTEGER NOT NULL, " +
                    USERNAME_COL + " TEXT NOT NULL UNIQUE, " +
                    HASH_PASSWORD_COL + " TEXT NOT NULL);";

    private final DatabaseHelper dbHelper;
    public LoginUsersHelper(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    //VALIDATE USER
    //This method is used to check if the inputted username and password are valid
    //and exist within the database. This is used to check login credentials
    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try{                                                                            //Query the USERS_TABLE for the password where the username matches
            cursor = db.query(USERS_TABLE, new String[]{HASH_PASSWORD_COL},
                    USERNAME_COL + " = ?", new String[]{username},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {                                                        // Check if we found the username
                String storedHash = cursor.getString(cursor.getColumnIndexOrThrow(HASH_PASSWORD_COL));           // Retrieve the stored hashed password
                return BCrypt.checkpw(password, storedHash);                                                     // Use BCrypt to compare the provided password with the stored password
            }
        }finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;       // If no matching user was found return false
    }

    //ADD user to database
    //This method gets an accountType, username and password and adds it to the database. This is used
    //when admins create an account.
    public boolean addUser(int accountType, String username, String passwordHash) {

        if(isUsernameTaken(username)){                          //Check to make sure Employee Email is unique
            return false;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();              //Open the database in Write mode
        ContentValues values = new ContentValues();                      //Creates a ContentValue object to hold the data that will be inserted into the TABLE
        values.put(ACCOUNT_TYPE_COL, accountType);
        values.put(USERNAME_COL, username);
        values.put(HASH_PASSWORD_COL, passwordHash);

        long result = db.insert(USERS_TABLE, null, values);
        if(result != -1){                                                                       // Check if the insert was successful
            HolidayAllowanceHelper holidayHelper = new HolidayAllowanceHelper(dbHelper);        //If the user was added to the USERS TABLE. Create a HolidayAllowanceHelper
            return holidayHelper.addHolidayAllowance(username);                                 //Add the Holiday Allowance for the new Employee

        } else {
            return false;
        }

    }

    //GET User Type
    //This method is used to get the Account Type of a User given their username (email). This method
    //is used when login in to see if it was an ADMIN or EMPLOYEE
    public int getUserType(String username){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(USERS_TABLE, new String[]{ACCOUNT_TYPE_COL},
                    USERNAME_COL + " =?", new String[]{username},
                    null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getInt(cursor.getColumnIndexOrThrow(ACCOUNT_TYPE_COL));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return -1;
    }

    //CHECK if a username already exists
    //This method takes in a username (email) and checks if it already exists in the database. This
    //method is used when creating ADMINS create a new Employee
    public boolean isUsernameTaken(String username){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.query(USERS_TABLE, new String[]{USERNAME_COL},
                    USERNAME_COL + " =?", new String[]{username},
                    null,null,null);
            return cursor != null && cursor.moveToFirst();
        }
        finally {
            if(cursor != null){
                cursor.close();
            }
        }
    }



}
