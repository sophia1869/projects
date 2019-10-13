package com.example.nicolemorris.lifestyle;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.widget.Toast;

import com.example.nicolemorris.lifestyle.Model.ChoiceViewModel;
import com.example.nicolemorris.lifestyle.Model.User;
import com.example.nicolemorris.lifestyle.Model.UserRepo;
import com.example.nicolemorris.lifestyle.Model.UserViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity
        implements BottomButtons.OnBottomDataPass, ReviewFragment.ReviewOnDataPass, GoalsFragment.GoalsOnDataPass,
        ChangeProfileFragment.ChangeProfileOnDataPass, ProfilePicFragment.ProfilePicOnDataPass {

    User u;
    String username;
    int user_choice;
    double height_inches = 72;
    double weight_pounds = 105;
    public static boolean hasGoal = false;
    boolean isFirstChoice;

//    public void setUser_choice (int input) {
//        user_choice= input;
//    }

    ReviewFragment pf;
    GoalsFragment gf;


    ChangeGoalFragment cgf;
    BmiFragment bf;
    WeatherFragment wf;
    HelpFragment hf;
    ChangeProfileFragment cpf;
    HeaderFragment hef;
    ProfilePicFragment ppf;

    int goal, act_level, goal_amount;

    //variables for find-a-hike
    LocationManager locationManager;
    private static final int REQUEST_LOCATION = 1;
    String latitude, longitude;

    //Add user or update user
    List<User> users;

    //File information
    FileOutputStream out;
    FileInputStream in;
    String fileName = "user_profile";
    String folder = "profile_images/";
    String city = "default city";


    UserViewModel mUserViewModel;

    ChoiceViewModel mChoiceViewModel;

    int choice;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getIntent().getExtras()!= null){
            user_choice = getIntent().getExtras().getInt("CHOICE");
        }

        u = UserRepo.readUserProfile(getBaseContext());


        System.out.println("user choice"+user_choice);
        //Add permission for getting access to the current location
        ActivityCompat.requestPermissions(this,new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        isFirstChoice = true;

        if (u == null) {
            Intent messageIntent = new Intent(this, NewUserActivity.class);
            this.startActivity(messageIntent);

        } else if (user_choice != 0){
            Toast.makeText(getBaseContext(), "user_choice-oncreate" + user_choice, Toast.LENGTH_SHORT).show();
            changeFragments();
        } else {
            Intent messageIntent = new Intent(this, HomeActivity.class);
            messageIntent.putExtra("uri", u.getUri());
            this.startActivity(messageIntent);
        }

//        //Create the view model
//        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
//
//        //Set the observer
//        mUserViewModel.getData().observe(this,userObserver);
//
//
//        //Create the view model
//        mChoiceViewModel = ViewModelProviders.of(this).get(ChoiceViewModel.class);
//
//        //Set the observer
//        mChoiceViewModel.getData().observe(this,choiceObserver);

    }


    final Observer<User> userObserver  = new Observer<User>() {
        @Override
        public void onChanged(@Nullable final User userData) {
        if (userData == null) {
            createNewUser();
        } else if (user_choice != 0){
            changeFragments();
        } else {
            goHome();
        }
        }
    };

    final Observer<Integer> choiceObserver  = new Observer<Integer>() {
        @Override
        public void onChanged(@Nullable final Integer choice) {
            if(choice!=null)
                user_choice = choice;

        }
    };

    public void createNewUser(){
        Intent messageIntent = new Intent(this, NewUserActivity.class);
        this.startActivity(messageIntent);
    }

    public void goHome(){
        Intent messageIntent = new Intent(this, HomeActivity.class);
        this.startActivity(messageIntent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        //Put them in the outgoing Bundle
        outState.putParcelable("user",u);
        outState.putInt("user_choice",user_choice);

        //Save the view hierarchy
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        //Restore stuff
        u = savedInstanceState.getParcelable("user");
        user_choice = savedInstanceState.getInt("user_choice");
        changeFragments();

        //Restore the view hierarchy automatically
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBottomDataPass(int data) {

        user_choice = data;
        changeFragments();

    }

//    @Override
//    public void onChangeGoalDataPass(int g, int l, int a){
//        hasGoal = true;
//        u.setGoal(g,l,a);
//        changeFragments();
//    }

    @Override
    public void onGoalsDataPass(){
        hasGoal = false;
        changeFragments();
    }

    @Override
    public void onReviewDataPass(){
        user_choice = 7;
        changeFragments();
    }

    @Override
    public void onChangeProfileDataPass(User user, int choice){
        u = user;
        user_choice = choice;
        changeFragments();
    }

    @Override
    public void onProfilePicDataPass(String image){
        u.setUri(image);
        user_choice = 9;
        changeFragments();
    }

    public void changeFragments(){

        boolean addHeader = true;

        //Find each frame layout, replace with corresponding fragment
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();

        if (user_choice == 9){

            isFirstChoice = false;

            //Launch profile information
            pf = new ReviewFragment();

            //Put this into a bundle
            Bundle fragmentBundle = new Bundle();
            fragmentBundle.putParcelable("user",u);
            pf.setArguments(fragmentBundle);

            fTrans.replace(R.id.fl_frag_ph_2,pf,"Profile");

        } else if (user_choice == 2){
            isFirstChoice = false;
            u.setHasGoal(true);
            if(u.checkGoal() && hasGoal){
                //Launch fitness goals
                gf = new GoalsFragment();
                Bundle sentData = new Bundle();
                sentData.putParcelable("user", u);
                gf.setArguments(sentData);
                fTrans.replace(R.id.fl_frag_ph_2,gf,"Goals");

            } else {
                //Launch change fitness goals
                cgf = new ChangeGoalFragment();
                fTrans.replace(R.id.fl_frag_ph_2,cgf,"Goals");
            }

        } else if (user_choice == 3){
            isFirstChoice = false;
            bf = new BmiFragment();

            //Send data to it
            Bundle sentData = new Bundle();
            height_inches = (u.getFeet() * 12) + u.getInches();
            sentData.putDouble("HEIGHT",height_inches);
            sentData.putDouble("WEIGHT",u.getWeight());
            bf.setArguments(sentData);

            //Launch bmi
            fTrans.replace(R.id.fl_frag_ph_2,bf,"BMI");


        } else if (user_choice == 4){

            isFirstChoice = false;
            findHikeAround();

        } else if (user_choice == 5){

            locateForWeather();
            isFirstChoice = false;

            wf = new WeatherFragment();
            Bundle sentData = new Bundle();
            sentData.putString("city",city);
            wf.setArguments(sentData);
            fTrans.replace(R.id.fl_frag_ph_2,wf,"Weather");

        } else if (user_choice == 6){

            if(isTablet()){
                addHeader = false;
            }
            //Launch help
            hf = new HelpFragment();
            fTrans.replace(R.id.fl_frag_ph_2,hf,"Help");

        } else if (user_choice == 7) {
            isFirstChoice = false;
            cpf = new ChangeProfileFragment();
            //Put this into a bundle
            Bundle fragmentBundle = new Bundle();
            fragmentBundle.putParcelable("user",u);
            cpf.setArguments(fragmentBundle);

            fTrans.replace(R.id.fl_frag_ph_2,cpf,"Profile");

        } else if (user_choice == 8){
            ppf = new ProfilePicFragment();
            Bundle sentData = new Bundle();
            sentData.putParcelable("user",u);
            ppf.setArguments(sentData);
            fTrans.replace(R.id.fl_frag_ph_2,ppf,"Profile");
        }

        if(addHeader){
            hef = new HeaderFragment();
            Bundle sentData = new Bundle();
            sentData.putInt("CHOICE",user_choice);
            sentData.putString("uri", u.getUri());
            hef.setArguments(sentData);
            fTrans.replace(R.id.fl_frag_ph_1,hef,"Header");
        } else {
            if(!isFirstChoice){
                fTrans.remove(hef);
            }
        }

        fTrans.replace(R.id.fl_frag_ph_3,new BottomButtons(),"Choices");
        fTrans.commit();
    }

    public void locateForWeather(){

        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Check gps is enable or not
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            //Enable gps
            OnGPS();
        }
        else
        {
            //Get latitude and longitude
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);
            }else{
                Geocoder geocoder = new Geocoder(this);

                Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

                List<Address> addresses = null;
                if (LocationGps !=null)
                {
                    try {
                        addresses = geocoder.getFromLocation(LocationGps.getLatitude(), LocationGps.getLongitude(), 1);
                        city = addresses.get(0).getLocality();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else if (LocationNetwork !=null)
                {
                    try {
                        addresses = geocoder.getFromLocation(LocationNetwork.getLatitude(), LocationNetwork.getLongitude(), 1);
                        city = addresses.get(0).getLocality();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if (LocationPassive !=null)
                {
                    try {
                        addresses = geocoder.getFromLocation(LocationPassive.getLatitude(), LocationPassive.getLongitude(), 1);
                        city = addresses.get(0).getLocality();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    city = "Salt Lake City";
                    Log.d("failLoc","fail to locate you for weather, using SLC");
                }
//                city = "Houston"; //For testing (currently pooring rain (tropical storm)
            }

//            Geocoder geocoder = new Geocoder(this);
//            try {
//                List<Address> addresses = geocoder.getFromLocation(lat, longi, 1);
//                city = addresses.get(0).getLocality();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

    }

    private void findHikeAround(){
        locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //Check gps is enable or not
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            //Enable gps
            OnGPS();
        }
        else
        {
            //Get latitude and longitude and open map based on the location
            getLocation();
            getHikeResult();
        }
    }

    private void getLocation() {

        //Check Permissions again

        if (ActivityCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,

                Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps !=null)
            {
                double lat=LocationGps.getLatitude();
                double longi=LocationGps.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);
            }
            else if (LocationNetwork !=null)
            {
                double lat=LocationNetwork.getLatitude();
                double longi=LocationNetwork.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);
            }
            else if (LocationPassive !=null)
            {
                double lat=LocationPassive.getLatitude();
                double longi=LocationPassive.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);
            }
            else
            {
                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void OnGPS() {

        final AlertDialog.Builder builder= new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    private void getHikeResult(){
        Uri searchUri = Uri.parse("geo:"+latitude+","+longitude+"?q=" + "hike");

        //Create the implicit intent
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, searchUri);

        //If there's an activity associated with this intent, launch it
        if(mapIntent.resolveActivity(getPackageManager())!=null){
            startActivity(mapIntent);
        }
    }

    public String serializeUser(User user){
        String content = user.getName()+","+user.getAge()+","+user.getName()+","+user.getState()+","+user.getFeet()+","+
                user.getInches()+","+user.getWeight()+","+user.getSex()+"\n";
        return content;
    }


    private void saveUserProfile(User user){
        try {
            out = openFileOutput(fileName, MODE_PRIVATE);
            String fileContents = serializeUser(user);
            out.write(fileContents.getBytes());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        try {
//            FileOutputStream fileOutputStream = openFileOutput("Tutorial File.txt", MODE_PRIVATE);
//            fileOutputStream.write(textToSave.getBytes());
//            fileOutputStream.close();
//
//            Toast.makeText(getApplicationContext(), "Text Saved", Toast.LENGTH_SHORT).show();
//
//            inputField.setText("");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    private void updateUserProfile(User user){
        try {
            in = openFileInput(fileName);
            String temp = "";
            Scanner sc = new Scanner((InputStream)in);
            while(sc.hasNextLine()){
                String next = sc.nextLine();
                String currName = next.substring(0, next.indexOf(","));
                if(user.getName().equals(currName)){
                    temp += serializeUser(user);
                }else{
                    temp += next;
                }
            }
            out = openFileOutput(fileName, Context.MODE_PRIVATE);
            out.write(temp.getBytes());
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveProfileImage(Bitmap profileImage){
        ContextWrapper cw = new ContextWrapper(this);
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir(folder, Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,username+".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            profileImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private User readUserProfile(){
        try{
            in = openFileInput(fileName);
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


    boolean isTablet()
    {
        return getResources().getBoolean(R.bool.isTablet);
    }
}


