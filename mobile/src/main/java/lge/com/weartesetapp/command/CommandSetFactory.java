package lge.com.weartesetapp.command;

import android.content.Context;

import lge.com.weartesetapp.command.commandsets.FakeModeCommandSet;

/**
 * Created by engeng on 10/31/15.
 */
public class CommandSetFactory {
    private String mode;
    private Context context;

    public CommandSetFactory mode(String mode) {
        this.mode = mode;
        return this;
    }


    public CommandSetFactory context(Context context) {
        this.context = context;
        return this;
    }

    public CommandSet create() {
        return new FakeModeCommandSet(mode, context);
    }
}
