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
    private String mode = "init";
    private Context context;

    public interface Callback {
        void onNaviStart();
        void onNaviFinish();
        void onWorkoutStart();
        void onWorkoutFinish();
        void onVibrateFinish();
        void onCallFinish();
        void onAlarmFinish();
    }

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
                return new WakeUpCommandSet((Callback) context);
            case "navi":
                return new NaviCommandSet((Callback) context);
            case "lunch":
                return new LunchCommandSet((Callback) context);
            case "vibrate":
                return new VibrateCommandSet((Callback) context);
            case "call":
                return new CallCommandSet((Callback) context);
            case "workout":
                return new WorkoutCommandSet((Callback) context);
            case "sleep":
                return new SleepCommandSet((Callback) context);
        }

        return new FakeModeCommandSet(mode, context);
    }
}
