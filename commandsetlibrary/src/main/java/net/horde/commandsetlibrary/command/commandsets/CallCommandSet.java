package net.horde.commandsetlibrary.command.commandsets;

import net.horde.commandsetlibrary.command.Command;
import net.horde.commandsetlibrary.command.CommandSet;
import net.horde.commandsetlibrary.command.CommandSetFactory;

/**
 * Created by engeng on 10/31/15.
 */
public class CallCommandSet implements CommandSet {
    private CommandSetFactory.Callback callback;

    public CallCommandSet(CommandSetFactory.Callback callback) {
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

            }
        };
    }

    @Override
    public Command getWristCoverCommand() {
        return new Command() {
            @Override
            public void execute() {
                callback.onCallFinish();
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
