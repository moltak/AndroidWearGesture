package net.horde.commandsetlibrary.command.commandsets;

import net.horde.commandsetlibrary.command.Command;
import net.horde.commandsetlibrary.command.CommandSet;
import net.horde.commandsetlibrary.command.CommandSetFactory;

/**
 * Created by engeng on 10/31/15.
 */
public class WorkoutCommandSet implements CommandSet {
    private CommandSetFactory.Callback callback;

    public WorkoutCommandSet(CommandSetFactory.Callback callback) {
        this.callback = callback;
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
                callback.onWorkoutStart();
            }
        };
    }

    @Override
    public Command getWristCoverCommand() {
        return new Command() {
            @Override
            public void execute() {
                callback.onWorkoutFinish();
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
