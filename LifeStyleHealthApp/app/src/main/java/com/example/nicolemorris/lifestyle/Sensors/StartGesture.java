package com.example.nicolemorris.lifestyle.Sensors;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.TextView;

import com.example.nicolemorris.lifestyle.Model.UserViewModel;

public class StartGesture {

    private SensorManager mSensorManager;
    private Sensor mTwoSteps;
    private Context mContext;

    private final double mMaxThreshold = 2.0;
    private final double mMinThreshold = 1.0;

//    private final double mMaxThreshold = 0.10;
//    private final double mMinThreshold = 0.05;

    private double last_x, last_y, last_z;
    private double now_x, now_y,now_z;
    private boolean mNotFirstTime;
    int steps;

    StepCounter stepCounter;
    StopGesture stopGesture;
    StartGesture thisGesture;

    UserViewModel mUserViewModel;

    public StartGesture(Context context, UserViewModel userViewModel){
        mContext = context;
        mUserViewModel = userViewModel;
        steps = 0;
        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mTwoSteps = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        if(mTwoSteps!=null){
            mSensorManager.registerListener(mListener,mTwoSteps,SensorManager.SENSOR_DELAY_NORMAL);
        }
        thisGesture = this;
    }

    private SensorEventListener mListener = new SensorEventListener() {


        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            //Get the acceleration rates along the y and z axes
            now_z = sensorEvent.values[2];

//            System.out.print("X: " + sensorEvent.values[0] + ", Y: " + sensorEvent.values[1] + ", Z: " + sensorEvent.values[2]);

            //Check if step (forward fast but not too fast & far but not too far) was taken (no = break)

            //If not motion sense
//            if(mNotFirstTime){
//                double dx = Math.abs(last_x - now_x);
//                double dy = Math.abs(last_y - now_y);
                double dz = last_z - now_z;

                //If step was taken
//                if (dz > mMaxThreshold && dz > mMinThreshold) {

                        /*
                        SOUND NOTIFICATION
                         */
                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        Ringtone r = RingtoneManager.getRingtone(mContext, notification);
                        r.play();

                        /*
                        START STEP COUNTER
                         */
                        stepCounter = new StepCounter(mContext, mUserViewModel);

                        /*
                        START STOP GESTURE
                         */
                        stopGesture = new StopGesture(mContext, stepCounter, mUserViewModel);

                        /*
                        STOP LISTENER
                         */
                        mSensorManager.unregisterListener(mListener);

                        /*
                        FOR TESTING ONLY (REMOVE LATER)
                         */
//                        tv_z.setText("Last: " + String.valueOf(last_z) + ", Now: " + String.valueOf(now_z));

//                }

//            }

            last_x = now_x;
            last_y = now_y;
            last_z = now_z;
            mNotFirstTime = true;

        }


        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }

    };
}
