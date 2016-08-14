package structure;

import junit.framework.TestCase;

import java.util.ArrayList;
import noteworthyengine.QuadTreeSystem;
import utils.QTree;
import utils.Vector2;

/**
 * Created by eric on 8/9/16.
 */
public class QuadTreeTest extends TestCase {

    public class Item implements QTree.Positionable {
        private Vector2 position;
        private QTree.QTreeNode qtNodeRef;

        public Item(double x, double y) {
            position = new Vector2(x, y);
        }

        public void setPosition(double x, double y) {
            position = new Vector2(x, y);
        }

        @Override
        public Vector2 getPosition() {
            return position;
        }

        @Override
        public Object getData() {
            return null;
        }

        @Override
        public void setQTreeNode(QTree.QTreeNode qTreeNode) {
            this.qtNodeRef = qTreeNode;
        }

        @Override
        public QTree.QTreeNode getQTreeNode() {
            return qtNodeRef;
        }
    }

    public void testCanCreateQuadTreeSystem() {
        QuadTreeSystem quadTreeSystem = new QuadTreeSystem();
        assertTrue(quadTreeSystem.nodes.size() == 0);
        assertTrue(quadTreeSystem.qTree.root == null);
    }

    public void testCanAddNode() {
        QTree qTree = new QTree(10, 200, (byte) 1);
        Item item = new Item(0, 0);
        qTree.add(item);

        assertTrue(qTree.root != null);
        assertTrue(qTree.root == item.qtNodeRef);
        assertNull(item.qtNodeRef.parent);

        assertTrue(qTree.root.northWest == null);
        assertEquals(1, qTree.root.items.size());
        assertTrue(qTree.root.squareBoundary.x == 0);
        assertTrue(qTree.root.squareBoundary.y == 0);
        assertTrue(qTree.root.squareBoundary.width == 200);
        assertTrue(qTree.root.containsPoint(0, 0));
        assertTrue(qTree.root.containsPoint(4, -4));
        assertTrue(qTree.root.containsPoint(100, 100));
        assertTrue(qTree.root.containsPoint(-100, -100));
        assertFalse(qTree.root.containsPoint(101, 101));
    }

    public void testCanSubdivide() {
        QTree qTree = new QTree(10, 200, (byte) 1);
        Item item = new Item(0, 0);
        qTree.add(item);
        item.qtNodeRef.subdivide();

        assertNotNull(item.qtNodeRef.northWest);
        assertNotNull(item.qtNodeRef.northEast);
        assertNotNull(item.qtNodeRef.southWest);
        assertNotNull(item.qtNodeRef.southEast);

        assertEquals(-50.0, item.qtNodeRef.northWest.squareBoundary.x);
        assertEquals(50.0, item.qtNodeRef.northWest.squareBoundary.y);
        assertEquals(50.0, item.qtNodeRef.northEast.squareBoundary.x);
        assertEquals(50.0, item.qtNodeRef.northEast.squareBoundary.y);
        assertEquals(-50.0, item.qtNodeRef.southWest.squareBoundary.x);
        assertEquals(-50.0, item.qtNodeRef.southWest.squareBoundary.y);
        assertEquals(50.0, item.qtNodeRef.southEast.squareBoundary.x);
        assertEquals(-50.0, item.qtNodeRef.southEast.squareBoundary.y);
        assertEquals(item.qtNodeRef.squareBoundary.width / 2, item.qtNodeRef.northWest.squareBoundary.width);

        assertEquals(qTree.root, qTree.root.northEast.parent);
        assertEquals(qTree.root, qTree.root.northWest.parent);
        assertEquals(qTree.root, qTree.root.southEast.parent);
        assertEquals(qTree.root, qTree.root.southWest.parent);
    }

    public void testCanAddMultipleNodes() {
        QTree qTree = new QTree(10, 200, (byte) 1);
        Item item = new Item(-2, -2);
        Item item2 = new Item(2, 2);
        qTree.add(item);
        qTree.add(item2);

        assertTrue(qTree.root != null);
        assertTrue(qTree.root == item.qtNodeRef);

        assertEquals(1, item.qtNodeRef.items.size());
        assertEquals(1, item2.qtNodeRef.items.size());

        assertTrue(qTree.root.squareBoundary.x == 0);
        assertTrue(qTree.root.squareBoundary.y == 0);
        assertTrue(qTree.root.squareBoundary.width == 200);
        assertTrue(qTree.root.containsPoint(1, 1));

        assertTrue(qTree.root.northEast != null);
        assertTrue(qTree.root.northEast == item2.qtNodeRef);
        assertTrue(qTree.root.northEast.squareBoundary.x == 50);
        assertTrue(qTree.root.northEast.squareBoundary.y == 50);
        assertTrue(qTree.root.northEast.squareBoundary.width == 100);
        assertTrue(qTree.root.containsPoint(1, 1));

        Item item3 = new Item(-4, -4);
        qTree.add(item3);

        assertTrue(item3.qtNodeRef.squareBoundary.containsPoint(-4, -4));
        assertFalse(item3.qtNodeRef.squareBoundary.containsPoint(3, 3));
        assertTrue(item3.qtNodeRef.parent == item.qtNodeRef);
        assertTrue(item3.qtNodeRef.squareBoundary.width == item.qtNodeRef.squareBoundary.width / 2);
    }

    public void testCanRecycleNodes() {}

    public void testCanQueryRange() {
        QTree qTree = new QTree(10, 200, (byte) 1);
        Item item = new Item(1, 1);
        Item item2 = new Item(3, 3);

        qTree.add(item);
        qTree.add(item2);

        ArrayList results = qTree.queryRange(0, 0, 4);
        assertEquals(2, results.size());

        results = qTree.queryRange(0, 0, 2);
        assertEquals(1, results.size());
        Item result0 = (Item)results.get(0);
        assertEquals(1.0, result0.getPosition().x, 0.001);
        assertEquals(1.0, result0.getPosition().y, 0.001);

        results = qTree.queryRange(1.1, 1.2, 1);
        assertEquals(1, results.size());
        result0 = (Item)results.get(0);
        assertEquals(1.0, result0.getPosition().x, 0.001);
        assertEquals(1.0, result0.getPosition().y, 0.001);

        qTree.clear();
        qTree.add(item);
        qTree.add(item2);

        results = qTree.queryRange(0, 0, 4);

        assertEquals(2, results.size());

        results = qTree.queryRange(0, 0, 2);
        assertEquals(1, results.size());
        result0 = (Item) results.get(0);
        assertEquals(1.0, result0.getPosition().x, 0.001);
        assertEquals(1.0, result0.getPosition().y, 0.001);

        results = qTree.queryRange(1.1, 1.2, 1);
        assertEquals(1, results.size());
        result0 = (Item) results.get(0);
        assertEquals(1.0, result0.getPosition().x, 0.001);
        assertEquals(1.0, result0.getPosition().y, 0.001);
    }

    public void testfindSmallestQTNodeForPoint() {
        QTree qTree = new QTree(10, 200, (byte) 1);
        Item item = new Item(1, 1);
        Item item2 = new Item(1.1, 1.1);
        Item item3 = new Item(1.05, 1.05);

        qTree.add(item);
        qTree.add(item2);
        qTree.add(item3);

        QTree.QTreeNode node = item.qtNodeRef.findSmallestQTNodeForPoint(1, 1);
        assertEquals(item3.getQTreeNode(), node);
        assertNull(item3.getQTreeNode().northWest);
        assertNull(item3.getQTreeNode().northEast);
        assertNull(item3.getQTreeNode().southWest);
        assertNull(item3.getQTreeNode().southEast);
    }

    public void testCanFindClosestItem() {
        Item item = new Item(2, 2);
        Item item2 = new Item(4, 4);
        Item item3 = new Item(8, 8);
        Item item4 = new Item(16, 16);
        Item item5 = new Item(32, 32);

        QTree<Item> qTree = new QTree<Item>(10, 200, (byte) 1);

        qTree.add(item);
        Item initialTest = qTree.queryClosestTo(item);
        assertNull(initialTest);

        qTree = new QTree<Item>(10, 200, (byte) 1);
        qTree.add(item);
        qTree.add(item5);
        Item testAnyResult = qTree.queryClosestTo(item5);
        assertNotNull(testAnyResult);
        assertEquals(item, testAnyResult);

        qTree = new QTree<Item>(10, 200, (byte) 1);

        qTree.add(item);
        qTree.add(item2);
        qTree.add(item3);
        qTree.add(item4);
        qTree.add(item5);

        Item result = qTree.queryClosestTo(item3);
        assertNotSame(item3, result);
        assertEquals(item2, result);
    }

    /**
     * Needed to test the resetting of any flags related to tree traversals
     */
    public void testCanFindClosestItemMultipleTimes() {
        Item item = new Item(2, 2);
        Item item2 = new Item(4, 4);
        Item item3 = new Item(8, 8);
        Item item4 = new Item(16, 16);
        Item item5 = new Item(32, 32);

        QTree<Item> qTree = new QTree<Item>(10, 200, (byte) 1);
        qTree.add(item);
        qTree.add(item2);
        qTree.add(item3);
        qTree.add(item4);
        qTree.add(item5);

        Item result = qTree.queryClosestTo(item3);
        assertNotSame(item3, result);
        assertEquals(item2, result);

        Item secondResult = qTree.queryClosestTo(item4);
        assertNotNull(secondResult);
        assertNotSame(item4, secondResult);
        assertEquals(item3, secondResult);

        Item thirdResult = qTree.queryClosestTo(item5);
        assertNotNull(thirdResult);
        assertNotSame(item5, thirdResult);
        assertEquals(item4, thirdResult);
    }

    public void testUseMeasure() {
        Item item = new Item(2, 2);
        Item item2 = new Item(4, 4);
        Item item3 = new Item(8, 8);
        final Item item4 = new Item(16, 16);
        Item item5 = new Item(32, 32);

        QTree<Item> qTree = new QTree<Item>(10, 200, (byte) 1);
        qTree.add(item);
        qTree.add(item2);
        qTree.add(item3);
        qTree.add(item4);
        qTree.add(item5);

        assertEquals(item4, qTree.queryClosestTo(item5));

        QTree.DistanceMeasurable biasedMeasure = new QTree.DistanceMeasurable() {
            @Override
            public double distanceMeasure(QTree.Positionable item, QTree.Positionable candidateItem) {
                if (candidateItem == item4) return QTree.INFINITE_DISTANCE;
                return item.getPosition().squaredDistanceTo(candidateItem.getPosition());
            }
        };

        qTree.useMeasure(biasedMeasure);

        assertNotSame(item4, qTree.queryClosestTo(item5));
        assertEquals(item3, qTree.queryClosestTo(item5));
    }
}
