package com.example.nicolemorris.lifestyle;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nicolemorris.lifestyle.Model.User;
import com.example.nicolemorris.lifestyle.Model.UserRepo;
import com.example.nicolemorris.lifestyle.Model.UserViewModel;
import com.example.nicolemorris.lifestyle.Room.UserTable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import static com.example.nicolemorris.lifestyle.MainActivity.db;

public class ChangeProfileFragment extends Fragment
        implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    EditText etName, etCity, etState, etAge;
    Spinner s_h_feet,s_h_inches,s_weight1, s_weight2, s_weight3,s_sex;
    Calendar date;
    String[] dataToPass;
    Button bSave, bDate,bLocation,bPic;

    String w1="",w2="",w3="";

    String name, city, state, sex;
    int feet, inches, age, newFeet, newInches, weight, newWeight;
    String image_uri;


    User user;
    //ChangeProfileOnDataPass userDataPasser;

    DatePickerDialog picker;

    LocationManager locationManager;
    private static final int REQUEST_LOCATION = 1;
    Double latitude, longitude;

//    FileOutputStream out;
//    FileInputStream in;
//
//    String fileName = "user_profile";

    UserViewModel mUserViewModel;

    ArrayAdapter<CharSequence> num_adapter;
    ArrayAdapter<CharSequence> gender_adapter;

//    public interface ChangeProfileOnDataPass{
//        public void onChangeProfileDataPass(User user, int choice);
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        try{
//            userDataPasser = (ChangeProfileOnDataPass) context;
//        }catch(ClassCastException e){
//            throw new ClassCastException(context.toString() + " must implement ChangeProfileOnDataPass");
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_profile, container, false);

        etName = view.findViewById(R.id.et_name);

        bPic = view.findViewById(R.id.b_edit_pic);
        bPic.setOnClickListener(this);

        bDate = view.findViewById(R.id.b_birthday);
        bDate.setOnClickListener(this);

        bLocation = view.findViewById(R.id.b_location);
        bLocation.setOnClickListener(this);

        etCity = view.findViewById(R.id.tv_city_hc_revf);

        etState = view.findViewById(R.id.tv_state_hc_revf);

        etAge = view.findViewById(R.id.et_age);

        num_adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.number_array, android.R.layout.simple_spinner_item);


        s_h_feet  = (Spinner)view.findViewById(R.id.s_feet);
        s_h_feet.setOnItemSelectedListener(this);
        s_h_feet.setAdapter(num_adapter);

        s_h_inches = (Spinner)view.findViewById(R.id.s_inches);
        s_h_inches.setOnItemSelectedListener(this);
        s_h_inches.setAdapter(num_adapter);

        s_weight1 = (Spinner)view.findViewById(R.id.s_weight1);
        s_weight1.setOnItemSelectedListener(this);
        s_weight1.setAdapter(num_adapter);

        s_weight2 = (Spinner)view.findViewById(R.id.s_weight2);
        s_weight2.setOnItemSelectedListener(this);
        s_weight2.setAdapter(num_adapter);

        s_weight3 = (Spinner)view.findViewById(R.id.s_weight3);
        s_weight3.setOnItemSelectedListener(this);
        s_weight3.setAdapter(num_adapter);

        gender_adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.gender_array, android.R.layout.simple_spinner_item);

        s_sex = (Spinner)view.findViewById(R.id.s_sex);
        s_sex.setOnItemSelectedListener(this);
        s_sex.setAdapter(gender_adapter);

        bSave = view.findViewById(R.id.b_save);
        bSave.setOnClickListener(this);

        //Create the view model
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mUserViewModel.setContext(getContext());


        //Set the observer
        mUserViewModel.getData().observe(this,userObserver);


        //byte[] imageByte = getArguments().getByteArray("image");
        //profile_image = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);

        return view;
    }

    final Observer<User> userObserver  = new Observer<User>() {
        @Override
        public void onChanged(@Nullable final User userData) {
            // Update the UI if this data variable changes
            if(userData!=null) {
                    user = userData;
                    name = user.getName();
                    etName.setText(name);
                    city = user.getCity();
                    etCity.setText(city);
                    state = user.getState();
                    etState.setText(state);
                    age = user.getAge();
                    etAge.setText(""+age);
                    int feetPosition = num_adapter.getPosition(""+user.getFeet());
                    s_h_feet.setSelection(feetPosition);
                    feet = user.getFeet();
                    int inchPosition = num_adapter.getPosition(""+user.getInches());
                    s_h_inches.setSelection(inchPosition);
                    inches = user.getInches();
                    weight  =user.getWeight();
                    String w= ""+weight;
                    int w1Pos, w2Pos, w3Pos;
                    if(w.length() == 3){
                        w1Pos = num_adapter.getPosition(w.substring(0,1));
                        w2Pos = num_adapter.getPosition(w.substring(1,2));
                        w3Pos = num_adapter.getPosition(w.substring(2,3));
                    }else if (w.length()==2){
                        w1Pos = num_adapter.getPosition("0");
                        w2Pos = num_adapter.getPosition(w.substring(0,1));
                        w3Pos = num_adapter.getPosition(w.substring(1,2));
                    }else{
                        w1Pos = num_adapter.getPosition("0");
                        w2Pos = num_adapter.getPosition("0");
                        w3Pos = num_adapter.getPosition(w.substring(0,1));
                    }


                    s_weight1.setSelection(w1Pos);
                    s_weight2.setSelection(w2Pos);
                    s_weight3.setSelection(w3Pos);

                    sex = user.getSex();
                    int genderPosition = gender_adapter.getPosition(sex);
                    s_sex.setSelection(genderPosition);

                    image_uri = user.getUri()==null? "https://www.petmd.com/sites/default/files/Acute-Dog-Diarrhea-47066074.jpg":user.getUri();
            }
        }
    };

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        switch (parent.getId()){
            case R.id.s_feet: {
                newFeet = Integer.parseInt((String)parent.getItemAtPosition(pos));
                break;
            }
            case R.id.s_inches: {
                newInches = Integer.parseInt((String)parent.getItemAtPosition(pos));
                break;
            }
            case R.id.s_weight1: {
                w1 = (String)parent.getItemAtPosition(pos);
                break;
            }
            case R.id.s_weight2: {
                w2 = (String)parent.getItemAtPosition(pos);
                break;
            }
            case R.id.s_weight3: {
                w3 = (String)parent.getItemAtPosition(pos);
                break;
            }
            case R.id.s_sex: {
                sex = (String)parent.getItemAtPosition(pos);
                break;
            }
            default:{
                System.out.println("Nothing selected");
            }
        }

        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_edit_pic: {
//                userDataPasser.onChangeProfileDataPass(user,8);
                FragmentTransaction fTrans = getActivity().getSupportFragmentManager().beginTransaction();
                ProfilePicFragment ppf = new ProfilePicFragment();
                Bundle sentData = new Bundle();
                sentData.putParcelable("user",user);
                ppf.setArguments(sentData);
                fTrans.replace(R.id.fl_frag_ph_2,ppf,"Goals");
                fTrans.commit();

                break;
            }
            case R.id.b_save: {
                name = etName.getText().toString().trim();
                city = etCity.getText().toString();
                state = etState.getText().toString();

                if(name.equals("") || city.equals("") || state.equals("")){
                    Toast.makeText(getContext(), "You have empty fields!", Toast.LENGTH_SHORT).show();
                    break;
                }

                if(date == null){
                    age = user.getAge();
                    age = Integer.parseInt(etAge.getText().toString());
                }

                if(newFeet!=0) feet = newFeet;
                if(newInches != 0) inches = newInches;
                newWeight = Integer.parseInt(w1+w2+w3);
                if(newWeight != 0) weight = newWeight;
                if(user != null){
                    //String oldName = user.getName();
                    user = new User(name, age, feet, inches, city, state, weight, sex, image_uri);
                     //updateUserProfile(user, oldName);

                    UserRepo.saveUserProfile(getContext(),new User(name, age, feet, inches, city, state, weight, sex, image_uri));

                } else {
                    user = new User(name, age, feet, inches, city, state, weight, sex, image_uri);
                    //saveUserProfile(user);
                }


               // saveUserProfile(user);
                //userDataPasser.onChangeProfileDataPass(user,9);

                FragmentTransaction fTrans = getActivity().getSupportFragmentManager().beginTransaction();
                ReviewFragment rf = new ReviewFragment();
                Bundle sentData = new Bundle();

                sentData.putParcelable("user",user);
                rf.setArguments(sentData);
                fTrans.replace(R.id.fl_frag_ph_2,rf,"Goals");
                fTrans.commit();

                break;
            }
            case R.id.b_birthday:{
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar c = Calendar.getInstance();
                                c.set(year, monthOfYear, dayOfMonth);
                                date = c;
                                Calendar today = Calendar.getInstance();
                                age = today.get(Calendar.YEAR) - date.get(Calendar.YEAR);
                                if (today.get(Calendar.DAY_OF_YEAR) < date.get(Calendar.DAY_OF_YEAR)) {
                                    age--;
                                }
                                etAge.setText(""+age);
                            }
                        }, year, month, day);
                picker.show();
                break;
            }
            case R.id.b_location:{
                findLocation();
                Geocoder g = new Geocoder(getContext());
                try {
                    List<Address> addresses = g.getFromLocation(latitude, longitude, 1);
                    city = addresses.get(0).getLocality();
                    state = addresses.get(0).getAdminArea();
                    etCity.setText(city);
                    etState.setText(state);
                }catch(Exception e){
                    Toast.makeText(getActivity(), "Can't Get Your Location", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private void findLocation(){
        locationManager=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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
        }
    }

    private void getLocation() {

        //Check Permissions again

        if (ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),

                Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps !=null)
            {
                latitude=LocationGps.getLatitude();
                longitude=LocationGps.getLongitude();
            }
            else if (LocationNetwork !=null)
            {
                latitude=LocationNetwork.getLatitude();
                longitude=LocationNetwork.getLongitude();
            }
            else if (LocationPassive !=null)
            {
                latitude=LocationPassive.getLatitude();
                longitude=LocationPassive.getLongitude();
            }
            else
            {
                Toast.makeText(getActivity(), "Can't Get Your Location", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void OnGPS() {

        final AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
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

//    private void updateUserProfile(User user, String oldName){
//        try {
//            in = getActivity().openFileInput(fileName);
//            String temp = "";
//            Scanner sc = new Scanner((InputStream)in);
//            while(sc.hasNextLine()){
//                String next = sc.nextLine();
//                String currName = next.substring(0, next.indexOf(","));
//                if(oldName.equals(currName)){
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
//            out = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
//            out.write(temp.getBytes());
//            out.close();
//            in.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public String serializeUser(User user){
        String content = user.getName()+","+user.getAge()+","+user.getFeet()+","+
                user.getInches()+","+user.getCity()+","+user.getState()+","+user.getWeight()+","+user.getSex()+"\n";
        return content;
    }

    boolean isTablet()
    {
        return getResources().getBoolean(R.bool.isTablet);
    }

//    private void saveUserProfile(User user){
//        try {
//            out = getActivity().openFileOutput(fileName, Context.MODE_APPEND);
//            String fileContents = serializeUser(user);
//            out.write(fileContents.getBytes());
//            out.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

}
