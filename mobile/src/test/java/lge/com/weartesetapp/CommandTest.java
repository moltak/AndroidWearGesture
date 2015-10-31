package lge.com.weartesetapp;

import net.horde.commandsetlibrary.command.CommandSet;
import net.horde.commandsetlibrary.command.CommandSetFactory;
import net.horde.commandsetlibrary.command.commandsets.FakeModeCommandSet;

import org.junit.Test;


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
