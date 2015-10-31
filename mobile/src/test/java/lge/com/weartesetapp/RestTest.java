package lge.com.weartesetapp;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutionException;

import lge.com.weartesetapp.data.RetrofitAdapterProvider;
import retrofit.http.GET;
import rx.Observable;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by engeng on 10/31/15.
 */
public class RestTest {

    private MockWebServer server;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("{\n" +
                "  \"mode\":\"mode\",\n" +
                "  \"time\":\"1\",\n" +
                "  \"lat\":1.0,\n" +
                "  \"lng\":1.0\n" +
                "}"));
        server.start();
    }

    @Test
    public void simpleTest() throws ExecutionException, InterruptedException {
        Observable<Result> o = RetrofitAdapterProvider.test(server.url("/data").toString())
                .create(FakeMockService.class)
                .get();

        Result result = o.toBlocking().toFuture().get();
        assertThat(result.getTime(), is("1"));
        assertThat(result.getMode(), is("mode"));
        assertThat(result.getLat(), is(1.0));
        assertThat(result.getLng(), is(1.0));
    }

    interface FakeMockService {
        @GET("/data")
        Observable<Result> get();
    }

    public class Result {
        String time, mode;
        double lat, lng;

        public String getTime() {
            return time;
        }

        public String getMode() {
            return mode;
        }

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }
    }
}
