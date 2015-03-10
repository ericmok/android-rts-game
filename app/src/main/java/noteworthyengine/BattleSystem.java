package noteworthyengine;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by eric on 3/6/15.
 */
public class BattleSystem extends System {

    public QueueMutationList<BattleNode> battleNodes = new QueueMutationList<BattleNode>(127);

    public Hashtable<Gamer, QueueMutationList<BattleNode>> battleNodesByGamer =
            new Hashtable<Gamer, QueueMutationList<BattleNode>>(8);

    public ArrayList<Gamer> gamers = new ArrayList<Gamer>(8);

    public BattleSystem() {
    }

    @Override
    public void addNode(Node node) {
        if (node.getClass() == BattleNode.class) {
            BattleNode battleNode = (BattleNode) node;
            battleNodes.queueToAdd(battleNode);

            QueueMutationList gamerUnits = battleNodesByGamer.get(battleNode.gamer);
            if (gamerUnits == null) {
                gamerUnits = new QueueMutationList<BattleNode>(127);
                battleNodesByGamer.put(battleNode.gamer, gamerUnits);
                gamers.add(battleNode.gamer);
            }
            gamerUnits.queueToAdd(battleNode);
        }
    }

    @Override
    public void removeNode(Node node) {
        if (node.getClass() == BattleNode.class) {
            BattleNode battleNode = (BattleNode) node;
            battleNodes.queueToRemove((BattleNode)node);

            QueueMutationList gamerUnits = battleNodesByGamer.get(battleNode.gamer);
            if (gamerUnits != null) {
                gamerUnits.queueToRemove(battleNode);
            }
        }
    }

    @Override
    public void step(EngineDataPack engineDataPack, double ct, double dt) {
        for (int i = 0; i < gamers.size(); i++) {
            Gamer gamer = gamers.get(i);
            QueueMutationList<BattleNode> gamerUnits = battleNodesByGamer.get(gamer);

            for (int j = 0; j < gamerUnits.size(); j++) {
                BattleNode battleNode = gamerUnits.get(j);

                for (int k = 0; k < gamers.size(); k++) {
                    Gamer otherGamer = gamers.get(k);
                    QueueMutationList<BattleNode> otherGamerUnits = battleNodesByGamer.get(otherGamer);

                    for (int m = 0; m < otherGamerUnits.size(); m++) {
                        BattleNode otherBattleNode = otherGamerUnits.get(m);

                        // PHEW!!!
                        double distance = battleNode.coords.pos.distanceTo(otherBattleNode.coords.pos);
                        if (distance < battleNode.targetAcquisitionRange.v) {
                            battleNode.onTargetAcquired.apply(otherBattleNode);

                            if (battleNode.attackState.v == 0) {

                                battleNode.onAttack.apply(otherBattleNode);

                                // For demo
                                battleNode.hp.v -= 1 / dt;
                                otherBattleNode.hp.v -= 1 / dt;
                            }
                        }
                    }
                }

            }
        }
    }

    @Override
    public void flushQueues() {
        battleNodes.flushQueues();
    }
}
