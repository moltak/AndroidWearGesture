package com.horde.samantha.samantha.bus;

/**
 * Created by engeng on 10/31/15.
 */
public class DataEvent {

    public final String command;

    public DataEvent(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
