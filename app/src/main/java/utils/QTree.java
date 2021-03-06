package utils;

import java.util.ArrayList;

/**
 * Backing data structure of the system.
 * Based on: https://en.wikipedia.org/wiki/Quadtree
 * <p/>
 * QuadTreeSystem should not be confused with QTree.
 * QuadTreeNodes should not to be confused with QTreeNodes.
 */
public class QTree<T extends QTree.Positionable> {
    public static final double INFINITE_DISTANCE = Double.MAX_VALUE;
    public static final EuclideanDistance SQUARED_EUCLIDEAN_DISTANCE = new EuclideanDistance();

    private static MemoryPool<QTreeNode> QTreeNodeMemoryPool;

    /**
     * Used to avoid stack allocation
     */
    private BestCandidate<T> bestCandidateToReturn = new BestCandidate<T>();

    private ArrayList<T> RESULTS;

    public QTreeNode root = null;
    private double width = 0;

    // This number is needed for testing
    private byte numberItemsPerNode = 3;

    /**
     * Random number to check against during tree traversals to determine if a QTNode
     * was visited before.
     */
    private int traversalId = 314;

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
     * Synchronized function that increments internal traversal id for use
     * in tree traversals
     *
     * @return The internal counter
     */
    public synchronized int makeNewTraversalId() {
        traversalId = traversalId + 1;
        traversalId = traversalId % (Integer.MAX_VALUE - 123);
        return traversalId;
    }

    /**
     * Finds the closest item to the given item.
     *
     * @param item
     * @return
     */
    public T queryClosestTo(T item) {
        return queryClosestTo(item, QTree.SQUARED_EUCLIDEAN_DISTANCE);
    }

    /**
     * Find the closest item to the given item.
     *
     * @param item
     * @param distanceMeasurable A filter that runs on the nodes that are within range to manipulate
     *                           the final result that is considered best. This can be used to
     *                           prioritize certain units over another for example.
     * @return
     */
    public T queryClosestTo(T item, DistanceMeasurable distanceMeasurable) {
        int queryId = makeNewTraversalId();

        // The item might have been positioned right above a point that is even
        // a smaller QTNode
        QTreeNode initial =
                root.findSmallestQTNodeForPoint(item.getPosition().x, item.getPosition().y);

        // TODO: Set all nodes to !isVisited or use a random number for isVisited
        bestCandidateToReturn.item = null;
        bestCandidateToReturn.sqDist = Double.MAX_VALUE;
        return (T) queryClosestToRecursively(item, initial, bestCandidateToReturn, queryId, distanceMeasurable).item;
    }

    private boolean visitNodeTest(QTreeNode<T> node, double intersectX, double intersectY, double intersectWidth, int queryId) {
        if (node != null) {
            if (node.isVisited != queryId) {
                if (node.squareBoundary.intersectsAABB(intersectX, intersectY, intersectWidth)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This recursive algorithm visits every QTNode in post-order fashion in search of
     * the closest item to the item given.
     * The algorithm progressively prunes the search area each time it receives a better
     * candidate for the closest item.
     *
     * @param item             Position to measure against for closest item. This item is excluded as
     *                         a candidate for closest item.
     * @param node             The smallest (deepest) QTNode that overlaps with the item's location.
     *                         This is not necessarily the QTNode of the item itself!
     * @param bestCandidateOut Mutable data to pass up and down the stack.
     * @param queryId          A random number to check if a QTNode has been visited for this
     *                         recursive calling session
     * @return Returns the mutated BestCandidate passed in
     */
    private BestCandidate<T> queryClosestToRecursively(T item, QTreeNode<T> node, BestCandidate<T> bestCandidateOut, int queryId, DistanceMeasurable distanceMeasurable) {

        // Post order traversal
        node.isHalfVisited = queryId;

        if (node.northWest != null) {
            /*
            Note: Ideally the quadrants are sorted by their proximity to the item to
            increase the aggressiveness of the pruning. Example:
            double quadrantX = node.squareBoundary.x - item.getQTreeNode().getPosition().x;
            double quadrantY = node.squareBoundary.y - item.getQTreeNode().getPosition().y;
             */

            // Allow bestCandidate to be overwritten several times.
            // The bestCandidate is only written to if a better candidate is found

            if (visitNodeTest(node.northWest, item.getPosition().x, item.getPosition().y, bestCandidateOut.sqDist, queryId)) {
                queryClosestToRecursively(item, node.northWest, bestCandidateOut, queryId, distanceMeasurable);
            }
            if (visitNodeTest(node.northEast, item.getPosition().x, item.getPosition().y, bestCandidateOut.sqDist, queryId)) {
                queryClosestToRecursively(item, node.northEast, bestCandidateOut, queryId, distanceMeasurable);
            }
            if (visitNodeTest(node.southWest, item.getPosition().x, item.getPosition().y, bestCandidateOut.sqDist, queryId)) {
                queryClosestToRecursively(item, node.southWest, bestCandidateOut, queryId, distanceMeasurable);
            }
            if (visitNodeTest(node.southEast, item.getPosition().x, item.getPosition().y, bestCandidateOut.sqDist, queryId)) {
                queryClosestToRecursively(item, node.southEast, bestCandidateOut, queryId, distanceMeasurable);
            }
        }

        // Leaf node

        int numberItems = node.items.size(); // cache

        // Try to find an item to overwrite BestCandidate data with
        for (int i = 0; i < numberItems; i++) {

            T candidateItem = node.items.get(i);
            if (candidateItem != item) {

                double sqDistToCandidate = distanceMeasurable.distanceMeasure(item, candidateItem);
                if (sqDistToCandidate == INFINITE_DISTANCE) {
                    continue;
                }

                if (bestCandidateOut.item != null) {

                    if (sqDistToCandidate < bestCandidateOut.sqDist) {
                        bestCandidateOut.item = candidateItem;
                        bestCandidateOut.sqDist = sqDistToCandidate;
                    }

                } else {

                    bestCandidateOut.item = candidateItem;
                    bestCandidateOut.sqDist = distanceMeasurable.distanceMeasure(item, candidateItem);

                }
            }
        }

        node.isVisited = queryId;

        if (visitNodeTest(node.parent, item.getPosition().x, item.getPosition().y, bestCandidateOut.sqDist, queryId)
                && node.parent.isHalfVisited != queryId) {
            queryClosestToRecursively(item, node.parent, bestCandidateOut, queryId, distanceMeasurable);
        }

        return bestCandidateOut; // Not needed since we mutate it anyway
    }

    public void clear() {
        if (root != null) {
            root.recycle();
        }
        root = null;
    }

    /**
     * A measure for distance comparison between an item and a candidate item.
     *
     * @param <T>
     */
    public interface DistanceMeasurable<T extends Positionable> {
        /**
         * This can be used to manipulate preferences for certain types of items of type T.
         * If the measure returns INFINITE_DISTANCE, then the candidate is ignored.
         *
         * @param item
         * @param candidateItem
         * @return
         */
        double distanceMeasure(T item, T candidateItem);
    }

    public static class EuclideanDistance implements DistanceMeasurable {
        @Override
        public double distanceMeasure(Positionable item, Positionable candidateItem) {
            return item.getPosition().squaredDistanceTo(candidateItem.getPosition());
        }
    }

    public interface Positionable {
        Vector2 getPosition();

        Object getData();

        void setQTreeNode(QTreeNode qTreeNode);

        QTreeNode getQTreeNode();
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

        public QTreeNode() {
        }

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

        public QTreeNode.SquareBoundary squareBoundary = new QTreeNode.SquareBoundary();

        private int isHalfVisited = -1;
        private int isVisited = -1;

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
            } else {
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
                if (northWestResult != null) {
                    toReturn = northWestResult;
                }

                QTreeNode nortEastResult = northEast.findSmallestQTNodeForPoint(x, y);
                if (nortEastResult != null) {
                    toReturn = nortEastResult;
                }

                QTreeNode southWestResult = southWest.findSmallestQTNodeForPoint(x, y);
                if (southWestResult != null) {
                    toReturn = southWestResult;
                }

                QTreeNode southEastResult = southEast.findSmallestQTNodeForPoint(x, y);
                if (southEastResult != null) {
                    toReturn = southEastResult;
                }

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
                return inX >= x - width / 2 && inX <= x + width / 2 &&
                        inY >= y - width / 2 && inY <= y + width / 2;
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
                return aBottom <= bTop;
            }
        }
    }
}
