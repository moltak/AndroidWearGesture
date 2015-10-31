package lge.com.weartesetapp.rest;

import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by engeng on 10/31/15.
 */
public class RetrofitAdapterProvider {
    public static RestAdapter get() {
        return new RestAdapter.Builder()
                .setEndpoint("http://172.20.3.103:10001")
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
