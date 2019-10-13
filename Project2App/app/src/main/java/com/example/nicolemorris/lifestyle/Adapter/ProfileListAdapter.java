package com.example.nicolemorris.lifestyle.Adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.nicolemorris.lifestyle.Model.ProfilePicAndName;
import com.example.nicolemorris.lifestyle.R;

import java.util.List;

public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.ViewHolder>{

    private List<ProfilePicAndName> profiles;
    private Context mContext;

    public ProfileListAdapter(List<ProfilePicAndName> profileList){
        profiles = profileList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        protected View itemLayout;
        protected TextView itemTvData;
        protected ImageButton itemProfilePic;

        public ViewHolder(View view){
            super(view);
            itemLayout = view;
            itemTvData = (TextView) view.findViewById(R.id.tv_data);
            itemProfilePic = (ImageButton) view.findViewById(R.id.profile_pic);
        }
    }

    @NonNull
    @Override
    public ProfileListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View myView = layoutInflater.inflate(R.layout.fragment_profile_list_item,viewGroup,false);
        return new ViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileListAdapter.ViewHolder viewHolder, int i) {
        viewHolder.itemProfilePic.setImageURI(profiles.get(i).getProfilePic());
        viewHolder.itemTvData.setText(profiles.get(i).getUserName());
    }

    @Override
    public int getItemCount() {

        return profiles.size();
    }
}
