package com.example.huizhou.androidchat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class Main2Activity extends AppCompatActivity {

    public static String username;
    String room;
    ImageButton b;
    EditText e;
    WebSocket ws;
    ListView lv;
    ArrayList<String> mems;

    //ArrayAdapter<String> itemsAdapter;
    UsersAdapter usersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        room = intent.getStringExtra("room");
        username = intent.getStringExtra("userName");

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.editText3);
        textView.setText(room);

        b = (ImageButton) findViewById(R.id.imageButton);
        lv = (ListView) findViewById(R.id.dynamic);
        e = (EditText) findViewById(R.id.editText5);

        mems = new ArrayList<>();

        //itemsAdapter = new ArrayAdapter<String>(Main2Activity.this, android.R.layout.simple_list_item_1, mems);
        usersAdapter = new UsersAdapter(this,mems);
        //lv.setAdapter(itemsAdapter);
        lv.setAdapter(usersAdapter);

        // Create a WebSocket factory and set 5000 milliseconds as a timeout
        // value for socket connection.
        WebSocketFactory factory = new WebSocketFactory().setConnectionTimeout(5000);

        // Create a WebSocket. The timeout value set above is used.
        try {
            ws = factory.createSocket("ws://10.0.2.2:8080");

            ws.addListener(new WebSocketAdapter() {

                @Override
                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                    ws.sendText("join "+ room);
                    System.out.println("on web socket");
                }

                @Override
                public void onFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
                    display(frame.getPayloadText());
                    System.out.println("on frame");
                }
            });
            ws.connectAsynchronously();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ws != null) {
            ws.disconnect();
            ws = null;
        }
    }

    public void sendMessage(View v) {
            String msg = username + " " + e.getText().toString();
            ws.sendText(msg);
            e.getText().clear();
    }

    public void display(String msg){

        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(msg);
        JsonObject jo = je.getAsJsonObject();
        String parseMsg = jo.get("user").getAsString() + " "+ jo.get("message").getAsString();

        mems.add(parseMsg);

        //itemsAdapter.notifyDataSetChanged();
        usersAdapter.notifyDataSetChanged();

        lv.post(
                new Runnable() {
                    @Override
                    public void run() {

                        //lv.setSelection(itemsAdapter.getCount()-1);
                        lv.setSelection(usersAdapter.getCount()-1);

                    }
                }
        );

    }

//    public void BtnClick(View view) {
//        b.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View view) {
//                    String msg = username + ": " + e.getText().toString();
//                    ws.sendText(username + " " + msg);
//                    e.getText().clear();
//
//            }
//        });
//    }

}