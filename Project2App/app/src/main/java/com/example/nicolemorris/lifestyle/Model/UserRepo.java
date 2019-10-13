package com.example.nicolemorris.lifestyle.Model;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Scanner;

import androidx.lifecycle.MutableLiveData;

import static android.content.Context.MODE_APPEND;

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




    public static void saveUserProfile(Context context, User user){
        try {
            out = context.openFileOutput(fileName, MODE_APPEND);
            String fileContents = serializeUser(user);
            out.write(fileContents.getBytes());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static String serializeUser(User user){
        String content = user.getName()+","+user.getAge()+","+user.getFeet()+","+
                user.getInches()+","+user.getCity()+","+user.getState()+","+user.getWeight()+","+user.getSex()+","+user.getUri()+"\n";
        return content;
    }


    public static void updateUserProfile(Context context, User user){
        try {
            in = context.openFileInput(fileName);
            String temp = "";
            Scanner sc = new Scanner((InputStream)in);
            while(sc.hasNextLine()){
                String next = sc.nextLine();
                String currName = next.substring(0, next.indexOf(","));
                if(user.getName().equals(currName)){
                    temp += serializeUser(user);
                    if(sc.hasNextLine()){
                        temp+="\n";
                    }
                }else{
                    temp += next;
                    if(sc.hasNextLine()){
                        temp+="\n";
                    }
                }
            }
            out = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            out.write(temp.getBytes());
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static User readUserProfile(Context context){
        try{
            in = context.openFileInput(fileName);
            String temp = "";
            Scanner sc = new Scanner((InputStream)in);
            String userInfo = "";
            if(sc.hasNextLine()){
                userInfo = sc.nextLine();
                String[] info = userInfo.split(",");
                User user;
                if(info.length == 8){
                    user = new User(info[0], Integer.parseInt(info[1]), Integer.parseInt(info[2]), Integer.parseInt(info[3]), info[4], info[5], Integer.parseInt(info[6]), info[7], "NoPic");
                } else {
                    user = new User(info[0], Integer.parseInt(info[1]), Integer.parseInt(info[2]), Integer.parseInt(info[3]), info[4], info[5], Integer.parseInt(info[6]), info[7], info[8]);
                }

                return user;
            }
            return null;
        }catch(Exception e){
            return null;
        }

    }
}
