package lge.com.weartesetapp.rest.model;

/**
 * Created by engeng on 10/31/15.
 */
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

    @Override
    public String toString() {
        return "Result{" +
                "time='" + time + '\'' +
                ", mode='" + mode + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                '}';
    }
}
