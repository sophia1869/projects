package com.example.nicolemorris.lifestyle.Room;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="WeatherDataTable")
public class WeatherDataTable {

    @PrimaryKey(autoGenerate = true)
    public int did;

    @NonNull
    @ColumnInfo(name = "location")
    private String location;

    @NonNull
    @ColumnInfo(name = "weatherdata")
    private String weatherJson;

    public WeatherDataTable(@NonNull String location, @NonNull String weatherJson){
        this.location = location;
        this.weatherJson = weatherJson;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public void setWeatherJson(String weatherdata){
        this.weatherJson = weatherdata;
    }

    public String getLocation(){
        return location;
    }

    public String getWeatherJson(){
        return weatherJson;
    }


}
