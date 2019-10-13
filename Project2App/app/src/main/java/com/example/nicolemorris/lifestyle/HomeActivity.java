package com.example.nicolemorris.lifestyle;

import android.content.Intent;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class HomeActivity extends AppCompatActivity
        implements ChoicesDesignedFragment.OnChoiceDataPass {

    String image_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        image_uri = getIntent().getExtras().getString("uri");

        FragmentTransaction fTrans = getSupportFragmentManager().beginTransaction();
        ChoicesDesignedFragment fragment = new ChoicesDesignedFragment();
        Bundle b = new Bundle();
        b.putString("uri", image_uri);
        fragment.setArguments(b);
        fTrans.replace(R.id.fl_frag_ph_1,fragment,"Choices");
        fTrans.commit();

    }

    @Override
    public void onChoiceDataPass(int data) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("CHOICE", data);
        startActivity(intent);

    }

}
