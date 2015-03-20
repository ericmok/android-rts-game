package noteworthyengine;

import android.util.Log;

import noteworthyframework.*;
import utils.Vector2;

/**
 * Created by eric on 3/6/15.
 */
public class SeparationSystem extends noteworthyframework.System {

    public QueueMutationList<SeparationNode> nodes = new QueueMutationList<SeparationNode>(127);

    private Vector2 temp = new Vector2();

    @Override
    public void addNode(Node node) {
        if (node.getClass() == SeparationNode.class) {
            nodes.queueToAdd((SeparationNode)node);
        }
    }

    @Override
    public void removeNode(Node node) {
        if (node.getClass() == SeparationNode.class) {
            nodes.queueToRemove((SeparationNode)node);
        }
    }

    @Override
    public void step(double ct, double dt) {
        for (int i = 0; i < nodes.size(); i++) {
            SeparationNode node = nodes.get(i);
            node.separationForce.zero();

            for (int j = 0; j < nodes.size(); j++) {

                // Don't separate with self
                if (i == j) continue;

                SeparationNode otherNode = nodes.get(j);

                double distance = (node.coords.pos.distanceTo(otherNode.coords.pos) + 1);

                if (distance > 2) continue;

                distance = distance * distance * distance;

                Vector2.subtract(temp, node.coords.pos, otherNode.coords.pos);
                temp.scale(1 / distance, 1 / distance);

                node.separationForce.translate(temp.x , temp.y);
            }
        }
    }

    @Override
    public void flushQueues() {
        nodes.flushQueues();
    }
}
