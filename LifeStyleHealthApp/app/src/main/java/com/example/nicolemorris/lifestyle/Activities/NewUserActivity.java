package com.example.nicolemorris.lifestyle.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.example.nicolemorris.lifestyle.Fragments.ChangeProfileFragment;
import com.example.nicolemorris.lifestyle.Fragments.LocationFragment;
import com.example.nicolemorris.lifestyle.Fragments.NameAgeFragment;
import com.example.nicolemorris.lifestyle.Fragments.PhysDetailsFragment;
import com.example.nicolemorris.lifestyle.Fragments.ProfilePicFragment;
import com.example.nicolemorris.lifestyle.Fragments.ReviewFragment;
import com.example.nicolemorris.lifestyle.Fragments.TitleFragment;
import com.example.nicolemorris.lifestyle.Model.User;
import com.example.nicolemorris.lifestyle.Model.UserRepo;
import com.example.nicolemorris.lifestyle.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;

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

    String bucket_name = "lifestyle-userfiles-mobilehub-1257786421";

    String sqlPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        sqlPath = this.getDatabasePath("LifeStyleDB").getAbsolutePath();

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
            UserRepo.saveUserProfile(getApplicationContext(),user);
            uploadWithTransferUtility();

//            saveUserProfile(user);
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
            creation_step = 9;

        } else if (creation_step == 9) {

            if(isTablet()){
                UserRepo.saveUserProfile(getBaseContext(), user);
                uploadWithTransferUtility();
            } else {
                UserRepo.updateUserProfile(getBaseContext(),user);
                uploadWithTransferUtility();
            }

            Intent userIntent = new Intent(this, MainActivity.class);
            //User user = new User(name.trim(), age, feet, inches, city.trim(), state.trim(), weight.trim(), sex.trim());
            userIntent.putExtra("user", user);
            //userIntent.putExtra("profileImage", profileImage);
            this.startActivity(userIntent);
        }

        fTrans.commit();
    }

    public String serializeUser(User user){
        String content = user.getName()+","+user.getAge()+","+user.getFeet()+","+
                user.getInches()+","+user.getCity()+","+user.getState()+","+user.getWeight()+","+user.getSex()+","+user.getUri()+"\n";
        return content;
    }


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

    boolean isTablet()
    {
        return getResources().getBoolean(R.bool.isTablet);
    }

    public void uploadWithTransferUtility() {
        String KEY = "AKIAS2LP4TDHFHSAEBNN";
        String SECRET = "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";
        BasicAWSCredentials credentials = new BasicAWSCredentials(KEY,SECRET);
        AmazonS3Client s3Client = new AmazonS3Client(credentials);

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(s3Client)
                        .build();
        TransferObserver uploadObserver =
                transferUtility.upload(
                        bucket_name,
                        "qiliu"+sqlPath,
                        new File(sqlPath));

        // Attach a listener to the observer to get state update and progress notifications
        uploadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed upload.
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int)percentDonef;

                Log.d("YourActivity", "ID:" + id + " bytesCurrent: " + bytesCurrent
                        + " bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
            }

        });

        // If you prefer to poll for the data, instead of attaching a
        // listener, check for the state and progress in the observer.
        if (TransferState.COMPLETED == uploadObserver.getState()) {
            // Handle a completed upload.
        }

        Log.d("YourActivity", "Bytes Transferrred: " + uploadObserver.getBytesTransferred());
        Log.d("YourActivity", "Bytes Total: " + uploadObserver.getBytesTotal());
    }

    private void downloadWithTransferUtility() {
        String KEY = "";
        String SECRET = "";
        BasicAWSCredentials credentials = new BasicAWSCredentials(KEY,SECRET);
        AmazonS3Client s3Client = new AmazonS3Client(credentials);

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(s3Client)
                        .build();

        TransferObserver downloadObserver =
                transferUtility.upload(
                        bucket_name,
                        "qiliu"+sqlPath,
                        new File(sqlPath));

        // Attach a listener to the observer to get state update and progress notifications
        downloadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed upload.
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float)bytesCurrent/(float)bytesTotal) * 100;
                int percentDone = (int)percentDonef;

                Log.d("YourActivity", "   ID:" + id + "   bytesCurrent: " + bytesCurrent + "   bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                // Handle errors
            }

        });

        // If you prefer to poll for the data, instead of attaching a
        // listener, check for the state and progress in the observer.
        if (TransferState.COMPLETED == downloadObserver.getState()) {
            // Handle a completed upload.
        }

        Log.d("YourActivity", "Bytes Transferrred: " + downloadObserver.getBytesTransferred());
        Log.d("YourActivity", "Bytes Total: " + downloadObserver.getBytesTotal());
    }

}