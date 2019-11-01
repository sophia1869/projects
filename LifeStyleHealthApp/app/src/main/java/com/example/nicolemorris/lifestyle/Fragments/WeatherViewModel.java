package com.example.nicolemorris.lifestyle.Fragments;


import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class WeatherViewModel extends AndroidViewModel {
    private MutableLiveData<WeatherData> jsonData;
    private WeatherRepository mWeatherRepository;

    public WeatherViewModel(Application application){
        super(application);
        mWeatherRepository = new WeatherRepository(application);
        jsonData = mWeatherRepository.getData();
    }
    public void setLocation(String location){
        mWeatherRepository.setLocation(location);
    }

    public LiveData<WeatherData> getData(){
        return jsonData;
    }
}