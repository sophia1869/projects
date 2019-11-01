package com.example.nicolemorris.lifestyle.Model;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import com.example.nicolemorris.lifestyle.Room.UserTable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import androidx.lifecycle.MutableLiveData;

import static com.example.nicolemorris.lifestyle.Activities.MainActivity.db;

public class UserRepo {
    private final MutableLiveData<User> jsonData = new MutableLiveData<User>();
    private User mUser;
    Context mContext;

    static String fileName = "user_profile";

    static FileOutputStream out;
    static FileInputStream in;

    UserRepo(Application application){
        loadData();
    }

    public void setmContext(Context context){
        mContext = context;
        loadData();
    }

    public MutableLiveData<User> getData() {
        return jsonData;
    }

    private void loadData(){
        new AsyncTask<Context,Void,User>(){
            @Override
            protected User doInBackground(Context... contexts) {
                User user = UserRepo.readUserProfile(contexts[0]);
                return user;
            }

            @Override
            protected void onPostExecute(User user) {
                jsonData.setValue(user);
            }
        }.execute(mContext);
    }

    public static void saveUserLocation(String city, String state){
        new AsyncTask<String,String,Void>(){

            @Override
            protected Void doInBackground(String... strings) {
                return null;
            }
        }.execute();
    }


    public static void saveUserProfile(Context context, User user){
//        try {
//            out = context.openFileOutput(fileName, MODE_APPEND);
//            String fileContents = serializeUser(user);
//            out.write(fileContents.getBytes());
//            out.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        new AsyncTask<User, Void, Void>(){
            @Override
            protected Void doInBackground(User... users) {
                UserTable ut = new UserTable();
                ut.setName(users[0].name);
                ut.setAge(users[0].age);
                ut.setFeet(users[0].feet);
                ut.setInches(users[0].inches);
                ut.setCity(users[0].city);
                ut.setState(users[0].state);
                ut.setWeight(users[0].weight);
                ut.setSex(users[0].sex);
                ut.setUri(users[0].uri);
                ut.setGoal((users[0].goal));
                ut.setAct_level((users[0].act_level));
                ut.setWeight_amt((users[0].weight_amt));
                ut.setStepTimeStamp((users[0].stepTimeStamp));
                ut.setDailySteps((users[0].dailySteps));
                db.userDao().insertUserTable(ut);
                return null;
            }
        }.execute(user);

    }

    public static String serializeUser(User user){
        String content = user.getName()+","+user.getAge()+","+user.getFeet()+","+
                user.getInches()+","+user.getCity()+","+user.getState()+","+user.getWeight()+","+user.getSex()+","+user.getUri()+"\n";
        return content;
    }


    public static void updateUserProfile(Context context, final User user){

        new AsyncTask<User, Void, Void>(){
            @Override
            protected Void doInBackground(User... users) {
                List<UserTable> uts = db.userDao().getAll();
                UserTable ut = uts.get(0);
                ut.setName(user.name);
                ut.setAge(user.age);
                ut.setFeet(user.feet);
                ut.setInches(user.inches);
                ut.setCity(user.city);
                ut.setState(user.state);
                ut.setWeight(user.weight);
                ut.setSex(user.sex);
                ut.setUri(user.uri);
                ut.setGoal((user.goal));
                ut.setAct_level((user.act_level));
                ut.setWeight_amt((user.weight_amt));
                ut.setStepTimeStamp((user.stepTimeStamp));
                ut.setDailySteps((user.dailySteps));
                db.userDao().updateUserTable(ut);
                return null;
            }
        }.execute(user);

    }


    public static User readUserProfile(Context context){

        try{
            UserTable ut = db.userDao().getAll().get(0);

            User u = new User(ut.getName(), ut.getAge(), ut.getFeet(), ut.getInches(), ut.getCity(), ut.getState(),
                    ut.getWeight(), ut.getSex(), ut.getUri(), ut.getGoal(), ut.getAct_level(), ut.getWeight_amt(), ut.getStepTimeStamp(), ut.getDailySteps());
            return u;
        } catch (Exception e){
            return null;
        }

    }

}
