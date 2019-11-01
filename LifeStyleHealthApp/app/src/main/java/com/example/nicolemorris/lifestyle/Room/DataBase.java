package com.example.nicolemorris.lifestyle.Room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {WeatherDataTable.class, UserTable.class}, version = 2, exportSchema = false)
public abstract class DataBase extends RoomDatabase {

    private static final String DATABASE_NAME = "LifeStyleDB";

    private static DataBase dbInstance;

    public static DataBase getInstance(Context context) {
        if (dbInstance == null) {
            synchronized (DataBase.class) {
                if (dbInstance == null) {
                    dbInstance = Room.databaseBuilder(context.getApplicationContext(), DataBase.class, DATABASE_NAME).addCallback(sOnOpenCallback).fallbackToDestructiveMigration().build();
                }
            }

        }

        return dbInstance;
    }

    private static RoomDatabase.Callback sOnOpenCallback =
            new RoomDatabase.Callback() {
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                }
            };


    public abstract WeatherDao weatherDao();

    public abstract  UserDao userDao();

}
