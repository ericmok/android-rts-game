package noteworthyengine;

import noteworthyframework.Coords;
import noteworthyframework.Node;
import noteworthyframework.QueueMutationList;
import noteworthyframework.System;
import noteworthyframework.Unit;
import utils.QTree;
import utils.Vector2;

/**
 * Created by eric on 5/27/16.
 */
public class QuadTreeSystem extends System {

    public QueueMutationList<QuadTreeNode> nodes = new QueueMutationList<>(100);
    public QTree qTree = new QTree(100, 200, (byte)1);

    @Override
    public void addNode(Node node) {
        if (node instanceof QuadTreeNode) {
            nodes.queueToAdd((QuadTreeNode)node);
        }
    }

    @Override
    public void removeNode(Node node) {
        if (node instanceof QuadTreeNode) {
            nodes.queueToRemove((QuadTreeNode)node);
        }
    }

    private void index(QuadTreeNode node) {

    }

    @Override
    public void step(double ct, double dt) {
        // Important if other systems make use of the QTree and overwrite the metric.
        qTree.useMeasure(QTree.SQUARED_EUCLIDEAN_DISTANCE);

        qTree.clear();

        for (int i = 0; i < nodes.size(); i++) {
            qTree.add(nodes.get(i));
        }
    }

    @Override
    public void flushQueues() {
        nodes.flushQueues();
    }

    public static class QuadTreeNode extends Node implements QTree.Positionable {
        public static String _NAME = "QuadTreeNode";
        public Coords coords;
        public QTree.QTreeNode _qtTreeNode;

        public QuadTreeNode(Unit unit) {
            super(QuadTreeNode.class.getSimpleName(), unit);
            Node.instantiatePublicFieldsForUnit(unit, QuadTreeNode.class, this);
        }

        @Override
        public Vector2 getPosition() {
            return coords.pos;
        }

        @Override
        public QuadTreeNode getData() {
            return this;
        }

        @Override
        public void setQTreeNode(QTree.QTreeNode qTreeNode) {
            _qtTreeNode = qTreeNode;
        }

        @Override
        public QTree.QTreeNode getQTreeNode() {
            return _qtTreeNode;
        }
    }

}
