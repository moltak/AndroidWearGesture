package lge.com.weartesetapp.rest.service;

import lge.com.weartesetapp.rest.model.Result;
import retrofit.http.GET;
import rx.Observable;

/**
 * Created by engeng on 10/31/15.
 */
public interface Samanda {
    @GET("/")
    Observable<Result> get();
}
