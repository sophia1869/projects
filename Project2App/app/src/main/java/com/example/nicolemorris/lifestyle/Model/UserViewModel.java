package com.example.nicolemorris.lifestyle.Model;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class UserViewModel extends AndroidViewModel {
    private MutableLiveData<User> jsonData;
    private UserRepo mUserRepo;

    public UserViewModel(Application application){
        super(application);
        mUserRepo = new UserRepo(application);
        jsonData = mUserRepo.getData();
    }
    public void setContext(Context context){
        mUserRepo.setmContext(context);
    }


    public LiveData<User> getData(){
        return jsonData;
    }


}
