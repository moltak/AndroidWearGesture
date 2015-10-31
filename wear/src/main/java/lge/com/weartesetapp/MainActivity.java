package lge.com.weartesetapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;

import net.horde.commandsetlibrary.command.CommandSet;
import net.horde.commandsetlibrary.command.CommandSetFactory;
import net.horde.commandsetlibrary.rest.RetrofitAdapterProvider;
import net.horde.commandsetlibrary.rest.model.Result;
import net.horde.commandsetlibrary.rest.service.Samanda;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MainActivity extends Activity {

    private CommandSetFactory commandSetFactory;
    private CommandSet commandSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override public void onLayoutInflated(WatchViewStub stub) {
                final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
                pager.setAdapter(new SensorFragmentPagerAdapter(getFragmentManager()));

                DotsPageIndicator indicator = (DotsPageIndicator) findViewById(R.id.page_indicator);
                indicator.setPager(pager);
            }
        });

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
}