package com.example.nicolemorris.lifestyle;

import android.Manifest;
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
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class LocationFragment extends Fragment implements View.OnClickListener{

    private String city, state;
    private EditText cityText, stateText;
    private Button locate, next;
    private String[] location;
    private LocationManager locationManager;

    int REQUEST_LOCATION = 1;
    double latitude, longitude;

    LocationOnDataPass mDataPasser;

    //Callback interface
    public interface LocationOnDataPass{
        public void onLocationDataPass(String[] data);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mDataPasser = (LocationOnDataPass) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnDataPass");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        location = new String[2];
        cityText = view.findViewById(R.id.et_city);
        stateText = view.findViewById(R.id.et_state);
        locate = view.findViewById(R.id.b_location);
        next = view.findViewById(R.id.b_next);

        locate.setOnClickListener(this);
        next.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.b_location:{
                findCoordinates();
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                try{
                    List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                    city = addresses.get(0).getLocality();
                    state = addresses.get(0).getAdminArea();
                    cityText.setText(city);
                    stateText.setText(state);
                } catch(Exception e){
                }
                break;
            }
            case R.id.b_next:{
                //NEED TO ADD LOCATION TO PASS FOR STORAGE :)
                state = stateText.getText().toString();
                city = cityText.getText().toString();
                location[0] = state;
                location[1] = city;
                if(state.equals("") || city.equals("")){
                    Toast.makeText(getContext(), "Please specify your location", Toast.LENGTH_SHORT).show();
                }
                mDataPasser.onLocationDataPass(location);
                break;
            }
        }
    }

    private void findCoordinates(){
        locationManager=(LocationManager)getContext().getSystemService(Context.LOCATION_SERVICE);
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

        if (ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),

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
                Toast.makeText(getContext(), "Can't Get Your Location", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void OnGPS() {

        final AlertDialog.Builder builder= new AlertDialog.Builder(getContext());

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

}
