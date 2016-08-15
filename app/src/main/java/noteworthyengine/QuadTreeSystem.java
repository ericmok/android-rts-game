package noteworthyengine;

import java.util.ArrayList;

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
    private QTree<QuadTreeNode> qTree = new QTree(100, 200, (byte) 1);

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

    public void useMeasure(QTree.DistanceMeasurable distanceMeasurable) {
        qTree.useMeasure(distanceMeasurable);
    }

    public QuadTreeNode queryClosestTo(QuadTreeNode quadTreeNode) {
        return qTree.queryClosestTo(quadTreeNode);
    }

    public ArrayList<QuadTreeNode> queryRange(double x, double y, double range) {
        return qTree.queryRange(x, y, range);
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
        public QTree.QTreeNode qTreeNode;

        public QuadTreeNode(Unit unit) {
            super(QuadTreeNode.class, unit);
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
            this.qTreeNode = qTreeNode;
        }

        @Override
        public QTree.QTreeNode getQTreeNode() {
            return qTreeNode;
        }
    }

}
