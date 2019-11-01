package com.example.nicolemorris.lifestyle.Fragments;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nicolemorris.lifestyle.Activities.MainActivity;
import com.example.nicolemorris.lifestyle.R;
import com.example.nicolemorris.lifestyle.Room.WeatherDataTable;
import com.google.gson.Gson;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherFragment extends Fragment{

    private TextView mTvTemp;
    private TextView mTvHum;
    private WeatherData mWeatherData;

    private WeatherViewModel mWeatherViewModel;
    String location;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        //super.onCreate(savedInstanceState);

        mTvTemp = view.findViewById(R.id.tv_temp_d);
        mTvHum = view.findViewById(R.id.tv_rain_d);

        //Create the view model
        mWeatherViewModel = ViewModelProviders.of(getActivity()).get(WeatherViewModel.class);

        //Set the observer
        mWeatherViewModel.getData().observe(this,weatherObserver);

        location = getArguments().getString("city");
        loadWeatherData(location);

        return view;
    }

    //create an observer that watches the LiveData<WeatherData> object
    final Observer<WeatherData> weatherObserver  = new Observer<WeatherData>() {
        @Override
        public void onChanged(@Nullable final WeatherData weatherData) {
            // Update the UI if this data variable changes
            if(weatherData!=null) {
                mWeatherData = weatherData;
                String rain = (mWeatherData.getCurrentCondition().getHumidity()<90)?"Low":"high";
                mTvTemp.setText("" + Math.round(mWeatherData.getTemperature().getTemp() - 273.15) +  "\u00B0 C");
                mTvHum.setText("" + rain);
                WeatherRepository.saveDataToDB(mWeatherData);
            }
        }
    };

    void loadWeatherData(String location){

        //pass the location in to the view model
        mWeatherViewModel.setLocation(location);
    }


}

class WeatherData {
    private CurrentCondition mCurrentCondition = new CurrentCondition();
    private Temperature mTemperature = new Temperature();
    private Wind mWind = new Wind();
    private Rain mRain = new Rain();
    private Snow mSnow = new Snow();
    private Clouds mClouds = new Clouds();


    public  class CurrentCondition {
        private long mWeatherId;
        private String mCondition;
        private String mDescr;
        private String mIcon;

        private double mPressure;
        private double mHumidity;

        public long getWeatherId() {
            return mWeatherId;
        }
        public void setWeatherId(long weatherId) {
            mWeatherId = weatherId;
        }
        public String getCondition() {
            return mCondition;
        }
        public void setCondition(String condition) {
            mCondition = condition;
        }
        public String getDescr() {
            return mDescr;
        }
        public void setDescr(String descr) {
            mDescr = descr;
        }
        public String getIcon() {
            return mIcon;
        }
        public void setIcon(String icon) {
            mIcon = icon;
        }
        public double getPressure() {
            return mPressure;
        }
        public void setPressure(double pressure) {
            mPressure = pressure;
        }
        public double getHumidity() {
            return mHumidity;
        }
        public void setHumidity(double humidity) {
            mHumidity = humidity;
        }
    }

    public class Temperature {
        private double mTemp;
        private double mMinTemp;
        private double mMaxTemp;

        public double getTemp() {
            return mTemp;
        }
        public void setTemp(double temp) {
            mTemp = temp;
        }
        public double getMinTemp() {
            return mMinTemp;
        }
        public void setMinTemp(double minTemp) {
            mMinTemp = minTemp;
        }
        public double getMaxTemp() {
            return mMaxTemp;
        }
        public void setMaxTemp(double maxTemp) {
            mMaxTemp = maxTemp;
        }

    }

//    public class Weather{
//        private int mId;
//        private  String mMain;
//        private  String mDescription;
//        private  String mIcon;
//
//        public String getmDescription(){return mDescription;}
//        public void setmDescription(String description){mDescription = description;}
//    }

    public class Wind {
        private double mSpeed;
        private double mDeg;
        public double getSpeed() {
            return mSpeed;
        }
        public void setSpeed(double speed) {
            mSpeed = speed;
        }
        public double getDeg() {
            return mDeg;
        }
        public void setDeg(double deg) {
            mDeg = deg;
        }
    }

    public class Rain {
        private String mTime;
        private double mAmount;
        public String getTime() {
            return mTime;
        }
        public void setTime(String time) {
            mTime = time;
        }
        public double getAmount() {
            return mAmount;
        }
        public void setAmount(double amount) {
            mAmount = amount;
        }
    }

    public class Snow {
        private String mTime;
        private double mAmount;
        public String getTime() {
            return mTime;
        }
        public void setTime(String time) {
            mTime = time;
        }
        public double getAmount() {
            return mAmount;
        }
        public void setAmount(double amount) {
            mAmount = amount;
        }
    }

    public class Clouds {
        private long mPerc;

        public long getPerc() {
            return mPerc;
        }

        public void setPerc(long perc) {
            mPerc = perc;
        }
    }

    //Setters and Getters

    public void setCurrentCondition(CurrentCondition currentCondition){
        mCurrentCondition = currentCondition;
    }
    public CurrentCondition getCurrentCondition(){
        return mCurrentCondition;
    }

    public void setTemperature(Temperature temperature){
        mTemperature = temperature;
    }
    public Temperature getTemperature(){
        return mTemperature;
    }

    public void setWind(Wind wind){
        mWind = wind;
    }
    public Wind getWind(){
        return mWind;
    }

    public void setRain(Rain rain){
        mRain = rain;
    }
    public Rain getRain(){
        return mRain;
    }

    public void setSnow(Snow snow){
        mSnow = snow;
    }
    public Snow getSnow(){
        return mSnow;
    }

    public void setClouds(Clouds clouds){
        mClouds = clouds;
    }
    public Clouds getClouds(){
        return mClouds;
    }

}

class NetworkUtils {
    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    private static String APPIDQUERY = "&appid=";
    private static final String app_id="99ea8382701bd7481e5ea568772f739a";

    public static URL buildURLFromString(String location){
        URL myURL = null;
        try{
            myURL = new URL(BASE_URL + location + APPIDQUERY + app_id);
        }catch(MalformedURLException e){
            e.printStackTrace();
        }
        return myURL;
    }

    public static String getDataFromURL(URL url) throws IOException{
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();

            //The scanner trick: search for the next "beginning" of the input stream
            //No need to user BufferedReader
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            }
            else{
                return null;
            }

        }finally {
            urlConnection.disconnect();
        }
    }
}

class JSONWeatherUtils {
    public static WeatherData getWeatherData(String data) throws JSONException{
        WeatherData weatherData = new WeatherData();

        //Start parsing JSON data
        JSONObject jsonObject = new JSONObject(data); //Must throw JSONException

        WeatherData.CurrentCondition currentCondition = weatherData.getCurrentCondition();
        JSONObject jsonMain = jsonObject.getJSONObject("main");
        currentCondition.setHumidity(jsonMain.getInt("humidity"));
        currentCondition.setPressure(jsonMain.getInt("pressure"));
        weatherData.setCurrentCondition(currentCondition);

        //Get the temperature, wind and cloud data.
        WeatherData.Temperature temperature = weatherData.getTemperature();
        WeatherData.Wind wind = weatherData.getWind();
        WeatherData.Clouds clouds = weatherData.getClouds();
        temperature.setMaxTemp(jsonMain.getDouble("temp_max"));
        temperature.setMinTemp(jsonMain.getDouble("temp_min"));
        temperature.setTemp(jsonMain.getDouble("temp"));
        weatherData.setTemperature(temperature);

        return weatherData;
    }
}

class WeatherRepository {

    private final MutableLiveData<WeatherData> jsonData = new MutableLiveData<>();
    static private String mLocation;


    WeatherRepository(Application application){

        //loadData();
    }

    public void setLocation(String location){
        mLocation = location;
        if (mLocation != null) { // keeps crashing without this if check!!!!!
            loadData();
        }
    }

    public MutableLiveData<WeatherData> getData() {
        return jsonData;
    }

    private void loadData(){
        new AsyncTask<Void,Void,String>(){
            @Override
            protected String doInBackground(Void... voids) {

                URL weatherDataURL = NetworkUtils.buildURLFromString(mLocation);
                String retrievedJsonData = null;
                try {
                    retrievedJsonData = NetworkUtils.getDataFromURL(weatherDataURL);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return retrievedJsonData;
            }

            @Override
            protected void onPostExecute(String jsonWeatherData) {
                try {
                    jsonData.setValue(JSONWeatherUtils.getWeatherData(jsonWeatherData));
                }catch(JSONException e){
                    e.printStackTrace();
                }

            }
        }.execute();
    }

    public static void saveDataToDB(WeatherData wd){
        Log.e("WeatherRepository", "WeatherData " + wd.getCurrentCondition().getDescr() );


        new AsyncTask<WeatherData, Void, Void>() {
            @Override
            protected Void doInBackground(WeatherData... wds) {
                String weatherDataJson = null;

                weatherDataJson = WeatherRepository.weatherData2String(wds[0]);
                Log.e("ProfileRepo", "userJson " + wds[0].getCurrentCondition().getDescr());


                WeatherDataTable wdt = new WeatherDataTable(mLocation, weatherDataJson);
                Log.e("ProfileRepo", "PT: " + wdt.getWeatherJson());

                MainActivity.db.weatherDao().insertWeatherDataTable(wdt);
                return null;
            }
        }.execute(wd);
    }

    public static String weatherData2String(WeatherData wd) {
        String ret;
        Gson gson = new Gson();
        ret = gson.toJson(wd);
        return ret;
    }

}