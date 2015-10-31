package net.horde.commandsetlibrary.command;

import android.content.Context;

import net.horde.commandsetlibrary.command.commandsets.CallCommandSet;
import net.horde.commandsetlibrary.command.commandsets.FakeModeCommandSet;
import net.horde.commandsetlibrary.command.commandsets.LunchCommandSet;
import net.horde.commandsetlibrary.command.commandsets.NaviCommandSet;
import net.horde.commandsetlibrary.command.commandsets.SleepCommandSet;
import net.horde.commandsetlibrary.command.commandsets.VibrateCommandSet;
import net.horde.commandsetlibrary.command.commandsets.WakeUpCommandSet;
import net.horde.commandsetlibrary.command.commandsets.WorkoutCommandSet;

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
        switch (mode) {
            case "wakeup":
                return new WakeUpCommandSet();
            case "navi":
                return new NaviCommandSet();
            case "lunch":
                return new LunchCommandSet();
            case "vibrate":
                return new VibrateCommandSet();
            case "call":
                return new CallCommandSet();
            case "workout":
                return new WorkoutCommandSet();
            case "sleep":
                return new SleepCommandSet();
        }

        return new FakeModeCommandSet(mode, context);
    }
}
