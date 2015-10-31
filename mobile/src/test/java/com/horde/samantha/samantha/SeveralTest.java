package com.horde.samantha.samantha;

import com.horde.samantha.samantha.util.ISO8601;

import net.horde.commandsetlibrary.rest.RetrofitAdapterProvider;
import net.horde.commandsetlibrary.rest.model.Result;
import net.horde.commandsetlibrary.rest.service.Samanda;

import org.junit.Test;

import java.text.ParseException;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import rx.Observable;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by engeng on 10/31/15.
 */
public class SeveralTest {

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

    @Test
    public void iso8601Test() throws ParseException {
        Calendar c = ISO8601.toCalendar("2015-10-31T15:23:13+09:00", TimeZone.getDefault());
        assertThat(c.get(Calendar.YEAR), is(2015));
        assertThat(c.get(Calendar.MONTH), is(Calendar.OCTOBER));
        assertThat(c.get(Calendar.DATE), is(31));
        assertThat(c.get(Calendar.HOUR_OF_DAY), is(15));
    }
}
