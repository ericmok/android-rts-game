package noteworthyengine;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import noteworthyframework.*;
import utils.Vector2;

/**
 * Created by eric on 3/6/15.
 */
public class SeparationSystem extends noteworthyframework.System {

    public QueueMutationList<SeparationNode> nodes = new QueueMutationList<SeparationNode>(127);

    // External system dependency
    //private GridSystem gridSystem;
    private QuadTreeSystem quadTreeSystem;

    private Vector2 temp = new Vector2();

    public SeparationSystem(QuadTreeSystem quadTreeSystem) {
        this.quadTreeSystem = quadTreeSystem;
    }

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

        NoteworthyEngine noteworthyEngine = (NoteworthyEngine)this.getBaseEngine();
        //Grid grid = noteworthyEngine.gridSystem.grid;

        for (int i = 0; i < nodes.size(); i++) {
            SeparationNode node = nodes.get(i);
            node.separationForce.zero();

            //List<GridNode> nearbyNodes = grid.getSurroundingNodes(node.gridX.v, node.gridY.v, 2);
            double radius = 2;
            ArrayList<QuadTreeSystem.QuadTreeNode> nearbyNodes = quadTreeSystem.queryRange(node.coords.pos.x, node.coords.pos.y, radius);

            for (int j = 0; j < nearbyNodes.size(); j++) {

                // Don't separate with self
                //if (i == j) continue;

                //SeparationNode otherNode = (SeparationNode)nearbyNodes.get(j).unit.node(SeparationNode.NAME);
                //SeparationNode otherNode = (SeparationNode)nearbyNodes.get(j)._separationNode;
                Vector2 otherNodePosition = nearbyNodes.get(j).getPosition();

                // Not all gridNodes belong to units that have separationNodes
                //if (otherNode == null || node == otherNode) { continue; }

                double distance = (node.coords.pos.distanceTo(otherNodePosition) + 1);

                if (distance > 2.1) continue;

                distance = distance * distance * distance;

                Vector2.subtract(temp, node.coords.pos, otherNodePosition);
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
