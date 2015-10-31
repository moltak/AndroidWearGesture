package com.horde.samantha.samantha;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import net.horde.commandsetlibrary.command.CommandSet;
import net.horde.commandsetlibrary.command.CommandSetFactory;
import net.horde.commandsetlibrary.rest.RetrofitAdapterProvider;
import net.horde.commandsetlibrary.rest.model.Result;
import net.horde.commandsetlibrary.rest.service.Samanda;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends Activity implements SensorEventListener {

    //TextView mTextValues;
    Button mButtonAlarm, mButtonLeft, mButtonRight;

    // sensors
    private static final float SHAKE_THRESHOLD = 1.1f;
    private static final int SHAKE_WAIT_TIME_MS = 250;
    private static final float ROTATION_THRESHOLD = 6.0f;
    private static final int ROTATION_WAIT_TIME_MS = 100;

    private SensorManager mSensorManager;
    private Sensor mAccSensor, mGyroSensor;
    private int mSensorType;
    private long mShakeTime = 0;
    private long mRotationTime = 0;

    private boolean flag_alarm_status = false;
    private boolean flag_rotate_detection_left = false;
    private boolean flag_rotate_detection_right = false;
    //

    private CommandSetFactory commandSetFactory;
    private CommandSet commandSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                //mTextValues = (TextView) stub.findViewById(R.id.values);
                mButtonAlarm = (Button) stub.findViewById(R.id.alarm_toggle);
                mButtonLeft = (Button) stub.findViewById(R.id.rotation_left_toggle);
                mButtonRight = (Button) stub.findViewById(R.id.rotation_right_toggle);
            }
        });

        mSensorType = Sensor.TYPE_ACCELEROMETER;//Sensor.TYPE_GYROSCOPE
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        //mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        // my libary
        commandSetFactory = new CommandSetFactory();
        retrieveCurrentMode();
    }

    private void retrieveCurrentMode() {
        RetrofitAdapterProvider.get()
                .create(Samanda.class)
                .get()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Action1<Result>() {
                    @Override
                    public void call(Result result) {
                        Log.d("tag", result.toString());
                        commandSet = commandSetFactory.mode(result.getMode()).context(MainActivity.this).create();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        //mSensorManager.registerListener(this, mAccSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void requestContext(View view) {
    }

    public void alarmToggle(View view) {
        if(flag_alarm_status) {
            alarmOff();
        } else {
            alarmOn();
        }

        flag_alarm_status = !flag_alarm_status;

        mButtonAlarm.setText("Alarm " + (flag_alarm_status ? "off" : "on"));
    }

    public void alarmOn() {
        Intent toggleAlarmOperation = new Intent(this, FindPhoneService.class);
        toggleAlarmOperation.setAction(FindPhoneService.ACTION_TOGGLE_ALARM);
        startService(toggleAlarmOperation);
    }

    public void alarmOff() {
        Intent cancelAlarmOperation = new Intent(this, FindPhoneService.class);
        cancelAlarmOperation.setAction(FindPhoneService.ACTION_CANCEL_ALARM);
        startService(cancelAlarmOperation);
    }

    public void rotationLeftToggle(View view) {
        rotationLeft(!flag_rotate_detection_left);
    }

    public void rotationLeft(boolean flag) {
        flag_rotate_detection_left = flag;

        Toast.makeText(this, "rotate left " + (flag_rotate_detection_left ? "on" : "off"), Toast.LENGTH_SHORT).show();

        mButtonLeft.setText("Rotation left " + (flag_rotate_detection_left ? "off" : "on"));
    }

    public void rotationRightToggle(View view) {
        rotationRight(!flag_rotate_detection_right);
    }

    public void rotationRight(boolean flag) {
        flag_rotate_detection_right = flag;

        Toast.makeText(this, "rotate right " + (flag_rotate_detection_right ? "on" : "off"), Toast.LENGTH_SHORT).show();

        mButtonRight.setText("Rotation right " + (flag_rotate_detection_right ? "off" : "on"));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // If sensor is unreliable, then just return
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
        {
            return;
        }
/*
        mTextValues.setText(
                "x = " + Float.toString(event.values[0]) + "\n" +
                        "y = " + Float.toString(event.values[1]) + "\n" +
                        "z = " + Float.toString(event.values[2]) + "\n"
        );
*/
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            detectShake(event);
        }
        else if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            detectRotation(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // References:
    //  - http://jasonmcreynolds.com/?p=388
    //  - http://code.tutsplus.com/tutorials/using-the-accelerometer-on-android--mobile-22125
    private void detectShake(SensorEvent event) {
        long now = System.currentTimeMillis();

        if((now - mShakeTime) > SHAKE_WAIT_TIME_MS) {
            mShakeTime = now;

            float gX = event.values[0] / SensorManager.GRAVITY_EARTH;
            float gY = event.values[1] / SensorManager.GRAVITY_EARTH;
            float gZ = event.values[2] / SensorManager.GRAVITY_EARTH;

            // gForce will be close to 1 when there is no movement
            //float gForce = FloatMath.sqrt(gX * gX + gY * gY + gZ * gZ);
            float gForce = (float)Math.sqrt(gX * gX + gY * gY + gZ * gZ);

            if(gForce > SHAKE_THRESHOLD) {
                alarmOff();
            }
            else {
            }
        }
    }

    private void detectRotation(SensorEvent event) {
        long now = System.currentTimeMillis();

        if((now - mRotationTime) > ROTATION_WAIT_TIME_MS) {
            mRotationTime = now;

            // Change background color if rate of rotation around any
            // axis and in any direction exceeds threshold;
            // otherwise, reset the color
            /*
            if(Math.abs(event.values[0]) > ROTATION_THRESHOLD ||
                    Math.abs(event.values[1]) > ROTATION_THRESHOLD ||
                    Math.abs(event.values[2]) > ROTATION_THRESHOLD) {
                    */
            if(Math.abs(event.values[0]) > ROTATION_THRESHOLD) {
                //mView.setBackgroundColor(Color.rgb(0, 100, 0));
                if(flag_rotate_detection_right && event.values[0] > 0) {// right. 안쪽.
                    rotationRight(false);
                } else if(flag_rotate_detection_left && event.values[0] < 0) {// left. 바깥쪽.
                    rotationLeft(false);
                }
            }
            else {
            }
        }
    }
}
