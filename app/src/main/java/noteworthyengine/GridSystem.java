package noteworthyengine;

import noteworthyframework.*;

/**
 * Created by eric on 3/20/15.
 */
public class GridSystem extends noteworthyframework.System {

    public QueueMutationList<GridNode> nodes = new QueueMutationList<GridNode>(127);

    public Grid grid = new Grid(50, 50, 4);

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
    public void initialize() {
        super.initialize();

        grid.clear();
    }

    @Override
    public void step(double ct, double dt) {
        for (int i = nodes.size() - 1; i >= 0; i--) {
            GridNode gridNode = nodes.get(i);
            grid.update(gridNode);
        }
    }

    @Override
    public void flushQueues() {
        for (int i = 0; i < nodes.itemsToAdd.size(); i++) {
            grid.index(nodes.itemsToAdd.get(i));
        }
        for (int i = 0; i < nodes.itemsToRemove.size(); i++) {
            grid.removeIndex(nodes.itemsToRemove.get(i));
        }
        nodes.flushQueues();
    }
}
