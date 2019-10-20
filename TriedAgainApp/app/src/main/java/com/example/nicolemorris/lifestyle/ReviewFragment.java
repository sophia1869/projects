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
import android.widget.Button;
import android.widget.TextView;

import com.example.nicolemorris.lifestyle.Model.User;
import com.example.nicolemorris.lifestyle.Model.UserViewModel;

public class ReviewFragment extends Fragment
        implements View.OnClickListener {

    UserViewModel mUserViewModel;

    User u;
    Button bEditProfile;
    ReviewOnDataPass mDataPasser;
    TextView name, age, city, state, height, weight, sex;

    //Callback interface
    public interface ReviewOnDataPass {
        public void onReviewDataPass();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mDataPasser = (ReviewOnDataPass) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnDataPass");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review, container, false);


        //Create the view model
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        //Set the observer
        mUserViewModel.getData().observe(this,userObserver);

        u = getArguments().getParcelable("user");

        name = view.findViewById(R.id.tv_name_d_revf);
        name.setText(u.getName());

        age = view.findViewById(R.id.tv_age_d_revf);
        age.setText(Integer.toString(u.getAge()));

        city = view.findViewById(R.id.tv_city_d_revf);
        city.setText(u.getCity());

        state = view.findViewById(R.id.tv_state_d_revf);
        state.setText(u.getState());

        height = view.findViewById(R.id.tv_height_d_revf);
        height.setText(Integer.toString(u.getFeet()) + " feet " + Integer.toString(u.getInches()) + " inches");

        weight = view.findViewById(R.id.tv_weight_d_revf);
        weight.setText(Integer.toString(u.getWeight()) + " pounds");

        sex = view.findViewById(R.id.tv_sex_d_revf);
        sex.setText(u.getSex());

        bEditProfile = view.findViewById(R.id.b_edit_profile_revf);
        bEditProfile.setOnClickListener(this);

        return view;
    }

    final Observer<User> userObserver  = new Observer<User>() {
        @Override
        public void onChanged(@Nullable final User u) {
            // Update the UI if this data variable changes
            if(u!=null) {
               setFields(u);
            }
        }
    };

    public void setFields(User u){
        name = getActivity().findViewById(R.id.tv_name_d_revf);
        name.setText(u.getName());

        age = getActivity().findViewById(R.id.tv_age_d_revf);
        age.setText(Integer.toString(u.getAge()));

        city = getActivity().findViewById(R.id.tv_city_d_revf);
        city.setText(u.getCity());

        state = getActivity().findViewById(R.id.tv_state_d_revf);
        state.setText(u.getState());

        height = getActivity().findViewById(R.id.tv_height_d_revf);
        height.setText(Integer.toString(u.getFeet()) + " feet " + Integer.toString(u.getInches()) + " inches");

        weight = getActivity().findViewById(R.id.tv_weight_d_revf);
        weight.setText(Integer.toString(u.getWeight()) + " pounds");

        sex = getActivity().findViewById(R.id.tv_sex_d_revf);
        sex.setText(u.getSex());

        bEditProfile = getActivity().findViewById(R.id.b_edit_profile_revf);
        bEditProfile.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.b_edit_profile_revf:{
                mDataPasser.onReviewDataPass();


                break;
            }

//            case R.id.b_finish: {
//                mDataPasser.onReviewDataPass(6);
//                break;
//            }
        }
    }

}
