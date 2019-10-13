package com.example.nicolemorris.lifestyle.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "lifestyle.dp";
    public static final String PROFILE_TABLE = "profile_table";
    public static final List<String> PROFILE_TABLE_COLS = new ArrayList<>(Arrays.asList("NAME","AGE","CITY",
            "STATE", "FEET", "INCHES","WEIGHT", "SEX"));

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+PROFILE_TABLE+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME, AGE INTEGER, " +
                "CITY, STATE, FEET INTEGRE, INCHES INTEGER, WEIGHT, SEX )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+PROFILE_TABLE);
        onCreate(db);
    }

    public boolean insertData(String name, int age, String city, String state, int feet, int inches, String weight, String sex){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put(PROFILE_TABLE_COLS.get(0), name);
            contentValues.put(PROFILE_TABLE_COLS.get(1), age);
            contentValues.put(PROFILE_TABLE_COLS.get(2), city);
            contentValues.put(PROFILE_TABLE_COLS.get(3), state);
            contentValues.put(PROFILE_TABLE_COLS.get(4), feet);
            contentValues.put(PROFILE_TABLE_COLS.get(5), inches);
            contentValues.put(PROFILE_TABLE_COLS.get(6), weight);
            contentValues.put(PROFILE_TABLE_COLS.get(7), sex);
            long res = db.insert(PROFILE_TABLE, null, contentValues);
            return res == -1 ? false : true;
        }catch(Exception e){
            return false;
        }
    }

    /**
     * Update user profile by id
     * @param id
     * @param name
     * @param age
     * @param city
     * @param state
     * @param weight
     * @param sex
     * @return
     */
    public boolean updateDate(int id, String name, int age, String city, String state, int feet, int inches, String weight, String sex){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try{
            contentValues.put(PROFILE_TABLE_COLS.get(0), name);
            contentValues.put(PROFILE_TABLE_COLS.get(1), age);
            contentValues.put(PROFILE_TABLE_COLS.get(2), city);
            contentValues.put(PROFILE_TABLE_COLS.get(3), state);
            contentValues.put(PROFILE_TABLE_COLS.get(4), feet);
            contentValues.put(PROFILE_TABLE_COLS.get(5), inches);
            contentValues.put(PROFILE_TABLE_COLS.get(6), weight);
            contentValues.put(PROFILE_TABLE_COLS.get(7), sex);
            long res = db.update(PROFILE_TABLE,contentValues, "id="+id, null);
            return res == -1?false:true;
        }catch(Exception e){
            return false;
        }
    }

    /**
     * Update user profile by name (temporarily used before login feature created)
     * @param name
     * @param age
     * @param city
     * @param state
     * @param weight
     * @param sex
     * @return
     */
    public boolean updateDate(String name, int age, String city, String state, int feet, int inches, String weight, String sex){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        try{
            contentValues.put(PROFILE_TABLE_COLS.get(0), name);
            contentValues.put(PROFILE_TABLE_COLS.get(1), age);
            contentValues.put(PROFILE_TABLE_COLS.get(2), city);
            contentValues.put(PROFILE_TABLE_COLS.get(3), state);
            contentValues.put(PROFILE_TABLE_COLS.get(4), feet);
            contentValues.put(PROFILE_TABLE_COLS.get(5), inches);
            contentValues.put(PROFILE_TABLE_COLS.get(6), weight);
            contentValues.put(PROFILE_TABLE_COLS.get(7), sex);
            long res = db.update(PROFILE_TABLE,contentValues, "name="+name, null);
            return res == -1?false:true;
        }catch(Exception e){
            return false;
        }
    }





}
