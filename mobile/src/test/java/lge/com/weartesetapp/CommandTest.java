package lge.com.weartesetapp;

import org.junit.Test;

/**
 * Created by engeng on 10/31/15.
 */
public class CommandTest {

    @Test
    public void testCommandSetFactory() {
        CommandSetFactory factory = new CommandSetFactory();
        CommandSet commandSet = factory.create("mode");
        Command command = commandSet.getWristLeft();
        assertThat(command, isInstanceOf());
    }
}
