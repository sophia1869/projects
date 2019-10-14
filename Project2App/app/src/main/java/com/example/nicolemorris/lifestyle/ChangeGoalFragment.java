package com.example.nicolemorris.lifestyle;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nicolemorris.lifestyle.Model.User;
import com.example.nicolemorris.lifestyle.Model.UserRepo;
import com.example.nicolemorris.lifestyle.Model.UserViewModel;

public class ChangeGoalFragment extends Fragment
        implements AdapterView.OnItemSelectedListener, View.OnClickListener {

//    ChangeGoalOnDataPass mDataPasser;
    Button bSetGoal;
    Spinner sGoal, sActLevel, sAmount;
    int goal; //0 = lose weight, 1 = maintain weight, 2 = gain weight
    int act_level; //0 = sedentary, 1 = light, 2 = moderate, 3 = very, 4 = extremely
    int weight_amt; //If goal to lose or gain weight, amount to lose or gain

    User mUser;


    UserViewModel mUserViewModel;


//    //Callback interface
//    public interface ChangeGoalOnDataPass{
//        public void onChangeGoalDataPass(int goal, int act_level, int amount);
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        try{
//            mDataPasser = (ChangeGoalOnDataPass) context;
//        }catch(ClassCastException e){
//            throw new ClassCastException(context.toString() + " must implement OnDataPass");
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_goal, container, false);

        //Set spinners
        ArrayAdapter<CharSequence> goal_adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.goal_choices, android.R.layout.simple_spinner_item);
        sGoal = view.findViewById(R.id.s_goal);
        sGoal.setAdapter(goal_adapter);
        sGoal.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> act_level_adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.activity_levels, android.R.layout.simple_spinner_item);
        sActLevel = view.findViewById(R.id.s_act_level);
        sActLevel.setAdapter(act_level_adapter);
        sActLevel.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> amt_adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.weight_amount, android.R.layout.simple_spinner_item);
        sAmount = view.findViewById(R.id.s_amount);
        sAmount.setAdapter(amt_adapter);
        sAmount.setOnItemSelectedListener(this);

        //Set buttons
        bSetGoal = view.findViewById(R.id.b_set_goal);
        bSetGoal.setOnClickListener(this);

        //create viewmodel
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
                mUser = user;
                Log.e("changegoalfrag", "user name: " + mUser.getName() );
                Log.e("changegoalfrag", "user hasGoal: " + mUser.checkGoal() );

            }

        }
    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        switch (parent.getId()) {
            case R.id.s_goal: {
                goal = parent.getSelectedItemPosition();
                break;
            }
            case R.id.s_act_level: {
                act_level = parent.getSelectedItemPosition();
                break;
            }
            case R.id.s_amount: {
                weight_amt = parent.getSelectedItemPosition() + 1;
                if( weight_amt>2){
                    Toast.makeText(getContext(), "amount over 2 pounds!", Toast.LENGTH_SHORT).show();
                    break;
                }
                break;
            }

        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_set_goal: {

                /*
                NEED TO SAVE THE DATA FIRST (ADD)
                 */

                System.out.println("Goal = " + goal);
                System.out.println("Level = " + act_level);
                System.out.println("Amount = " + weight_amt);

                //Tell view data was saved
//                mDataPasser.onChangeGoalDataPass(goal,act_level,weight_amt);
                Log.e("ChangeGoalFrag", "goal, act_level, weitht: " + goal +"/" + act_level +"/" + weight_amt );
                mUser.setGoal(goal, act_level,weight_amt);
                MainActivity.hasGoal =true;
//                Intent  intent2Main = new Intent(getContext(), MainActivity.class);
//                intent2Main.putExtra("choice", 2);
//                this.startActivity(intent2Main);
                FragmentTransaction fTrans = getActivity().getSupportFragmentManager().beginTransaction();
                GoalsFragment gf = new GoalsFragment();
                Bundle sentData = new Bundle();
                sentData.putParcelable("user",mUser);
                gf.setArguments(sentData);
                fTrans.replace(R.id.fl_frag_ph_2,gf,"Goals");
                fTrans.commit();

                break;
            }
        }
    }

}
