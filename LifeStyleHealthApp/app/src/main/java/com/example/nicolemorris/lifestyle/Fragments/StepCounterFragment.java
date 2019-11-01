package com.example.nicolemorris.lifestyle.Fragments;

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
import com.example.nicolemorris.lifestyle.R;

/*
 * Displays fragment data
 */
public class StepCounterFragment extends Fragment {

    TextView step_number;
    UserViewModel mUserViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_step_counter, container, false);
        step_number = view.findViewById(R.id.textView10);

        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mUserViewModel.setContext(getContext());

        //Set the observer
        mUserViewModel.getData().observe(this, userObserver);
        return view;
    }

    final Observer<User> userObserver = new Observer<User>() {
        @Override
        public void onChanged(@Nullable final User user) {
            // Update the UI if this data variable changes
            if (user != null) {
                step_number.setText(user.getDailySteps());
            }
        }
    };

}
