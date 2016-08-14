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

    /**
     * Backing data structure of the system.
     * Based on: https://en.wikipedia.org/wiki/Quadtree
     *
     * QuadTreeSystem should not be confused with QTree.
     * QuadTreeNodes should not to be confused with QTreeNodes.
     */
    public static class QTree<T extends QTree.Positionable> {
        private static MemoryPool<QTreeNode> QTreeNodeMemoryPool;

        /** Used to avoid stack allocation */
        private BestCandidate<T> bestCandidateToReturn = new BestCandidate<T>();

        private ArrayList<T> RESULTS;

        public QTreeNode root = null;
        private double width = 0;

        // This number is needed for testing
        private byte numberItemsPerNode = 3;

        public QTree(int maxCapacity, double width, byte numberItemsPerNode) {
            QTreeNodeMemoryPool = new MemoryPool<>(QTreeNode.class, maxCapacity);

            // TODO: Select a better capacity value
            RESULTS = new ArrayList<>(maxCapacity / 4);

            this.width = width;
            this.numberItemsPerNode = numberItemsPerNode;
        }

        public void add(T item) {
            if (root == null) {
                root = QTreeNode.create(0, 0, width, numberItemsPerNode);
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

        /**
         * Find closest item that is not itself within the range
         * @param item
         * @return
         */
        public T queryClosestTo(T item) {

            // The item might have been positioned right above a point that is even
            // a smaller QTNode
            QTreeNode initial =
                    root.findSmallestQTNodeForPoint(item.getPosition().x, item.getPosition().y);

            // TODO: Set all nodes to !isVisited or use a random number for isVisited
            bestCandidateToReturn.item = null;
            bestCandidateToReturn.sqDist = Double.MAX_VALUE;
            return (T)queryClosestToRecursive(item, initial, bestCandidateToReturn).item;
        }

        private boolean visitNodeTest(QTreeNode<T> node, double intersectX, double intersectY, double intersectWidth) {
            if (node != null) {
                if (!node.isVisited) {
                    if (node.squareBoundary.intersectsAABB(intersectX, intersectY, intersectWidth)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public BestCandidate<T> queryClosestToRecursive(T item, QTreeNode<T> node, BestCandidate<T> bestCandidateOut) {

            // Post order traversal
            node.isHalfVisited = true;

            if (node.northWest != null) {
                //double quadrantX = node.squareBoundary.x - item.getPosition().x;
                //double quadrantY = node.squareBoundary.y - item.getPosition().y;

                // Allow bestCandidate to be overwritten several times.
                // The bestCandidate is only written to if a better candidate is found

                if (visitNodeTest(node.northWest, item.getPosition().x, item.getPosition().y, bestCandidateOut.sqDist)) {
                    queryClosestToRecursive(item, node.northWest, bestCandidateOut);
                }
                if (visitNodeTest(node.northEast, item.getPosition().x, item.getPosition().y, bestCandidateOut.sqDist)) {
                    queryClosestToRecursive(item, node.northEast, bestCandidateOut);
                }
                if (visitNodeTest(node.southWest, item.getPosition().x, item.getPosition().y, bestCandidateOut.sqDist)) {
                    queryClosestToRecursive(item, node.southWest, bestCandidateOut);
                }
                if (visitNodeTest(node.southEast, item.getPosition().x, item.getPosition().y, bestCandidateOut.sqDist)) {
                    queryClosestToRecursive(item, node.southEast, bestCandidateOut);
                }
            }

            // Leaf node

            int numberItems = node.items.size(); // cache

            // Try to find an item to overwrite BestCandidate data with
            for (int i = 0; i < numberItems; i++) {

                T candidateItem = node.items.get(i);
                if (candidateItem != item) {

                    if (bestCandidateOut.item != null) {

                        double sqDistToCandidate = item.getPosition().squaredDistanceTo(candidateItem.getPosition());
//                                candidateItem.getPosition().x * bestCandidateOut.item.getPosition().x +
//                                        candidateItem.getPosition().y * bestCandidateOut.item.getPosition().y;

                        if (sqDistToCandidate < bestCandidateOut.sqDist) {
                            bestCandidateOut.item = candidateItem;
                            bestCandidateOut.sqDist = sqDistToCandidate;
                        }
                    }
                    else {
                        bestCandidateOut.item = candidateItem;
                        bestCandidateOut.sqDist = item.getPosition().squaredDistanceTo(candidateItem.getPosition());
                        //bestCandidateOut.sqDist = candidateItem.getPosition().x * item.getPosition().x +
                        //                    candidateItem.getPosition().y * item.getPosition().y;
                    }
                }
            }

            node.isVisited = true;

            if (visitNodeTest(node.parent, item.getPosition().x, item.getPosition().y, bestCandidateOut.sqDist)
                    && node.parent.isHalfVisited == false) {
                queryClosestToRecursive(item, node.parent, bestCandidateOut);
            }

            return bestCandidateOut; // Not needed since we mutate it anyway
        }

//        public ArrayList<T> queryClosestTo(QTreeNode node, ArrayList results) {
//
//            QTreeNode point = findSmallestQTNodeForPoint(node.squareBoundary.x, node.squareBoundary.y);
//
//            this.isVisited = true;
//
//            // Go postorder in tree to find a point
//            // Set range search based on that point
//            // For postorder to work, we bias the quadrant search
//
//            return results;
////
////                node.isVisited = true;
////
////                if (node.northWest != null) {
////                    //double quadrantX = node.squareBoundary.x - node.getPosition().x;
////                    if (!northWest.isVisited) queryClosestTo(northWest, results);
////                    if (!northEast.isVisited) queryClosestTo(northEast, results);
////                    if (!southWest.isVisited) queryClosestTo(southWest, results);
////                    if (!southEast.isVisited) queryClosestTo(southEast, results);
////                }
////                else {
////                    // Leaf node
////
//////                    int numberItems = items.size();
//////                    for (int i = 0; i < numberItems; i++) {
//////                        results.add(items.get(i));
//////                    }
////                    QTreeNode anyNode;
////
////                    if (items.size() > 0) {
////                        anyNode = items.get(0).getQTreeNode();
////                    }
////
////                    if (node.parent != null) {
////                        queryClosestTo(node.parent, results);
////                    }
////                    return queryClosestTo(node.parent, results);
////                }
//        }

//        public ArrayList<T> queryClosestTo(T item, ArrayList results) {
//            QTreeNode node = root.findSmallestQTNodeForPoint(item.getPosition().x, item.getPosition().y);
//            queryClosestTo(node, results);
//            return null;
//        }

        public void clear() {
            if (root != null) {
                root.recycle();
            }
            root = null;
        }

        public static interface Positionable {
            public Vector2 getPosition();
            public Object getData();
            public void setQTreeNode(QTreeNode qTreeNode);
            public QTreeNode getQTreeNode();
        }

        public static class BestCandidate<T> implements Comparable<BestCandidate<T>> {
            public double sqDist = Double.MAX_VALUE;
            public T item;

            @Override
            public int compareTo(BestCandidate bestCandidate) {
                return Double.compare(sqDist, bestCandidate.sqDist);
            }

            public void copy(BestCandidate<T> other) {
                this.sqDist = other.sqDist;
                this.item = other.item;
            }
        }

        public static class QTreeNode<T extends Positionable> {

            private byte numberItemsPerNode = 1;

            public QTreeNode() { }

            public static QTreeNode create(double x, double y, double width, byte numberItemsPerNode) {
                QTreeNode qTreeNode = QTreeNodeMemoryPool.fetchMemory();
                qTreeNode.squareBoundary.x = x;
                qTreeNode.squareBoundary.y = y;
                qTreeNode.squareBoundary.width = width;
                qTreeNode.parent = null;
                qTreeNode.items.clear();
                qTreeNode.numberItemsPerNode = numberItemsPerNode;
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
                for (int i = 0; i < items.size(); i++) {
                    items.get(i).setQTreeNode(null);
                }
                QTreeNodeMemoryPool.recycleMemory(this);
            }

            public QTreeNode parent = null;
            public ArrayList<T> items = new ArrayList<T>(numberItemsPerNode);

            public QTreeNode northWest = null;
            public QTreeNode northEast = null;
            public QTreeNode southWest = null;
            public QTreeNode southEast = null;

            public SquareBoundary squareBoundary = new SquareBoundary();

            private boolean isHalfVisited = false;
            private boolean isVisited = false;

            public boolean containsPoint(double x, double y) {
                return this.squareBoundary.containsPoint(x, y);
            }

            public boolean add(T item) {

                // Point doesn't belong in region
                if (!this.containsPoint(item.getPosition().x, item.getPosition().y)) {
                    return false;
                }

                if (items.size() < numberItemsPerNode) {
                    items.add(item);
                    item.setQTreeNode(this);
                    return true;
                }
                else {
                    // Subdivide

                    if (northWest == null) {
                        subdivide();
                    }

                    if (northWest.add(item)) return true;
                    if (northEast.add(item)) return true;
                    if (southWest.add(item)) return true;
                    if (southEast.add(item)) return true;
                }

                return false;
            }

            /**
             * Constructs children nodes and sets their boundaries up
             */
            public void subdivide() {
                if (northWest == null) {
                    // TODO: Think about / Test precision errors...
                    double quarterWidth = squareBoundary.width / 4;
                    northWest = QTreeNode.create(squareBoundary.x - quarterWidth, squareBoundary.y + quarterWidth, squareBoundary.width / 2, numberItemsPerNode);
                    northEast = QTreeNode.create(squareBoundary.x + quarterWidth, squareBoundary.y + quarterWidth, squareBoundary.width / 2, numberItemsPerNode);
                    southWest = QTreeNode.create(squareBoundary.x - quarterWidth, squareBoundary.y - quarterWidth, squareBoundary.width / 2, numberItemsPerNode);
                    southEast = QTreeNode.create(squareBoundary.x + quarterWidth, squareBoundary.y - quarterWidth, squareBoundary.width / 2, numberItemsPerNode);

                    northWest.parent = this;
                    northEast.parent = this;
                    southWest.parent = this;
                    southEast.parent = this;
                }
            }

            public QTreeNode findSmallestQTNodeForPoint(double x, double y) {
                QTreeNode toReturn = this;

                if (!this.squareBoundary.containsPoint(x, y)) {
                    return null;
                }

                if (northWest != null) {

                     //Quadrants are exclusive

                    QTreeNode northWestResult = northWest.findSmallestQTNodeForPoint(x, y);
                    if (northWestResult != null) { toReturn = northWestResult; }

                    QTreeNode nortEastResult = northEast.findSmallestQTNodeForPoint(x, y);
                    if (nortEastResult != null) { toReturn = nortEastResult; }

                    QTreeNode southWestResult = southWest.findSmallestQTNodeForPoint(x, y);
                    if (southWestResult != null) { toReturn = southWestResult; }

                    QTreeNode southEastResult = southEast.findSmallestQTNodeForPoint(x, y);
                    if (southEastResult != null) { toReturn = southEastResult; }

                }

                return toReturn;
            }

            public ArrayList queryRange(double x, double y, double range, ArrayList results) {

                if (!this.squareBoundary.intersectsAABB(x, y, range)) {
                    return results;
                }

                for (int i = 0; i < items.size(); i++) {
                    T item = items.get(i);
                    if (item.getPosition().x >= x - range &&
                            item.getPosition().x <= x + range &&
                            item.getPosition().y >= y - range &&
                            item.getPosition().y <= y + range) {
                        results.add(items.get(i));
                    }
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
                public double x = 0;
                public double y = 0;
                public double width = 1;

                public boolean containsPoint(double inX, double inY) {
                    return inX >= x - width/2 && inX <= x + width/2 &&
                            inY >= y - width/2 && inY <= y + width/2;
                }

                public boolean intersectsAABB(double inX, double inY, double inWidth) {
                    //return (Math.abs(inX - x) * 2 < (width + inWidth)) &&
                    //        (Math.abs(inY - y) * 2 < (width + inWidth));

                    double aLeft = x - width / 2;
                    double aRight = x + width / 2;
                    double aTop = y + width / 2;
                    double aBottom = y - width / 2;

                    double bLeft = inX - inWidth / 2;
                    double bRight = inX + inWidth / 2;
                    double bTop = inY + inWidth / 2;
                    double bBottom = inY - inWidth / 2;

                    if (aLeft > bRight) return false;
                    if (bLeft > aRight) return false;
                    if (bBottom > aTop) return false;
                    if (aBottom > bTop) return false;
                    return true;
                }
            }
        }
    }
}
