package noteworthyengine;

import noteworthyengine.events.GameEvents;
import noteworthyengine.units.WinUnit;
import noteworthyframework.Gamer;
import noteworthyframework.Node;
import noteworthyframework.QueueMutationList;
import structure.Game;
import utils.QueueMutationHashedList;

/**
 * Created by eric on 5/13/15.
 */
public class CityWinLoseConditionSystem extends noteworthyframework.System {

    public QueueMutationHashedList<Gamer, CityWinLoseConditionNode> nodesByGamer = new QueueMutationHashedList(7, 63);

    private Game game;

    public CityWinLoseConditionSystem(Game game) {
        this.game = game;
    }

    @Override
    public void addNode(Node node) {
        //if (node instanceof CityWinLoseConditionNode) {
        if (node.getClass() == CityWinLoseConditionNode.class) {
            CityWinLoseConditionNode cityWinLoseConditionNode = (CityWinLoseConditionNode) node;
            //nodesByGamer.getListFor(cityWinLoseConditionNode.gamer.v).queueToAdd(cityWinLoseConditionNode);
            nodesByGamer.queueToAdd(cityWinLoseConditionNode.gamer.v, cityWinLoseConditionNode);
        }
    }

    @Override
    public void removeNode(Node node) {
        if (node.getClass() == CityWinLoseConditionNode.class) {
            CityWinLoseConditionNode cityWinLoseConditionNode = (CityWinLoseConditionNode) node;
            //nodesByGamer.getListFor(cityWinLoseConditionNode.gamer.v).queueToRemove(cityWinLoseConditionNode);
            nodesByGamer.queueToRemove(cityWinLoseConditionNode.gamer.v, cityWinLoseConditionNode);
        }
    }

    @Override
    public void step(double ct, double dt) {
        Gamer currentGamer = this.getBaseEngine().currentGamer;

        int size = nodesByGamer.numberKeys();
        int checks = 0;

        for (int i = 0; i < size; i++) {

            Gamer gamer = nodesByGamer.keys.get(i);

            if (gamer == currentGamer) continue;

            QueueMutationList<CityWinLoseConditionNode> gamerUnits = nodesByGamer.getListFor(gamer);

            int numberUnitsForThatGamer = 0;

            if (gamerUnits != null) {
                numberUnitsForThatGamer = gamerUnits.size();
            }

            if (gamer != currentGamer && numberUnitsForThatGamer == 0) {
                checks += 1;
            }
        }

        // Remember we check against number players minus the current player
        if (checks == size - 1) {
            this.getBaseEngine().emitEvent(GameEvents.WIN);
        }

        QueueMutationList currentGamerUnits = nodesByGamer.getListFor(currentGamer);

        if (currentGamerUnits != null && currentGamerUnits.size() == 0) {
            this.getBaseEngine().emitEvent(GameEvents.LOSE);
        }
    }

    @Override
    public void flushQueues() {
        nodesByGamer.flushQueues();
    }
}
