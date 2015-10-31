package lge.com.weartesetapp;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.junit.Before;
import org.junit.Test;

import retrofit.http.GET;
import rx.Observable;

/**
 * Created by engeng on 10/31/15.
 */
public class RestTest {

    private MockWebServer server;

    @Before
    public void setUp() throws Exception {
        server = new MockWebServer();
        server.enqueue(new MockResponse().setBody("hello, world!"));
        server.start();
    }

    @Test
    public void simpleTest() {
    }

    interface FakeMockService {
        @GET("/data")
        Observable<Result> get();
    }

    public class Result {

    }
}
