package com.example.nicolemorris.lifestyle.Model;


import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    int age, feet, inches, weight;
    String name,city, state, sex;
    boolean hasGoal;
    Integer goal; //0 = lose weight, 1 = maintain weight, 2 = gain weight
    Integer act_level; //0 = sedentary, 1 = light, 2 = moderate, 3 = very, 4 = extremely
    Integer weight_amt; //If goal to lose or gain weight, amount to lose or gain
    String uri;

    public User(){

    }
    public User(String name, int age, int feet, int inches, String city, String state, int weight, String sex, String uri){
        this.name = name;
        this.age = age;
        this.city = city;
        this.state = state;
        this.feet = feet;
        this.inches = inches;
        this.weight = weight;
        this.sex = sex;
        this.hasGoal = false;
        this.uri = uri;
    }

    public void setGoal(int goal, int act_level, int weight_amt){
        this.goal = goal;
        this.act_level = act_level;
        this.weight_amt = weight_amt;
        this.hasGoal = true;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(age);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeInt(feet);
        dest.writeInt(inches);
        dest.writeInt(weight);
        dest.writeString(sex);
        dest.writeString(uri);
        if(goal != null){
            dest.writeInt(goal);
        }
        if(act_level != null){
            dest.writeInt(act_level);
        }
        if(weight_amt != null){
            dest.writeInt(weight_amt);
        }
    }

    protected User(Parcel in) {
        name = in.readString();
        age = in.readInt();
        city = in.readString();
        state = in.readString();
        feet = in.readInt();
        inches = in.readInt();
        weight = in.readInt();
        sex = in.readString();
        uri = in.readString();
        System.out.println(in.dataSize());
        if(in.dataSize() > 0){
            hasGoal = true;
            goal = in.readInt();
            act_level = in.readInt();
            weight_amt = in.readInt();
        }

    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getName(){
        return this.name;
    }
    public int getAge(){return this.age;}
    public int getFeet(){return this.feet;}
    public int getInches(){return this.inches;}
    public String getCity(){return this.city;}
    public String getState(){return this.state;}
    public int getWeight(){return this.weight;}
    public String getSex(){return this.sex;}
    public boolean checkGoal() {return this.hasGoal;}
    public Integer getGoal() {return this.goal;}
    public Integer getAct_level() {return this.act_level;}
    public Integer getWeight_amt() {return this.weight_amt;}
    public int getHeight(){
        return 12*feet + inches;
    }
    public String getUri() { return this.uri;}
    public void setUri(String uri) {this.uri = uri;}
    public void setHasGoal (boolean b) {this.hasGoal = b;}

    @Override
    public int describeContents() {
        return 0;
    }



}
