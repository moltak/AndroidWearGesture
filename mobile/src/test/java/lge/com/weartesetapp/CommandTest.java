package lge.com.weartesetapp;

import org.junit.Test;

import lge.com.weartesetapp.command.CommandSet;
import lge.com.weartesetapp.command.CommandSetFactory;
import lge.com.weartesetapp.command.commandsets.FakeModeCommandSet;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by engeng on 10/31/15.
 */
public class CommandTest {

    @Test
    public void testCommandSetFactory() {
        CommandSetFactory factory = new CommandSetFactory();
        CommandSet commandSet = factory.mode("mode").context(null).create();
        assertThat(commandSet, instanceOf(FakeModeCommandSet.class));

        assertThat(commandSet.getWristLeftCommand(), notNullValue());
        assertThat(commandSet.getWristRightCommand(), notNullValue());
        assertThat(commandSet.getShakeCommand(), notNullValue());
        assertThat(commandSet.getWristCoverCommand(), notNullValue());
        assertThat(commandSet.getHeartCommand(), notNullValue());
    }
}
