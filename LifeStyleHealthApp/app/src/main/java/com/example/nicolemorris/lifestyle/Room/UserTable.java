package com.example.nicolemorris.lifestyle.Room;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="UserTable")
public class UserTable {

    @PrimaryKey(autoGenerate = true)
    public int did;

    public int getDid() {
        return did;
    }

    public void setDid(int did) {
        this.did = did;
    }

    @ColumnInfo(name ="name")
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ColumnInfo(name ="age")
    int age;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @ColumnInfo(name ="city")
    String city;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @ColumnInfo(name ="state")
    String state;
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @ColumnInfo(name ="feet")
    int feet;

    public int getFeet() {
        return feet;
    }

    public void setFeet(int feet) {
        this.feet = feet;
    }

    @ColumnInfo(name ="inches")
    int inches;

    public int getInches() {
        return inches;
    }

    public void setInches(int inches) {
        this.inches = inches;
    }

    @ColumnInfo(name ="weight")
    int weight;

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @ColumnInfo(name ="sex")
    String sex;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @ColumnInfo(name ="hasGoal")
    boolean hasGoal;

    public boolean getHasGoal(){
        return hasGoal;
    }

    public void setHasGoal(boolean hasGoal){
        this.hasGoal = hasGoal;
    }

    @ColumnInfo(name ="goal")
    Integer goal; //0 = lose weight, 1 = maintain weight, 2 = gain weight
    public Integer getGoal() {
        return goal;
    }

    public void setGoal(Integer goal) {
        this.goal = goal;
    }

    @ColumnInfo(name ="act_level")
    Integer act_level; //0 = sedentary, 1 = light, 2 = moderate, 3 = very, 4 = extremely
    public Integer getAct_level() {
        return act_level;
    }

    public void setAct_level(Integer act_level) {
        this.act_level = act_level;
    }

    @ColumnInfo(name ="weight_amt")
    Integer weight_amt; //If goal to lose or gain weight, amount to lose or gain

    public Integer getWeight_amt() {
        return weight_amt;
    }

    public void setWeight_amt(Integer weight_amt) {
        this.weight_amt = weight_amt;
    }

    @ColumnInfo(name ="uri")
    String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @ColumnInfo(name ="stepTimeStamp")
    long stepTimeStamp;

    public long getStepTimeStamp(){
        return this.stepTimeStamp;
    }

    public void setStepTimeStamp(long stepTimeStamp){
        this.stepTimeStamp = stepTimeStamp;
    }

    @ColumnInfo(name ="dailySteps")
    int dailySteps;

    public int getDailySteps(){
        return this.dailySteps;
    }

    public void setDailySteps(int dailySteps){
        this.dailySteps = dailySteps;
    }

}
