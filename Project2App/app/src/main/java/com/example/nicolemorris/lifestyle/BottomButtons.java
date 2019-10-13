package com.example.nicolemorris.lifestyle;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.nicolemorris.lifestyle.Model.ChoiceViewModel;

public class BottomButtons extends Fragment
        implements View.OnClickListener {

    OnBottomDataPass mDataPasser;
    ImageButton profile_data, goals, bmi, hikes, weather, help;

    ChoiceViewModel mChoiceViewModel;

    int mChoice;

    //Callback interface
    public interface OnBottomDataPass{
        public void onBottomDataPass(int data);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mDataPasser = (OnBottomDataPass) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnDataPass");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_buttons, container, false);

        //Store buttons
        profile_data = view.findViewById(R.id.b_profile);
        goals = view.findViewById(R.id.b_goals);
        bmi = view.findViewById(R.id.b_bmi);
        hikes = view.findViewById(R.id.b_hikes);
        weather = view.findViewById(R.id.b_weather);
        help = view.findViewById(R.id.b_help);

        //Set listeners
        profile_data.setOnClickListener(this);
        goals.setOnClickListener(this);
        bmi.setOnClickListener(this);
        hikes.setOnClickListener(this);
        weather.setOnClickListener(this);
        help.setOnClickListener(this);

        //Return view
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_profile: {
                mDataPasser.onBottomDataPass(9);
                mChoice = 9;


                // call database, save mChoice into ChoiceViewModel



                break;
            }

            case R.id.b_goals: {
                mDataPasser.onBottomDataPass(2);
                mChoice = 2;


                // call database, save mChoice into ChoiceViewModel



                break;
            }

            case R.id.b_bmi: {
                mDataPasser.onBottomDataPass(3);
                mChoice = 3;


                // call database, save mChoice into ChoiceViewModel



                break;
            }

            case R.id.b_hikes: {
                mDataPasser.onBottomDataPass(4);
                mChoice = 4;


                // call database, save mChoice into ChoiceViewModel




                break;
            }

            case R.id.b_weather: {
                mDataPasser.onBottomDataPass(5);
                mChoice = 5;


                // call database, save mChoice into ChoiceViewModel




                break;
            }

            case R.id.b_help: {
                mDataPasser.onBottomDataPass(6);
                mChoice = 6;



                // call database, save mChoice into ChoiceViewModel


                break;
            }


        }
    }

}
