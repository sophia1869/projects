package com.example.nicolemorris.lifestyle.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.nicolemorris.lifestyle.R;

public class TitleFragment extends Fragment {

    int creation_step;
    TextView tvSubtitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_title, container, false);
        tvSubtitle = view.findViewById(R.id.tv_subtitle);
        creation_step = getArguments().getInt("STEP");
        setSubtitle();

        return view;

    }

    private void setSubtitle(){

        if (creation_step == 0) {

            tvSubtitle.setText("Create New User");

        } else if (creation_step == 1) {

            tvSubtitle.setText("Physical Details");


        } else if (creation_step == 2) {

            tvSubtitle.setText("Location");

        } else if (creation_step == 3) {

            tvSubtitle.setText("Profile Picture");

        }
        else if (creation_step == 4) {

            tvSubtitle.setText("Review Profile");

        }
        else if (creation_step == 5) {

            tvSubtitle.setText("Edit Profile");

        }
    }

}
