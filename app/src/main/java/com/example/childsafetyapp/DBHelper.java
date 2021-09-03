package com.example.childsafetyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public static final String TAG = "DBHelper";
    public static final String SERVER_URL="http://192.168.137.2/childsafetysync/syncinfo.php";

    //columns of the users table

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_USER_ID = "_id";
    public static final String COLUMN_USER_NAME = "username";
    public static final String COLUMN_USER_PASSWORD = "password";
    public static final String COLUMN_EMAIL_ADDRESS = "email";

    //column for helplines

    public static final String TABLE_HELPLINES = "helplines";
    public static final String COLUMN_HELPLINE_ID = "_id";
    public static final String COLUMN_HELPLINE_NAME = "orgname";
    public static final String COLUMN_HELPLINE_NUMBER = "orgnumber";
    public static final String COLUMN_HELPLINE_EMAIL = "orgemail";

    // columns of the organisations table
    public static final String TABLE_ORGANISATIONS = "organisations";
    public static final String COLUMN_ORGANISATION_ID = "_id";
    public static final String COLUMN_ORGANISATION_NAME = "organisation_name";
    public static final String COLUMN_ORGANISATION_ADDRESS = "address";
    public static final String COLUMN_ORGANISATION_WEBSITE = "website";
    public static final String COLUMN_ORGANISATION_PHONE_NUMBER = "phone_number";

    // columns of the childrens table
    public static final String TABLE_CHILDREN = "childrens";
    public static final String COLUMN_CHILD_ID = COLUMN_ORGANISATION_ID;
    public static final String COLUMN_CHILD_FIRST_NAME = "first_name";
    public static final String COLUMN_CHILD_LAST_NAME = "last_name";
    public static final String COLUMN_CHILD_ADDRESS = COLUMN_ORGANISATION_ADDRESS;
    public static final String COLUMN_CHILD_EMAIL = "email";
    public static final String COLUMN_CHILD_PHONE_NUMBER = COLUMN_ORGANISATION_PHONE_NUMBER;
    public static final String COLUMN_CHILD_SALARY = "date";
    public static final String COLUMN_CHILD_ORGANISATION_ID = "organisation_id";

    private static final String DATABASE_NAME = "serviceproviderdetails.db";
    private static final int DATABASE_VERSION = 1;

    // SQL statement of the child table creation
    private static final String SQL_CREATE_TABLE_EMPLOYEES = "CREATE TABLE " + TABLE_CHILDREN + "("
            + COLUMN_CHILD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_CHILD_FIRST_NAME + " TEXT NOT NULL, "
            + COLUMN_CHILD_LAST_NAME + " TEXT NOT NULL, "
            + COLUMN_CHILD_ADDRESS + " TEXT NOT NULL, "
            + COLUMN_CHILD_EMAIL + " TEXT NOT NULL, "
            + COLUMN_CHILD_PHONE_NUMBER + " TEXT NOT NULL, "
            + COLUMN_CHILD_SALARY + " REAL NOT NULL, "
            + COLUMN_CHILD_ORGANISATION_ID + " INTEGER NOT NULL "
            + ");";

    // SQL statement of the organisation table creation
    private static final String SQL_CREATE_TABLE_COMPANIES = "CREATE TABLE " + TABLE_ORGANISATIONS + "("
            + COLUMN_ORGANISATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_ORGANISATION_NAME + " TEXT NOT NULL, "
            + COLUMN_ORGANISATION_ADDRESS + " TEXT NOT NULL, "
            + COLUMN_ORGANISATION_WEBSITE + " TEXT NOT NULL, "
            + COLUMN_ORGANISATION_PHONE_NUMBER + " TEXT NOT NULL "
            + ");";

    private static final String SQL_CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_USERS + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_USER_NAME + " TEXT NOT NULL, "
            + COLUMN_USER_PASSWORD + " TEXT NOT NULL, "
            + COLUMN_EMAIL_ADDRESS + " TEXT NOT NULL "
            + ");";

    private static final String SQL_CREATE_TABLE_HELPLINES = "CREATE TABLE " + TABLE_HELPLINES + "("
            + COLUMN_HELPLINE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_HELPLINE_NAME + " TEXT NOT NULL, "
            + COLUMN_HELPLINE_NUMBER + " TEXT NOT NULL, "
            + COLUMN_HELPLINE_EMAIL + " TEXT NOT NULL "
            + ");";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(SQL_CREATE_TABLE_COMPANIES);
        database.execSQL(SQL_CREATE_TABLE_EMPLOYEES);
        database.execSQL(SQL_CREATE_TABLE_USERS);
        database.execSQL(SQL_CREATE_TABLE_HELPLINES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG,
                "Upgrading the database from version " + oldVersion + " to " + newVersion);
        // clear all data
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CHILDREN);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORGANISATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HELPLINES);

        // recreate the tables
        onCreate(db);
    }

    public DBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public boolean insertdata(String username, String password, String email){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values2= new ContentValues();
        values2.put("username", username);
        values2.put("password", password);
        values2.put("email", email);
        long  result = db.insert(TABLE_USERS, null, values2);
        if(result==-1){
            return false;
        }

        else {
            return true;
        }
    }
    public boolean checkusernamepassword(String username, String password){

        SQLiteDatabase MYDB = this.getWritableDatabase();
        Cursor cursor = MYDB.rawQuery("select * from users where username = ? and password = ?", new String[] {username, password} );
        if (cursor.getCount()>0)

            return  true;
        else
            return false;
    }

    public boolean checkusername(String username){

        SQLiteDatabase MYDB = this.getWritableDatabase();
        //Cursor cursor = MYDB.rawQuery("Select * from users where username= ?", new String[] {username});
        Cursor cursor = MYDB.rawQuery("select * from users where username = ?", new String[] {username} );
        if (cursor.getCount()>0)

            return  true;
        else return  false;

    }

    public boolean inserthelplinedata(String orgname, String orgnumber, String orgemail){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values= new ContentValues();
        values.put("orgname", orgname);
        values.put("orgnumber", orgnumber);
        values.put("orgemail", orgemail);
        long  result = db.insert(TABLE_HELPLINES, null, values);
        if(result==-1){
            return false;
        }

        else {
            return true;
        }
    }
    public Cursor getData(){

        SQLiteDatabase  DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select * from helplines",null);
        return cursor;
    }
}
