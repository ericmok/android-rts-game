package noteworthyengine.players;

import noteworthyframework.*;
import structure.Game;

/**
 * Created by eric on 2/2/16.
 */
public class PlayerSystem extends noteworthyframework.System {

    public QueueMutationList<PlayerNode> players = new QueueMutationList<>(8);

    private Game game;
    private PlayerUnit currentPlayer;

    public PlayerSystem(Game game) {
        this.game = game;
    }

    public void setCurrentPlayer(PlayerUnit playerUnit) {
        this.currentPlayer = playerUnit;
    }

    public PlayerUnit getCurrentPlayer() {
        return (PlayerUnit)this.currentPlayer;
    }

    @Override
    public void addNode(Node node) {
        if (node instanceof PlayerNode) {
            players.queueToAdd((PlayerNode)node);
        }
    }

    @Override
    public void removeNode(Node node) {
        if (node instanceof PlayerNode) {
            players.queueToRemove((PlayerNode) node);
        }
    }

    @Override
    public void step(double ct, double dt) {

    }

    @Override
    public void flushQueues() {
        players.flushQueues();
    }
}
