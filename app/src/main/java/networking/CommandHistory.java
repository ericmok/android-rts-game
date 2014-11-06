package networking;

import java.util.ArrayList;

/**
 * Created by eric on 11/5/14.
 */
public class CommandHistory {
    public static final int NUMBER_COMMANDS_IN_A_GAME = 8 * 60 * 10 * 10; // 8 taps/sec for 10 minutes * 10 units

    public ArrayList<Command> commands = new ArrayList<Command>(NUMBER_COMMANDS_IN_A_GAME);

    public float lastAck = 0;

    private float mostRecentAckTime = 0;

    /**
     * <p>
     * Inform command history time has passed on the server. Get rid of commands that
     * are less than the server time. We care only about simulating unAck'd commands.
     * </p>
     *
     * <p>
     *  PostConditions:<br/>
     *  CommandHistory.lastAck gets updated and commands gets popped
     * </p>
     *
     * @param time
     */
    public void ack(int time) {
        lastAck = time;

        for (int i = 0; i < commands.size(); i++) {
            if (commands.get(i).timeStamp < time) {
                commands.remove(i);
                i = i - 1;
            }
        }
    }
}
