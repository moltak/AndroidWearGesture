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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import net.horde.commandsetlibrary.command.CommandSet;
import net.horde.commandsetlibrary.command.CommandSetFactory;

public class MainActivity extends Activity implements
        SensorEventListener,
        CommandSetFactory.Callback,
        DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    TextView mText;

    //TextView mTextValues, mTextValues2;
    Button mButtonAlarm, mButtonLeft, mButtonRight, mButtonFight;

    // modes
    private static final int MODE_WAKEUP = 0;
    private static final int MODE_NAVI = 1;
    private static final int MODE_LUNCH = 2;
    private static final int MODE_VIBRATE = 3;
    private static final int MODE_CALL = 4;
    private static final int MODE_WORKOUT = 5;
    private static final int MODE_SLEEP = 6;
    private static final String[] MODE_ARRAY = new String[]{"wakeup", "navi", "lunch", "vibrate", "call", "workout", "sleep"};

    // sensors
    private static final float SHAKE_THRESHOLD = 1.1f;
    private static final int SHAKE_WAIT_TIME_MS = 250;
    private static final float ROTATION_THRESHOLD = 6.0f;
    private static final int ROTATION_WAIT_TIME_MS = 100;
    private static final float FIGHTING_THRESHOLD = 12.0f;

    private SensorManager mSensorManager;
    private Sensor mAccSensor, mGyroSensor;
    private long mShakeTime = 0;
    private long mRotationTime = 0;

    private boolean flag_alarm_status = false;
    private boolean flag_rotate_detection_left = false;
    private boolean flag_rotate_detection_right = false;
    private boolean flag_fighting = false;

    private CommandSetFactory commandSetFactory;
    private CommandSet commandSet;

    private String mode = null;

    private GoogleApiClient googleApiClient;

    private final String TAG = "Wear";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                //mTextValues = (TextView) stub.findViewById(R.id.values);
                //mTextValues2 = (TextView) stub.findViewById(R.id.values2);
                mButtonAlarm = (Button) stub.findViewById(R.id.alarm_toggle);
                mButtonLeft = (Button) stub.findViewById(R.id.rotation_left_toggle);
                mButtonRight = (Button) stub.findViewById(R.id.rotation_right_toggle);
                mButtonFight = (Button) stub.findViewById(R.id.fighting_toggle);

                mText = (TextView) stub.findViewById(R.id.text);
                mText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        requestToMobile(null);
                    }
                });
                mText.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        commandSet.getWristCoverCommand().execute();

                        return true;
                    }
                });
            }
        });

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        //mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
        // my libary
        commandSetFactory = new CommandSetFactory().context(this);
    }

    public void requestToMobile(View view) {

    }
/*
    public void generateCommandMode() {
        int index;

        while(true) {
            index = (int)(Math.random() * 10) % 7;

            if(!MODE_ARRAY[index].equals(mode)) {// 임시로 중복 방지
                break;
            }
        }

        setText(MODE_ARRAY[index]);
        setCommandMode(MODE_ARRAY[index]);

        switch(index) {
            case MODE_SLEEP:
                break;
            case MODE_WORKOUT:

                break;
            case MODE_CALL:
                break;
            case MODE_VIBRATE:
                break;
            case MODE_LUNCH:
                break;
            case MODE_NAVI:
                break;
            case MODE_WAKEUP:
                break;
        }
    }
*/
    public void setText(String text) {
        if(mText != null) {
            mText.setText(text);
        }
    }
/*
    public void setCommandMode(String mode) {
        this.mode = mode;

        setText(mode);

        commandSet = commandSetFactory.mode(mode).create();
    }
*/
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        googleApiClient.connect();
        mSensorManager.registerListener(this, mAccSensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected");
        Wearable.DataApi.addListener(googleApiClient, this);
    }

    @Override
    public void onPause() {
        Wearable.DataApi.removeListener(googleApiClient, this);
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    public void makeToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d(TAG, "onDataChanged");
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/mode") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    String data = dataMap.getString("com.samantha.data.mode");
                    if (data != null) {
                        createCommandSet(data);
                    }
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }

    private void createCommandSet(String mode) {
        setText(mode);
        commandSet = commandSetFactory.mode(mode).create();
        Log.d("TAG", commandSet.toString());

        sendToMobile(mode);
    }

    private void sendToMobile(String mode) {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/command");
        putDataMapReq.getDataMap().putString("com.samantha.data.command", mode);
        PutDataRequest request = putDataMapReq.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(googleApiClient, request);
        pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(final DataApi.DataItemResult result) {
                if(result.getStatus().isSuccess()) {
                    Log.d(TAG, "Item has been sent: " + result.getDataItem().getUri());
                }
            }
        });
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

        commandSet.getWristCoverCommand().execute();
    }

    public void rotationLeftToggle(View view) {
        rotationLeft(!flag_rotate_detection_left);
    }

    public void rotationLeft(boolean flag) {
        flag_rotate_detection_left = flag;

        //Toast.makeText(this, "rotate left " + (flag_rotate_detection_left ? "on" : "off"), Toast.LENGTH_SHORT).show();

        mButtonLeft.setText("Rotation left " + (flag_rotate_detection_left ? "off" : "on"));

        if(!flag) {
            commandSet.getWristLeftCommand().execute();
        }
    }

    public void rotationRightToggle(View view) {
        rotationRight(!flag_rotate_detection_right);
    }

    public void rotationRight(boolean flag) {
        flag_rotate_detection_right = flag;

        //Toast.makeText(this, "rotate right " + (flag_rotate_detection_right ? "on" : "off"), Toast.LENGTH_SHORT).show();

        mButtonRight.setText("Rotation right " + (flag_rotate_detection_right ? "off" : "on"));

        if(!flag) {
            commandSet.getWristRightCommand().execute();
        }
    }

    public void fightingToggle(View view) {
        fighting(!flag_fighting);
    }

    public void fighting(boolean flag) {
        flag_fighting = flag;

        //Toast.makeText(this, "fighting " + (flag_fighting ? "on" : "off"), Toast.LENGTH_SHORT).show();

        mButtonFight.setText("Fighting " + (flag_fighting ? "off" : "on"));

        if(!flag) {
            commandSet.getShakeCommand().execute();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // If sensor is unreliable, then just return
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
        {
            return;
        }

        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            if(flag_alarm_status) {
                //detectShake(event);
            }

            if(flag_fighting) {
                detectFight(event);
            }
        }

        if(event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            if(flag_rotate_detection_left || flag_rotate_detection_right) {
                detectRotation(event);
            }
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
            float gForce = (float)Math.sqrt(gX * gX + gY * gY + gZ * gZ);

            if(gForce > SHAKE_THRESHOLD) {
                alarmOff();
            }
        }
    }

    private void detectFight(SensorEvent event) {
        float gX = event.values[0];// / SensorManager.GRAVITY_EARTH;
        float gY = event.values[1];// / SensorManager.GRAVITY_EARTH;
        float gZ = event.values[2];// / SensorManager.GRAVITY_EARTH;

        if(Math.abs(gX) > FIGHTING_THRESHOLD) {
            flag_fighting = false;
        }
    }

    private void detectRotation(SensorEvent event) {
        long now = System.currentTimeMillis();

        if((now - mRotationTime) > ROTATION_WAIT_TIME_MS) {
            mRotationTime = now;

            // Change background color if rate of rotation around any
            // axis and in any direction exceeds threshold;
            // otherwise, reset the color
            if(Math.abs(event.values[0]) > ROTATION_THRESHOLD) {
                if(flag_rotate_detection_right && event.values[0] > 0) {// right. 안쪽.
                    rotationRight(false);
                } else if(flag_rotate_detection_left && event.values[0] < 0) {// left. 바깥쪽.
                    rotationLeft(false);
                }
            }
        }
    }

    /*
     * uselsess
     */

    @Override
    public void onConnectionSuspended(int i) {}
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    @Override
    public void onWorkoutStart() {

    }

    @Override
    public void onWorkoutFinish() {

    }
}