package com.horde.samantha.samantha.bus;

import com.squareup.otto.Bus;

/**
 * Created by engeng on 10/31/15.
 */
public class DataEventBus {
    public static Bus bus;

    public static Bus getBus() {
        if (bus == null) {
            synchronized (DataEventBus.class) {
                if (bus == null) {
                    bus = new Bus();
                }
            }
        }

        return bus;
    }
}
