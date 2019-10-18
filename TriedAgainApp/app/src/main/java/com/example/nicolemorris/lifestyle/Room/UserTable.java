package com.example.nicolemorris.lifestyle.Room;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="UserTable")
public class UserTable {

    @PrimaryKey(autoGenerate = true)
    public int did;

    @ColumnInfo(name ="age")
    int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getFeet() {
        return feet;
    }

    public void setFeet(int feet) {
        this.feet = feet;
    }

    @ColumnInfo(name ="feet")
    int feet;

    @ColumnInfo(name ="inches")
    int inches;

    @ColumnInfo(name ="weight")
    int weight;

    @ColumnInfo(name ="name")
    String name;

    @ColumnInfo(name ="city")
    String city;

    @ColumnInfo(name ="state")
    String state;

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

    public int getInches() {
        return inches;
    }

    public void setInches(int inches) {
        this.inches = inches;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getGoal() {
        return goal;
    }

    public void setGoal(Integer goal) {
        this.goal = goal;
    }

    public Integer getAct_level() {
        return act_level;
    }

    public void setAct_level(Integer act_level) {
        this.act_level = act_level;
    }

    public Integer getWeight_amt() {
        return weight_amt;
    }

    public void setWeight_amt(Integer weight_amt) {
        this.weight_amt = weight_amt;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @ColumnInfo(name ="sex")
    String sex;

    //boolean hasGoal;

    @ColumnInfo(name ="goal")
    Integer goal; //0 = lose weight, 1 = maintain weight, 2 = gain weight

    @ColumnInfo(name ="act_level")
    Integer act_level; //0 = sedentary, 1 = light, 2 = moderate, 3 = very, 4 = extremely

    @ColumnInfo(name ="weight_amt")
    Integer weight_amt; //If goal to lose or gain weight, amount to lose or gain

    @ColumnInfo(name ="uri")
    String uri;


}
