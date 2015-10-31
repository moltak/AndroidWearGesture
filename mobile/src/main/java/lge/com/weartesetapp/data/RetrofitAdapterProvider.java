package lge.com.weartesetapp.data;

import com.squareup.okhttp.OkHttpClient;

import retrofit.Retrofit;

/**
 * Created by engeng on 10/31/15.
 */
public class RetrofitAdapterProvider {
    public static Retrofit get() {
        return new Retrofit.Builder()
                .baseUrl("https://api.github.com")
                .client(new OkHttpClient())
                .build();
    }
}
