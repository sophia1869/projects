package com.example.huizhou.androidchat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void join(View view) {
        Intent intent = new Intent(this, Main2Activity.class);

        EditText editText = (EditText) findViewById(R.id.editText);
        String user = editText.getText().toString();
        intent.putExtra("userName", user);

        EditText editText2 = (EditText) findViewById(R.id.editText2);
        String room = editText2.getText().toString();
        intent.putExtra("room", room);

        startActivity(intent);
    }

}
