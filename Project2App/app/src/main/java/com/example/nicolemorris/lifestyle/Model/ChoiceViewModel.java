package com.example.nicolemorris.lifestyle.Model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ChoiceViewModel extends AndroidViewModel {
    private MutableLiveData<Integer> jsonData;
    private ChoiceRepo mChoiceRepo;

    public ChoiceViewModel(Application application){
        super(application);
        mChoiceRepo = new ChoiceRepo(application);
        jsonData = mChoiceRepo.getData();
    }
    public void setLocation(int choice){
        mChoiceRepo.setChoice(choice);
    }

    public LiveData<Integer> getData(){
        return jsonData;
    }


}
