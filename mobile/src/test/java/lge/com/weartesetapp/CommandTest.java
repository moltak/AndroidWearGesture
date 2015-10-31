package lge.com.weartesetapp;

import net.horde.commandsetlibrary.command.CommandSet;
import net.horde.commandsetlibrary.command.CommandSetFactory;
import net.horde.commandsetlibrary.command.commandsets.CallCommandSet;
import net.horde.commandsetlibrary.command.commandsets.FakeModeCommandSet;
import net.horde.commandsetlibrary.command.commandsets.LunchCommandSet;
import net.horde.commandsetlibrary.command.commandsets.NaviCommandSet;
import net.horde.commandsetlibrary.command.commandsets.SleepCommandSet;
import net.horde.commandsetlibrary.command.commandsets.VibrateCommandSet;
import net.horde.commandsetlibrary.command.commandsets.WakeUpCommandSet;
import net.horde.commandsetlibrary.command.commandsets.WorkoutCommandSet;

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

    @Test
    public void testCommandSetFactoryCreate() {
        CommandSetFactory factory = new CommandSetFactory();
        assertThat(factory.context(null).create(), instanceOf(FakeModeCommandSet.class));
        assertThat(factory.mode("wakeup").create(), instanceOf(WakeUpCommandSet.class));
        assertThat(factory.mode("navi").create(), instanceOf(NaviCommandSet.class));
        assertThat(factory.mode("lunch").create(), instanceOf(LunchCommandSet.class));
        assertThat(factory.mode("vibrate").create(), instanceOf(VibrateCommandSet.class));
        assertThat(factory.mode("call").create(), instanceOf(CallCommandSet.class));
        assertThat(factory.mode("workout").create(), instanceOf(WorkoutCommandSet.class));
        assertThat(factory.mode("sleep").create(), instanceOf(SleepCommandSet.class));
    }
}
