package com.example.nicolemorris.lifestyle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nicolemorris.lifestyle.Model.User;
import com.example.nicolemorris.lifestyle.Model.UserRepo;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Scanner;

public class NewUserActivity extends AppCompatActivity
        implements NameAgeFragment.NameAgeOnDataPass, PhysDetailsFragment.PhysOnDataPass,
        LocationFragment.LocationOnDataPass, ProfilePicFragment.ProfilePicOnDataPass,
        ReviewFragment.ReviewOnDataPass{

    int creation_step = 0;
    String name, city, state, sex, profile_image;
    int feet, inches, weight;
    int age;

    User user;

    FileOutputStream out;
    FileInputStream in;

    String fileName = "user_profile";
    String folder = "profile.images";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        if (savedInstanceState != null) {
            creation_step = savedInstanceState.getInt("STEP");
        } else {
            if(isTablet()){
                creation_step = 5;
            }
            setView();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Put them in the outgoing Bundle
        outState.putInt("STEP", creation_step);

    }

    @Override
    public void onNameAgeDataPass(Calendar date, String name) {
        // print to test
        this.name = name;
        Calendar today = Calendar.getInstance();
        this.age = today.get(Calendar.YEAR) - date.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < date.get(Calendar.DAY_OF_YEAR)) {
            this.age--;
        }
        creation_step = 1;
        setView();
    }

    @Override
    public void onPhysDataPass(String[] data) {
        feet = Integer.parseInt(data[0]);
        inches = Integer.valueOf(data[1]);
        weight = Integer.parseInt(data[2]);
        sex = data[3];
        creation_step = 2;
        setView();
    }


    @Override
    public void onLocationDataPass(String[] location) {
        state = location[0];
        city = location[1];
        creation_step = 8;
        setView();
    }

    @Override
    public void onProfilePicDataPass(String image) {
        profile_image = image;
        creation_step = 4;
        setView();

    }

//    @Override
//    public void onChangeProfileDataPass(User newUser, int choice) {
//        user = newUser;
//        creation_step = choice;
//        setView();
//    }

    @Override
    public void onReviewDataPass() {
        // no returned data
        creation_step = 5;
        setView();

    }



    private void setView() {
        //Find each frame layout, replace with corresponding fragment
        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();

        TitleFragment tf = new TitleFragment();
        Bundle sentData = new Bundle();
        if(isTablet()){
            sentData.putInt("STEP",0);
        } else {
            sentData.putInt("STEP",creation_step);
        }

        tf.setArguments(sentData);
        fTrans.replace(R.id.fl_frag_ph_1, tf, "Title");

        if (creation_step == 0) {

            //Name & birthday
            fTrans.replace(R.id.fl_frag_ph_2, new NameAgeFragment(), "Location");
            creation_step++;

        } else if (creation_step == 1) {

            //Physical details
            fTrans.replace(R.id.fl_frag_ph_2, new PhysDetailsFragment(), "Location");
            creation_step++;

        } else if (creation_step == 2) {

            //Location
            fTrans.replace(R.id.fl_frag_ph_2, new LocationFragment(), "Location");
            creation_step++;

        } else if (creation_step == 8) {
            //Profile Picture
            ProfilePicFragment fragment = new ProfilePicFragment();
            //fragment.setArguments(bundle);
            fTrans.replace(R.id.fl_frag_ph_2, fragment, "Location");
            creation_step++;

         }
        else if (creation_step == 4) {
            //Intent userIntent = new Intent(this, MainActivity.class);
            user = new User(name.trim(), age, feet, inches, city.trim(), state.trim(), weight, sex.trim(), profile_image);
            System.out.println(user.getUri());
            UserRepo.saveUserProfile(getBaseContext(),user);
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", user);
            ReviewFragment fragment = new ReviewFragment();
            fragment.setArguments(bundle);

            //Review
            fTrans.replace(R.id.fl_frag_ph_2, fragment, "Location");
            creation_step++;

         }
        else if (creation_step == 5) {
            //Edit details
            ChangeProfileFragment fragment = new ChangeProfileFragment();
            if(!isTablet()){
                Bundle bundle = new Bundle();
                bundle.putParcelable("user", user);
                //bundle.putByteArray("image", convertImageToByteArray(profileImage));
                fragment.setArguments(bundle);
            }

            fTrans.replace(R.id.fl_frag_ph_2, fragment, "Location");
            creation_step++;

        } else if (creation_step == 9) {

            if(isTablet()){
                UserRepo.saveUserProfile(getBaseContext(), user);
            } else {
                UserRepo.updateUserProfile(getBaseContext(),user);
            }

            Intent userIntent = new Intent(this, MainActivity.class);
            //User user = new User(name.trim(), age, feet, inches, city.trim(), state.trim(), weight.trim(), sex.trim());
            userIntent.putExtra("user", user);
            //userIntent.putExtra("profileImage", profileImage);
            this.startActivity(userIntent);
        }

        fTrans.commit();
    }

//    public String serializeUser(User user){
//        String content = user.getName()+","+user.getAge()+","+user.getFeet()+","+
//                user.getInches()+","+user.getCity()+","+user.getState()+","+user.getWeight()+","+user.getSex()+","+user.getUri()+"\n";
//        return content;
//    }
//
//
//    private void saveUserProfile(User user){
//        try {
//            out = openFileOutput(fileName, MODE_APPEND);
//            String fileContents = serializeUser(user);
//            out.write(fileContents.getBytes());
//            out.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

//    private void updateUserProfile(User user){
//        try {
//            in = openFileInput(fileName);
//            String temp = "";
//            Scanner sc = new Scanner((InputStream)in);
//            while(sc.hasNextLine()){
//                String next = sc.nextLine();
//                String currName = next.substring(0, next.indexOf(","));
//                if(user.getName().equals(currName)){
//                    temp += serializeUser(user);
//                    if(sc.hasNextLine()){
//                        temp+="\n";
//                    }
//                }else{
//                    temp += next;
//                    if(sc.hasNextLine()){
//                        temp+="\n";
//                    }
//                }
//            }
//            out = openFileOutput(fileName, Context.MODE_PRIVATE);
//            out.write(temp.getBytes());
//            out.close();
//            in.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private String saveProfileImage(){
//        ContextWrapper cw = new ContextWrapper(this);
//        File directory = cw.getDir(folder, Context.MODE_PRIVATE);
//        // Create imageDir
//        File mypath=new File(directory,user.getName()+".jpg");
//
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(mypath);
//            // Use the compress method on the BitMap object to write image to the OutputStream
//            profileImage.compress(Bitmap.CompressFormat.PNG, 50, fos);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return directory.getAbsolutePath();
//    }

//    private byte[] convertImageToByteArray(Bitmap image){
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        return stream.toByteArray();
//    }

    boolean isTablet()
    {
        return getResources().getBoolean(R.bool.isTablet);
    }

}