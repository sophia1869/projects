package com.example.huizhou.androidchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class UsersAdapter extends ArrayAdapter<String> {

    public UsersAdapter(Context context, ArrayList<String> mems) {
        super(context, 0, mems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String user = getItem(position);

        String[] parts = user.split(" ");
        boolean belong = (parts[0].equals(Main2Activity.username));

        String usern = "";
        for (int i =1; i<parts.length;i++){
            usern += (parts[i]+" ");
        }

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            if(belong){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_msg, parent, false);
                TextView tvHome = (TextView) convertView.findViewById(R.id.mmessage_body);
                tvHome.setText(usern);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateTime = dateFormat.format(new Date());
                TextView tvName = (TextView) convertView.findViewById(R.id.name);
                tvName.setText(parts[0]+" "+currentDateTime);
            }else{
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.their_msg, parent, false);
                TextView tvHome = (TextView) convertView.findViewById(R.id.tmessage_body);
                tvHome.setText(usern);

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentDateTime = dateFormat.format(new Date());
                TextView tvName = (TextView) convertView.findViewById(R.id.namer);
                tvName.setText(parts[0]+" "+currentDateTime);
            }
        }

        // Return the completed view to render on screen
        return convertView;
    }
}
