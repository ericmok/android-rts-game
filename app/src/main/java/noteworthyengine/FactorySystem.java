package noteworthyengine;

import java.util.Hashtable;

import noteworthyframework.*;

/**
 * Created by eric on 4/30/15.
 */
public class FactorySystem extends noteworthyframework.System {

    public QueueMutationList<FactoryNode> factoryNodes = new QueueMutationList<FactoryNode>(16);
    public QueueMutationList<FactoryCounterNode> factoryCounterNodes = new QueueMutationList<>(16);
    public Hashtable<Gamer, Integer> factoryCountByGamer = new Hashtable<>();

    @Override
    public void addNode(Node node) {
        if (node instanceof FactoryNode) {
            FactoryNode factoryNode = (FactoryNode) node;
            factoryNodes.queueToAdd(factoryNode);
            if (!factoryCountByGamer.containsKey(factoryNode.gamer.v)) {
                factoryCountByGamer.put(factoryNode.gamer.v, 0);
            }
            factoryCountByGamer.put(factoryNode.gamer.v, factoryCountByGamer.get(factoryNode.gamer.v) + 1);
        }
        if (node instanceof FactoryCounterNode) {
            factoryCounterNodes.queueToAdd((FactoryCounterNode)node);
        }
    }

    @Override
    public void removeNode(Node node) {
        if (node instanceof FactoryNode) {
            FactoryNode factoryNode = (FactoryNode) node;
            factoryNodes.queueToRemove(factoryNode);
            if (!factoryCountByGamer.containsKey(factoryNode.gamer.v)) {
                factoryCountByGamer.put(factoryNode.gamer.v, 0);
            }
            factoryCountByGamer.put(factoryNode.gamer.v, factoryCountByGamer.get(factoryNode.gamer.v) - 1);
        }
        if (node instanceof FactoryCounterNode) {
            factoryCounterNodes.queueToRemove((FactoryCounterNode) node);
        }
    }

    @Override
    public void step(double ct, double dt) {
        int size = factoryNodes.size();

        for (int i = 0; i < size; i++) {
            FactoryNode factoryNode = factoryNodes.get(i);
            factoryNode.buildProgress.v += dt;

            if (factoryNode.buildProgress.v >= factoryNode.buildTime.v) {
                factoryNode.buildProgress.v = 0;
                factoryNode.spawnFunction.apply(this, factoryNode);
            }
        }

        size = factoryCounterNodes.size();

        for (int i = 0; i < size; i++) {
            FactoryCounterNode factoryCounterNode = factoryCounterNodes.get(i);
            factoryCounterNode.numberFactories.v = factoryCountByGamer.get(factoryCounterNode.gamer.v);
        }
    }

    @Override
    public void flushQueues() {
        factoryNodes.flushQueues();
        factoryCounterNodes.flushQueues();
    }
}
