package lge.com.weartesetapp.command;

/**
 * Created by engeng on 10/31/15.
 */
public interface CommandSet {
    Command getWristLeftCommand();
    Command getWristRightCommand();
    Command getShakeCommand();
    Command getWristCoverCommand();
    Command getHeartCommand();
}
