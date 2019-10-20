package com.example.nicolemorris.lifestyle;

import android.content.Context;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicolemorris.lifestyle.Model.User;
import com.example.nicolemorris.lifestyle.Model.UserRepo;
import com.example.nicolemorris.lifestyle.Model.UserViewModel;
import com.example.nicolemorris.lifestyle.Room.UserTable;
import com.google.android.gms.common.UserRecoverableException;

import java.util.List;

import static com.example.nicolemorris.lifestyle.MainActivity.db;

public class GoalsFragment extends Fragment
        implements View.OnClickListener {

    User u;
    //GoalsOnDataPass mDataPasser;
    Button bChangeGoal;
    TextView tvCalAmt, tvGoalTxt, tvGoalAmt, tvGoalHC;
    //    int user_weight = 72; //Weight of user
//    int user_height = 175; //Height of user
//    boolean userIsMale = true; //Male or female
//    int user_age = 27; //Age of user
    int goal; //0 = lose weight, 1 = maintain weight, 2 = gain weight
    int act_level; //0 = sedentary, 1 = light, 2 = moderate, 3 = very, 4 = extremely
    int weight_amt; //If goal to lose or gain weight, amount to lose or gain

    UserViewModel mUserViewModel;

//    //Callback interface
//    public interface GoalsOnDataPass{
//        public void onGoalsDataPass();
//    }
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        try{
//            mDataPasser = (GoalsOnDataPass) context;
//        }catch(ClassCastException e){
//            throw new ClassCastException(context.toString() + " must implement OnDataPass");
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goals, container, false);

        u = getArguments().getParcelable("user");
        //Get arguments
//        goal = getArguments().getInt("Goal");
//        act_level = getArguments().getInt("Act_Level");
//        weight_amt = getArguments().getInt("Amount");


//        new AsyncTask<User, Void, Void>(){
//            @Override
//            protected Void doInBackground(User... users) {
//
//                    u = users[0];
//                    List<UserTable> uts = db.userDao().getAll();
//                    goal = uts.get(uts.size()-1).getGoal();
//                    act_level = uts.get(uts.size()-1).getAct_level();
//                    weight_amt = uts.get(uts.size()-1).getWeight_amt();
//                    return null;
//
////                goal = users[0].getGoal();
////                act_level = users[0].getAct_level();
////                weight_amt = users[0].getWeight_amt();
////                return null;
//            }
//        }.execute();

        goal = u.getGoal();
        act_level = u.getAct_level();
        weight_amt = u.getWeight_amt();

        tvGoalTxt = view.findViewById(R.id.tv_goal_txt_d);
        tvGoalHC = view.findViewById(R.id.tv_goal_hc);

        tvCalAmt = view.findViewById(R.id.tv_cal_amt_d);
        Long cal = Math.round(calcCalories());
        if(cal<1200 || (cal<1000 && u.getSex().equals("Female"))){ //200 for male 1000 for female
            Toast.makeText(getContext(), "not enough calories take in. Maybe reset your goal.", Toast.LENGTH_SHORT).show();
        }
        tvCalAmt.setText(cal.toString());

        tvGoalAmt = view.findViewById(R.id.tv_goal_amt_d);
        tvGoalAmt.setText(Integer.toString(weight_amt));


        bChangeGoal = view.findViewById(R.id.b_change_goal);
        bChangeGoal.setOnClickListener(this);

        //Create the view model
        mUserViewModel = ViewModelProviders.of(this).get(UserViewModel.class);
        mUserViewModel.setContext(getContext());


        //Set the observer
        mUserViewModel.getData().observe(this,userObserver);

        return view;
    }

    final Observer<User> userObserver  = new Observer<User>() {
        @Override
        public void onChanged(@Nullable final User userData) {
            // Update the UI if this data variable changes
            if(userData!=null) {
                u = userData;
//                tvCalAmt.setText(userData.getWeight_amt());
//                tvGoalAmt.setText(userData.getWeight_amt());
            }
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.b_change_goal: {
//                String cal = tvCalAmt.getText().toString().trim();
//                String goal = tvGoalAmt.getText().toString().trim();
//                if(cal.equals("")){
//                    Toast.makeText(getContext(), "Please input how many calories to eat today", Toast.LENGTH_SHORT).show();
//                    break;
//                }
//                if(goal == null){
//                    Toast.makeText(getContext(), "Please choose your goal", Toast.LENGTH_SHORT).show();
//                    break;
//                }
//                mDataPasser.onGoalsDataPass();
                FragmentTransaction fTrans = getActivity().getSupportFragmentManager().beginTransaction();
                ChangeGoalFragment cgf = new ChangeGoalFragment();
                Bundle sentData = new Bundle();
                sentData.putParcelable("user",u);
                cgf.setArguments(sentData);
                fTrans.replace(R.id.fl_frag_ph_2,cgf,"Goals");
                fTrans.commit();

                break;
            }
        }
    }

    private double calcCalories(){

        //Daily calorie requirement to maintain weight
        double calories = calcDailyCalToMaintain(calcBMR());

        //Goal lose weight
        if(goal==0){
            tvGoalTxt.setText("Lose");
            tvGoalHC.setText("pounds this week");
            calories = calories - (weight_amt*500);

        }

        //Goal gain weight
        if (goal==2) {
            tvGoalTxt.setText("Gain");
            tvGoalHC.setText("pounds this week");
            calories = calories + (weight_amt*500);

        }

        return calories;
    }

    private double calcBMR(){
        double bmr = 0.0;

        if(u!=null){
            if(u.getSex().equals("Male")){
                bmr = 66 + (6.3 * u.getWeight()) + (12.9 * u.getHeight()) - (6.8 * u.getAge());
            } else {
                bmr = 655 + (4.3 * u.getWeight()) + (4.7 * u.getHeight()) - (4.7 * u.getAge());
            }
        }

        return bmr;
    }

    private double calcDailyCalToMaintain(double bmr){

        if(act_level == 0) {
            //sedentary
            return bmr * 1.2;

        } else if(act_level == 1) {
            //light active
            return bmr * 1.375;

        } else if(act_level == 2) {
            //moderate active
            return bmr * 1.55;

        } else if(act_level == 3) {
            //very active
            return bmr * 1.725;

        } else {
            //extremely active
            return bmr * 1.9;
        }
    }


}