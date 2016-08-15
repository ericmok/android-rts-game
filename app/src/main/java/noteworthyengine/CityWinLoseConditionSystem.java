package noteworthyengine;

import noteworthyengine.events.GameEvents;
import noteworthyengine.players.PlayerNode;
import noteworthyengine.players.PlayerSystem;
import noteworthyengine.players.PlayerUnit;
import noteworthyengine.units.WinUnit;
import noteworthyframework.Node;
import noteworthyframework.QueueMutationList;
import structure.Game;
import utils.QueueMutationHashedList;

/**
 * Created by eric on 5/13/15.
 */
public class CityWinLoseConditionSystem extends noteworthyframework.System {

    public QueueMutationHashedList<PlayerUnit, CityWinLoseConditionNode> nodesByGamer = new QueueMutationHashedList(7, 63);

    private Game game;
    private PlayerSystem playerSystem;

    public CityWinLoseConditionSystem(Game game, PlayerSystem playerSystem) {
        this.game = game;
        this.playerSystem = playerSystem;
    }

    @Override
    public void addNode(Node node) {
        //if (node instanceof CityWinLoseConditionNode) {
        if (node.getClass() == CityWinLoseConditionNode.class) {
            CityWinLoseConditionNode cityWinLoseConditionNode = (CityWinLoseConditionNode) node;
            //nodesByGamer.getListFor(cityWinLoseConditionNode.gamer.v).queueToAdd(cityWinLoseConditionNode);
            nodesByGamer.queueToAdd(cityWinLoseConditionNode.playerUnitPtr.v, cityWinLoseConditionNode);
        }
    }

    @Override
    public void removeNode(Node node) {
        if (node.getClass() == CityWinLoseConditionNode.class) {
            CityWinLoseConditionNode cityWinLoseConditionNode = (CityWinLoseConditionNode) node;
            //nodesByGamer.getListFor(cityWinLoseConditionNode.gamer.v).queueToRemove(cityWinLoseConditionNode);
            nodesByGamer.queueToRemove(cityWinLoseConditionNode.playerUnitPtr.v, cityWinLoseConditionNode);
        }
    }

    @Override
    public void step(double ct, double dt) {

        int size = nodesByGamer.numberKeys();
        int checks = 0;

        for (int i = 0; i < size; i++) {

            PlayerUnit playerUnit = nodesByGamer.keys.get(i);

            if (playerUnit == playerSystem.getCurrentPlayer()) continue;

            QueueMutationList<CityWinLoseConditionNode> gamerUnits = nodesByGamer.getListFor(playerUnit);

            int numberUnitsForThatGamer = 0;

            if (gamerUnits != null) {
                numberUnitsForThatGamer = gamerUnits.size();
            }

            if (playerUnit != playerSystem.getCurrentPlayer() && numberUnitsForThatGamer == 0) {
                checks += 1;
            }
        }

        // TODO: Deactivate this system once events are emitted

        // Remember we check against number players minus the current player
        if (checks == size - 1) {
            this.getBaseEngine().emitEvent(GameEvents.WIN);
            this.getBaseEngine().removeSystem(this);
        }

        QueueMutationList currentGamerUnits = nodesByGamer.getListFor(playerSystem.getCurrentPlayer());

        if (currentGamerUnits != null && currentGamerUnits.size() == 0) {
            this.getBaseEngine().emitEvent(GameEvents.LOSE);
            this.getBaseEngine().removeSystem(this);
        }
    }

    @Override
    public void flushQueues() {
        nodesByGamer.flushQueues();
    }
}
