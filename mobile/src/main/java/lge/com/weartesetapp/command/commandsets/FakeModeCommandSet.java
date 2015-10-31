package lge.com.weartesetapp.command.commandsets;

import android.content.Context;

import lge.com.weartesetapp.command.Command;
import lge.com.weartesetapp.command.CommandSet;

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
            }
        };
    }

    @Override
    public Command getWristRightCommand() {
        return new Command() {
            @Override
            public void execute() {
            }
        };
    }

    @Override
    public Command getShakeCommand() {
        return new Command() {
            @Override
            public void execute() {
            }
        };
    }

    @Override
    public Command getWristCoverCommand() {
        return new Command() {
            @Override
            public void execute() {
            }
        };
    }

    @Override
    public Command getHeartCommand() {
        return new Command() {
            @Override
            public void execute() {
            }
        };
    }
}
