package net.horde.commandsetlibrary.command.commandsets;

import android.content.Context;
import android.util.Log;

import net.horde.commandsetlibrary.command.Command;
import net.horde.commandsetlibrary.command.CommandSet;


/**
 * Created by engeng on 10/31/15.
 */
public class FakeModeCommandSet implements CommandSet {
    private final String mode;
    private final Context context;

    public FakeModeCommandSet(String mode, Context context) {
        this.mode = mode;
        this.context = context;
    }

    @Override
    public Command getWristLeftCommand() {
        return new Command() {
            @Override
            public void execute() {
                Log.d("CommandSet", "Fake: wristLeft");
            }
        };
    }

    @Override
    public Command getWristRightCommand() {
        return new Command() {
            @Override
            public void execute() {
                Log.d("CommandSet", "Fake: wristRight");
            }
        };
    }

    @Override
    public Command getShakeCommand() {
        return new Command() {
            @Override
            public void execute() {
                Log.d("CommandSet", "Fake: shake");
            }
        };
    }

    @Override
    public Command getWristCoverCommand() {
        return new Command() {
            @Override
            public void execute() {
                Log.d("CommandSet", "Fake: wristCover");
            }
        };
    }

    @Override
    public Command getHeartCommand() {
        return new Command() {
            @Override
            public void execute() {
                Log.d("CommandSet", "Fake: heart");
            }
        };
    }
}
