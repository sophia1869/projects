package com.example.nicolemorris.lifestyle.Room;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface WeatherDao {

    @Insert
    void insertWeatherDataTable(WeatherDataTable wdt);

    @Delete
    void deleteWeatherDataTable(WeatherDataTable wdt);

    @Update
    void updateWeatherDataTable(WeatherDataTable wdt);


    @Query("select * from WeatherDataTable" )
    List<WeatherDataTable> getAll();

    @Query("delete from WeatherDataTable")
    void deleteAll();


}
