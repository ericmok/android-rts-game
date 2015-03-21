package noteworthyengine;

import noteworthyframework.*;

/**
 * Created by eric on 3/20/15.
 */
public class GridSystem extends noteworthyframework.System {

    public QueueMutationList<GridNode> nodes = new QueueMutationList<GridNode>(127);

    public Grid grid = new Grid(100, 100, 4);

    @Override
    public void addNode(Node node) {
        if (node.getClass() == GridNode.class) {
            nodes.queueToAdd((GridNode)node);
        }
    }

    @Override
    public void removeNode(Node node) {
        if (node.getClass() == GridNode.class) {
            nodes.queueToRemove((GridNode)node);
        }
    }

    @Override
    public void step(double ct, double dt) {
        grid.clear();

        for (int i = nodes.size() - 1; i >= 0; i--) {
            GridNode gridNode = nodes.get(i);
            grid.index(gridNode);

            gridNode.gridX.v = grid.getBucketX(gridNode.coords.pos.x);
            gridNode.gridY.v = grid.getBucketX(gridNode.coords.pos.y);
        }
    }

    @Override
    public void flushQueues() {
        nodes.flushQueues();
    }
}
