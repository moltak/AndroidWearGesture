package net.horde.commandsetlibrary.rest;

import retrofit.RestAdapter;
import retrofit.client.OkClient;


/**
 * Created by engeng on 10/31/15.
 */
public class RetrofitAdapterProvider {
    public static RestAdapter get() {
        return new RestAdapter.Builder()
                .setEndpoint("http://agileup.ml:8888/")
                .setClient(new OkClient())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
    }

    public static RestAdapter test(String url) {
        return new RestAdapter.Builder()
                .setEndpoint(url)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
    }
}
