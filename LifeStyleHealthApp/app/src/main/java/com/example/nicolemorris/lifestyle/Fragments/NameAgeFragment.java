package com.example.nicolemorris.lifestyle.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicolemorris.lifestyle.Model.UserViewModel;
import com.example.nicolemorris.lifestyle.R;

import java.util.Calendar;

public class NameAgeFragment extends Fragment implements View.OnClickListener{

    private Button bDate,bNext;
    private EditText tname;
    String name;
    Calendar date;
    NameAgeOnDataPass mDataPasser;
    DatePickerDialog picker;
    TextView tvBirthday;

    //Callback interface
    public interface NameAgeOnDataPass{
        public void onNameAgeDataPass(Calendar data, String name);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mDataPasser = (NameAgeOnDataPass) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnDataPass");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_name_age, container, false);
        bDate = view.findViewById(R.id.b_birthday);
        bDate.setOnClickListener(this);

        tname = view.findViewById(R.id.et_name);
        tvBirthday = view.findViewById(R.id.tv_birthday_d);

        bNext = view.findViewById(R.id.b_next);
        bNext.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view){
        switch(view.getId()){
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
                                String b_day = Integer.toString(view.getMonth()) + "/" + Integer.toString(view.getDayOfMonth()) + "/" + Integer.toString(view.getYear());
                                tvBirthday.setText(b_day);
                            }
                        }, year, month, day);
                picker.show();

                break;
            }
            case R.id.b_next: {
                name = tname.getText().toString().trim();
                if(name.equals("")){
                    Toast.makeText(getContext(), "Please input your name", Toast.LENGTH_SHORT).show();
                    break;
                }
                if(date == null){
                    Toast.makeText(getContext(), "Please choose your birth date", Toast.LENGTH_SHORT).show();
                    break;
                }
                mDataPasser.onNameAgeDataPass(date,name);
                break;

            }
        }
    }

}
