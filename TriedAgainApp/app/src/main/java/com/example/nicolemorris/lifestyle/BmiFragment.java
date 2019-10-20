package com.example.nicolemorris.lifestyle;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nicolemorris.lifestyle.Model.User;
import com.example.nicolemorris.lifestyle.Model.UserViewModel;


public class BmiFragment extends Fragment {

    TextView bmi_number, bmi_category;
    double height, weight, bmi_result;

    UserViewModel mUserViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bmi, container, false);

        //Get text views
        bmi_number = view.findViewById(R.id.tv_bmi);
        bmi_category = view.findViewById(R.id.tv_category);

//        //Get arguments
//        height = getArguments().getDouble("HEIGHT");
//        weight = getArguments().getDouble("WEIGHT");

        //Create the view model
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mUserViewModel.setContext(getContext());

        //Set the observer
        mUserViewModel.getData().observe(this, userObserver);

//        //Calculate bmi in kg/m^2 (weight x height^2 x 703)
//        //Note: 703 is the conversion from lbs/in^2 to kg/m^2
//        bmi_result = weight / (height*height) * 703;
//
//        bmi_number.setText(Long.toString(Math.round(bmi_result)));
//
//        if(bmi_result < 18.5){
//            bmi_category.setText("Underweight");
//        } else if (bmi_result >= 18.5 && bmi_result<=24.9){
//            bmi_category.setText("Normal");
//        } else if (bmi_result >= 25 && bmi_result<=29.9){
//            bmi_category.setText("Overweight");
//        } else if (bmi_result >= 30){
//            bmi_category.setText("Obsese");
//        }

        return view;
    }

    final Observer<User> userObserver = new Observer<User>() {
        @Override
        public void onChanged(@Nullable final User user) {
            // Update the UI if this data variable changes
            if (user != null) {
                weight = user.getWeight();
                String name = user.getName();
                height = user.getHeight();

                Log.e("name" ,"" + user.getName());
                Log.e("weight", "" + weight);

                //Calculate bmi in kg/m^2 (weight x height^2 x 703)
                //Note: 703 is the conversion from lbs/in^2 to kg/m^2
                bmi_result = weight / (height * height) * 703;
                Log.e("BMI", "BMI" + bmi_result);
                bmi_number.setText(Long.toString(Math.round(bmi_result)));

                if (bmi_result < 18.5) {
                    bmi_category.setText("Underweight");
                } else if (bmi_result >= 18.5 && bmi_result <= 24.9) {
                    bmi_category.setText("Normal");
                } else if (bmi_result >= 25 && bmi_result <= 29.9) {
                    bmi_category.setText("Overweight");
                } else if (bmi_result >= 30) {
                    bmi_category.setText("Obsese");
                }
            }
        }
    };


}
