package com.example.nicolemorris.lifestyle.Model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.nicolemorris.lifestyle.Adapter.ProfileListAdapter;

public class ProfilePicAndName implements Parcelable {

    private Uri imageUri;
    private String userName;

    public ProfilePicAndName(Uri imageUri, String userName){
        this.imageUri = imageUri;
        this.userName = userName;
    }


    protected ProfilePicAndName(Parcel in) {
        imageUri = in.readParcelable(Uri.class.getClassLoader());
        userName = in.readString();
    }

    public static final Creator<ProfilePicAndName> CREATOR = new Creator<ProfilePicAndName>() {
        @Override
        public ProfilePicAndName createFromParcel(Parcel in) {
            return new ProfilePicAndName(in);
        }

        @Override
        public ProfilePicAndName[] newArray(int size) {
            return new ProfilePicAndName[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(imageUri, flags);
        dest.writeString(userName);
    }

    public Uri getProfilePic(){
        return this.imageUri;
    }

    public String getUserName(){
        return this.userName;
    }
}
