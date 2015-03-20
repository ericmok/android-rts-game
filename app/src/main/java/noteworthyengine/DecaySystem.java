package noteworthyengine;

import noteworthyframework.*;

/**
 * Created by eric on 3/19/15.
 */
public class DecaySystem extends noteworthyframework.System {

    public QueueMutationList<DecayNode> nodes = new QueueMutationList<DecayNode>(127);

    @Override
    public void addNode(Node node) {
        if (node.getClass() == DecayNode.class) {
            nodes.queueToAdd((DecayNode)node);
        }
    }

    @Override
    public void removeNode(Node node) {
        if (node.getClass() == DecayNode.class) {
            nodes.queueToRemove((DecayNode)node);
        }
    }

    @Override
    public void step(double ct, double dt) {
        for (int i = nodes.size() - 1; i >= 0; i--) {
            DecayNode decayNode = nodes.get(i);

            if (decayNode.timeToLive.v <= 0) {
                this.getBaseEngine().removeUnit(decayNode.unit);
            }
            else {
                decayNode.timeToLive.v -= dt;
            }
        }
    }

    @Override
    public void flushQueues() {
        nodes.flushQueues();
    }
}
