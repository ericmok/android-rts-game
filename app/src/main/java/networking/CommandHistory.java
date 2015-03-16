package networking;

import java.util.ArrayList;

import structure.Game;

/**
 * Created by eric on 11/5/14.
 */
public class CommandHistory {
    public static final int NUMBER_COMMANDS_IN_A_GAME = 8 * 60 * 10 * 10; // 8 taps/sec for 10 minutes * 10 units

    public Game game;

    public ArrayList<Command> commands = new ArrayList<Command>(NUMBER_COMMANDS_IN_A_GAME);

    public double lastAck = 0;

    private double mostRecentAckTime = 0;

    /**
     * Needs access to game pool to recycle commands
     * @param game
     * @return
     */
    public CommandHistory (Game game) {
        this.game = game;
    }

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
    public void ack(double time) {
        lastAck = time;

        for (int i = 0; i < commands.size(); i++) {
            if (commands.get(i).timeStamp <= time) {
                //game.gamePool.commands.recycleMemory(commands.remove(i));
                i = i - 1;
            }
        }
    }
}
