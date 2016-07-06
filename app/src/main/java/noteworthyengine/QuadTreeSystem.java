package noteworthyengine;

import java.util.ArrayList;

import noteworthyframework.Coords;
import noteworthyframework.Node;
import noteworthyframework.QueueMutationList;
import noteworthyframework.System;
import noteworthyframework.Unit;
import utils.MemoryPool;
import utils.Vector2;

/**
 * Created by eric on 5/27/16.
 */
public class QuadTreeSystem extends System {

    public QueueMutationList<QuadTreeNode> nodes = new QueueMutationList<>(100);
    public QTree qTree = new QTree(100, 200);

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
        qTree.clear();

        for (int i = 0; i < nodes.size(); i++) {
            qTree.add(nodes.get(i));
        }
    }

    @Override
    public void flushQueues() {
        nodes.flushQueues();
    }

    public static class QuadTreeNode extends Node implements Positionable {
        public Coords coords;

        public QuadTreeNode(Unit unit) {
            super(QuadTreeNode.class.getSimpleName(), unit);
            Node.instantiatePublicFieldsForUnit(unit, QuadTreeNode.class, this);
        }

        @Override
        public Vector2 getPosition() {
            return coords.pos;
        }
    }

    public static interface Positionable {
        public Vector2 getPosition();
    }

    /**
     * Backing data structure of the system.
     * Based on: https://en.wikipedia.org/wiki/Quadtree
     *
     * QuadTreeSystem should not be confused with QTree.
     * QuadTreeNodes should not to be confused with QTreeNodes.
     */
    public static class QTree<T extends Positionable> {
        private static MemoryPool<QTreeNode> QTreeNodeMemoryPool;
        private ArrayList<T> RESULTS;

        public QTreeNode root = null;
        private double width = 0;

        public QTree(int maxCapacity, double width) {
            QTreeNodeMemoryPool = new MemoryPool<>(QTreeNode.class, maxCapacity);

            // TODO: Select a better capacity value
            RESULTS = new ArrayList<>(maxCapacity / 4);

            this.width = width;
        }

        public void add(T item) {
            if (root == null) {
                QTreeNode.create(0, 0, width);
            }

            root.add(item);
        }

        public ArrayList queryRange(double x, double y, double width) {
            RESULTS.clear();

            if (root != null) {
                return root.queryRange(x, y, width, RESULTS);
            }

            return RESULTS;
        }

        public void clear() {
            root.recycle();
            root = null;
        }

        public static class QTreeNode<T extends Positionable> {
            public static final int NUMBER_CHILDREN = 4;
            public static final int NUMBER_POINTS = 4;

            public QTreeNode() { }

            public static QTreeNode create(double x, double y, double width) {
                QTreeNode qTreeNode = QTreeNodeMemoryPool.fetchMemory();
                qTreeNode.squareBoundary.x = x;
                qTreeNode.squareBoundary.y = y;
                qTreeNode.squareBoundary.width = width;
                qTreeNode.parent = null;
                qTreeNode.items.clear();
                return qTreeNode;
            }

            public void recycle() {
                if (northWest != null) {
                    northWest.recycle();
                }
                if (northEast != null) {
                    northEast.recycle();
                }
                if (southWest != null) {
                    southWest.recycle();
                }
                if (southEast != null) {
                    southEast.recycle();
                }
                northWest = null;
                northEast = null;
                southWest = null;
                southEast = null;
                QTreeNodeMemoryPool.recycleMemory(this);
            }

            public QTreeNode parent = null;
            public ArrayList<T> items = new ArrayList<>(NUMBER_POINTS);

            public QTreeNode northWest = null;
            public QTreeNode northEast = null;
            public QTreeNode southWest = null;
            public QTreeNode southEast = null;

            public SquareBoundary squareBoundary = new SquareBoundary();

            public boolean containsPoint(double x, double y) {
                return this.squareBoundary.containsPoint(x, y);
            }

            public boolean add(T item) {

                // Point doesn't belong in region
                if (!this.containsPoint(item.getPosition().x, item.getPosition().y)) {
                    return false;
                }

                if (items.size() < NUMBER_CHILDREN) {
                    items.add(item);
                    return true;
                }
                else {
                    // Subdivide

                    if (northWest == null) {
                        subdivide();

                        if (northWest.add(item)) return true;
                        if (northEast.add(item)) return true;
                        if (southWest.add(item)) return true;
                        if (southEast.add(item)) return true;
                    }
                }

                return false;
            }

            /**
             * Constructs children nodes and sets their boundaries up
             */
            public void subdivide() {
                if (northWest == null) {
                    // TODO: Think about / Test precision errors...
                    northWest = QTreeNode.create(this.squareBoundary.x / 4, this.squareBoundary.y / 4, this.squareBoundary.width / 2);
                    northEast = QTreeNode.create(this.squareBoundary.x * 3 / 4, this.squareBoundary.y / 4, this.squareBoundary.width / 2);
                    southWest = QTreeNode.create(this.squareBoundary.x / 4, this.squareBoundary.y * 3 / 4, this.squareBoundary.width / 2);
                    southEast = QTreeNode.create(this.squareBoundary.x * 3 / 4, this.squareBoundary.y / 4, this.squareBoundary.width / 2);
                }
            }
            public ArrayList queryRange(double x, double y, double range, ArrayList results) {

                if (!this.squareBoundary.intersectsAABB(x, y, range)) {
                    return results;
                }

                for (int i = 0; i < items.size(); i++) {
                    results.add(items.get(i));
                }

                if (northWest != null) {
                    northWest.queryRange(x, y, range, results);
                    northEast.queryRange(x, y, range, results);
                    southWest.queryRange(x, y, range, results);
                    southEast.queryRange(x, y, range, results);
                }

                return results;
            }

            public static class SquareBoundary {
                double x = 0;
                double y = 0;
                double width = 1;

                public boolean containsPoint(double inX, double inY) {
                    return inX >= x - width/2 && inX <= x + width/2 &&
                            inY >= y - width/2 && inY >= y + width/2;
                }

                public boolean intersectsAABB(double inX, double inY, double inWidth) {
                    return (Math.abs(inX - x) * 2 < (width + inWidth)) &&
                            (Math.abs(inY - y) * 2 < (width + inWidth));
                }
            }
        }
    }
}
