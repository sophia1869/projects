package com.example.nicolemorris.lifestyle.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class ListofProfileAndName implements Parcelable {

    private List<ProfilePicAndName> list;

    public ListofProfileAndName(List<ProfilePicAndName> list){
        this.list = list;
    }


    protected ListofProfileAndName(Parcel in) {
        list = in.createTypedArrayList(ProfilePicAndName.CREATOR);
    }

    public static final Creator<ListofProfileAndName> CREATOR = new Creator<ListofProfileAndName>() {
        @Override
        public ListofProfileAndName createFromParcel(Parcel in) {
            return new ListofProfileAndName(in);
        }

        @Override
        public ListofProfileAndName[] newArray(int size) {
            return new ListofProfileAndName[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(list);
    }

    public List<ProfilePicAndName> getList(){
        return this.list;
    }
}
