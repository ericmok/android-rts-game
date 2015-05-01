package noteworthyengine;

import noteworthyframework.*;

/**
 * Created by eric on 4/30/15.
 */
public class FactorySystem extends noteworthyframework.System {

    public QueueMutationList<FactoryNode> factoryNodes = new QueueMutationList<FactoryNode>(16);

    @Override
    public void addNode(Node node) {
        if (node instanceof FactoryNode) {
            factoryNodes.queueToAdd((FactoryNode)node);
        }
    }

    @Override
    public void removeNode(Node node) {
        if (node instanceof FactoryNode) {
            factoryNodes.queueToRemove((FactoryNode) node);
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
    }

    @Override
    public void flushQueues() {
        factoryNodes.flushQueues();
    }
}
