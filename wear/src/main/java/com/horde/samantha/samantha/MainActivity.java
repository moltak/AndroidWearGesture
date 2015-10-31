package com.horde.samantha.samantha;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
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
    private static final int ROTATION_MIN_WAIT_TIME = 2000;// 좌우 한번에 인식되는 것 방지용
    private static final float FIGHTING_THRESHOLD = 12.0f;

    private SensorManager mSensorManager;
    private Sensor mAccSensor, mGyroSensor;
    private long mShakeTime = 0;
    private long mRotationTime = 0;

    private boolean flag_alarm_status = false;
    private boolean flag_rotate_detection_left = false;
    private boolean flag_rotate_detection_right = false;
    private boolean flag_fighting = false;
    private boolean flag_vibrate = false;
    private boolean flag_call = false;
    private boolean flag_alarm = false;
    private boolean flag_sleep = false;

    private long mRotationDetectTime = 0;

    private Vibrator vibrator;

    private CommandSetFactory commandSetFactory;
    private CommandSet commandSet;

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

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        // my libary
        commandSetFactory = new CommandSetFactory().context(this);
    }

    int COUNT = 0;
    public void requestToMobile(final String special) {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/command");
        putDataMapReq.getDataMap().putString("com.samantha.data.command", special != null ? special + String.valueOf(COUNT): String.valueOf(COUNT));
        COUNT ++;

        PutDataRequest request = putDataMapReq.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(googleApiClient, request);
        pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(final DataApi.DataItemResult result) {
                if (result.getStatus().isSuccess()) {
                    Log.d(TAG, "Item has been sent: " + (special != null ? special + String.valueOf(COUNT) : String.valueOf(COUNT)));
                }
            }
        });
    }
    public void setText(String text) {
        if(mText != null) {
            mText.setText(text);
        }
    }

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

        if(mode.equals(MODE_ARRAY[MODE_WORKOUT])) {
            flag_fighting = true;
        } else if(mode.equals(MODE_ARRAY[MODE_NAVI])) {
            flag_rotate_detection_left = true;
        } else if(mode.equals(MODE_ARRAY[MODE_VIBRATE])) {
            if(!flag_vibrate) {
                wakeWithVibrate();
            }
        } else if(mode.equals(MODE_ARRAY[MODE_CALL])) {
            if(!flag_call) {
                callWithVibrate();
            }
        } else if(mode.equals(MODE_ARRAY[MODE_WAKEUP])) {
            alarmToPhone();
        } else if(mode.equals(MODE_ARRAY[MODE_SLEEP])) {
            playSleepMusicOnThePhone();
        }
    }

    public void playSleepMusicOnThePhone() {
        if(!flag_sleep) {
            requestToMobile("sleep_on");

            flag_sleep = true;
        }
    }

    @Override
    public void onSleepFinish() {
        if(flag_sleep) {
            requestToMobile("sleep_off");

            flag_sleep = false;
        }
    }

    public void alarmToPhone() {
        if(!flag_alarm) {
            makeToast("Wake up ~~~");

            Intent toggleAlarmOperation = new Intent(this, FindPhoneService.class);
            toggleAlarmOperation.setAction(FindPhoneService.ACTION_TOGGLE_ALARM);
            startService(toggleAlarmOperation);

            flag_alarm = true;
        }
    }

    @Override
    public void onAlarmFinish() {
        if(flag_alarm) {
            makeToast("Good morning ~~~");

            Intent cancelAlarmOperation = new Intent(this, FindPhoneService.class);
            cancelAlarmOperation.setAction(FindPhoneService.ACTION_CANCEL_ALARM);
            startService(cancelAlarmOperation);

            flag_alarm = false;
        }
    }

    public void callWithVibrate() {
        makeToast("Phone call ~~~");

        long[] vibrationPattern = {0, 500, 50, 300};
        final int indexInPatternToRepeat = 0;// repeat
        vibrator.vibrate(vibrationPattern, indexInPatternToRepeat);

        //TODO: let mobile reject another call
        //TODO: change image to call screen

        flag_call = true;
    }

    @Override
    public void onCallFinish() {
        if(flag_call) {
            makeToast("Saved life ~~~");

            vibrator.cancel();

            //TODO: back to previous screen

            flag_call = false;
        }
    }

    public void wakeWithVibrate() {
        makeToast("Wake up ~~~");

        long[] vibrationPattern = {0, 500, 50, 300};
        final int indexInPatternToRepeat = 0;// repeat
        vibrator.vibrate(vibrationPattern, indexInPatternToRepeat);

        flag_vibrate = true;
    }

    @Override
    public void onVibrateFinish() {
        if(flag_vibrate) {
            makeToast("Good job ~~~");

            vibrator.cancel();

            flag_vibrate = false;
        }
    }

    @Override
    public void onNaviStart() {
        mRotationDetectTime = System.currentTimeMillis();

        makeToast("Drive go ~~~");
        flag_rotate_detection_left = false;
        flag_rotate_detection_right = true;
    }

    @Override
    public void onNaviFinish() {
        makeToast("Arrived ~~~");
        //flag_rotate_detection_left = true;
        flag_rotate_detection_right = false;
    }

    @Override
    public void onWorkoutStart() {
        makeToast("Run ~~~");
        flag_fighting = false;
    }

    @Override
    public void onWorkoutFinish() {
        makeToast("Welldone ~~~");
        //fighting(true);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // If sensor is unreliable, then just return
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE) {
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
                //alarmOff();
            }
        }
    }

    private void detectFight(SensorEvent event) {
        float gX = event.values[0];// / SensorManager.GRAVITY_EARTH;
        float gY = event.values[1];// / SensorManager.GRAVITY_EARTH;
        float gZ = event.values[2];// / SensorManager.GRAVITY_EARTH;

        if(Math.abs(gX) > FIGHTING_THRESHOLD) {
            commandSet.getShakeCommand().execute();
        }
    }

    private void detectRotation(SensorEvent event) {
        long now = System.currentTimeMillis();

        if((now - mRotationTime) > ROTATION_WAIT_TIME_MS) {
            mRotationTime = now;

            //mText.setText(String.valueOf(event.values[0]));

            // Change background color if rate of rotation around any
            // axis and in any direction exceeds threshold;
            // otherwise, reset the color
            if((now - mRotationDetectTime) > ROTATION_MIN_WAIT_TIME && Math.abs(event.values[0]) > ROTATION_THRESHOLD) {
                if(flag_rotate_detection_right && event.values[0] > 0) {// right. 안쪽.
                    commandSet.getWristRightCommand().execute();
                } else if(flag_rotate_detection_left && event.values[0] < 0) {// left. 바깥쪽.
                    commandSet.getWristLeftCommand().execute();
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
}