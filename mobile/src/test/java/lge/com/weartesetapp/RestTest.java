package lge.com.weartesetapp;

import org.junit.Test;

import java.util.concurrent.ExecutionException;

import lge.com.weartesetapp.rest.RetrofitAdapterProvider;
import lge.com.weartesetapp.rest.model.Result;
import lge.com.weartesetapp.rest.service.Samanda;
import rx.Observable;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by engeng on 10/31/15.
 */
public class RestTest {

    @Test
    public void simpleTest() throws ExecutionException, InterruptedException {
        Observable<Result> o = RetrofitAdapterProvider.get()
                .create(Samanda.class)
                .get();

        Result result = o.toBlocking().toFuture().get();
//        assertThat(result.getTime(), is("2015-10-31T15:23:13+09:00"));
        assertThat(result.getMode(), is("init"));
        assertThat(result.getLat(), is(37.507976));
        assertThat(result.getLng(), is(127.045094));
    }
}
