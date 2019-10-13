package com.example.nicolemorris.lifestyle.Model;

import android.app.Application;
import android.os.AsyncTask;

import org.json.JSONException;

import androidx.lifecycle.MutableLiveData;

public class ChoiceRepo {
    private final MutableLiveData<Integer> jsonData = new MutableLiveData<>();

    private Integer mChoice;

    ChoiceRepo(Application application){
        loadData();
    }

    public void setChoice(int choice){
        mChoice = choice;
        loadData();
    }

    public MutableLiveData<Integer> getData() {
        return jsonData;
    }

    private void loadData(){
        new AsyncTask<Void,Void,Integer>(){
            @Override
            protected Integer doInBackground(Void... params) {
                // should be taking int from database here databaseName.getChoice()
                return mChoice;
            }

            @Override
            protected void onPostExecute(Integer i) {
                jsonData.setValue(i);
            }
        }.execute();
    }
}
