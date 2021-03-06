package com.horde.samantha.samantha;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

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
import com.horde.samantha.samantha.bus.DataEventBus;
import com.horde.samantha.samantha.util.PickBulletineImageByMode;
import com.horde.samantha.samantha.util.PickImageByMode;
import com.horde.samantha.samantha.util.PickStringByMode;
import com.horde.samantha.samantha.util.PickTitleByMode;
import com.horde.samantha.samantha.util.ThirdpartyExectue;
import com.squareup.otto.Subscribe;

import net.horde.commandsetlibrary.rest.RetrofitAdapterProvider;
import net.horde.commandsetlibrary.rest.model.Result;
import net.horde.commandsetlibrary.rest.service.Samanda;

import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements
        DataApi.DataListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private final String TAG = "Wear";
    private GoogleApiClient googleApiClient;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        DataEventBus.getBus().register(this);
        createGoolgeApiClient();
        retrieveMode();
    }

    @Override
    protected void onResume() {
        super.onResume();
        googleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {}

    @Override
    protected void onPause() {
        super.onPause();
        googleApiClient.disconnect();
    }

    @Override
    protected void onDestroy() {
        DataEventBus.getBus().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        for (DataEvent event : dataEvents) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                // DataItem changed
                DataItem item = event.getDataItem();
                if (item.getUri().getPath().compareTo("/mode") == 0) {
                    DataMap dataMap = DataMapItem.fromDataItem(item).getDataMap();
                    Log.d(TAG, dataMap.getString("com.samantha.data.mode"));
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                // DataItem deleted
            }
        }
    }

    int count = 1;

    @Subscribe
    public void onDataEvent(com.horde.samantha.samantha.bus.DataEvent event) {
        String command = event.getCommand();
        Log.d(TAG, "from wear: " + command);
        if(command.startsWith("sleep_on")) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.sleep);
            mediaPlayer.start();
        } else if(command.startsWith("sleep_off")) {
            mediaPlayer.stop();
        } else if(command.startsWith("call_on")) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.ringing);
            mediaPlayer.start();
        } else if(command.startsWith("call_off")) {
            mediaPlayer.stop();
        } else if(command.startsWith("wakeup_on")) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.ringing);
            mediaPlayer.start();
        } else if(command.startsWith("wakeup_off")) {
            mediaPlayer.stop();
        } else if(command.startsWith("lunch_on")) {// off는 특별히 없다.
            ThirdpartyExectue.runMangoplate(this);
        } else if(command.startsWith("navi_on")) {
            ThirdpartyExectue.runNavigation(this);
        } else if(command.startsWith("navi_off")) {
            ThirdpartyExectue.killNavigation(this);
        } else if(command.startsWith("workout_on")) {
            ThirdpartyExectue.runRuntastic(this);
        } else if(command.startsWith("workout_off")) {
            ThirdpartyExectue.killRuntastic(this);
        } else {
            retrieveMode();
        }
    }

    private void createGoolgeApiClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.d(TAG, "onConnected: " + connectionHint);
                        // Now you can use the Data Layer API
                    }
                    @Override
                    public void onConnectionSuspended(int cause) {
                        Log.d(TAG, "onConnectionSuspended: " + cause);
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.d(TAG, "onConnectionFailed: " + result);
                    }
                })// Request access only to the Wearable API
                .addApi(Wearable.API)
                .build();
    }

    private void retrieveMode() {
        RetrofitAdapterProvider.get()
                .create(Samanda.class)
                .get()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Result>() {
                    @Override
                    public void call(Result result) {
                        ((ImageView) findViewById(R.id.imageViewMode)).setImageResource(PickImageByMode.pick(result.getMode()));
                        sendToWidget(result.getMode());
                        sendToWear(result.getMode());
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
    }

    private void sendToWidget(String mode) {
        SharedPreferences sharedPreferences = getSharedPreferences("widget", Context.MODE_PRIVATE);
        sharedPreferences.edit()
                .putInt("image", PickBulletineImageByMode.pick(mode))
                .putString("title", PickStringByMode.pick(mode))
                .putString("mode", PickTitleByMode.pick(mode))
                .commit();

        Intent i = new Intent(this, SamanthaWidget.class);
        i.setAction(SamanthaWidget.SAMANTHA_WIDGET_ACTION);
        sendBroadcast(i);
    }

    private void sendToWear(final String mode) {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/mode");
        putDataMapReq.getDataMap().putString("com.samantha.data.mode", mode);
        PutDataRequest request = putDataMapReq.asPutDataRequest();
        PendingResult<DataApi.DataItemResult> pendingResult =
                Wearable.DataApi.putDataItem(googleApiClient, request);
        pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
            @Override
            public void onResult(final DataApi.DataItemResult result) {
                if (result.getStatus().isSuccess()) {
                    Log.d(TAG, "Item has been sent: " + mode);
                }
            }
        });
    }

    /*
     * uselsess
     */
    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}
}
