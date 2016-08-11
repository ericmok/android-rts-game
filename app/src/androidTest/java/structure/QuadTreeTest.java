package structure;

import junit.framework.TestCase;

import java.util.ArrayList;

import noteworthyengine.QuadTreeSystem;
import noteworthyframework.Unit;

/**
 * Created by eric on 8/9/16.
 */
public class QuadTreeTest extends TestCase {

    public void testCanCreateQuadTreeSystem() {
        QuadTreeSystem quadTreeSystem = new QuadTreeSystem();
        assertTrue(quadTreeSystem.nodes.size() == 0);
        assertTrue(quadTreeSystem.qTree.root == null);
    }

    public void testCanAddNode() {
        QuadTreeSystem quadTreeSystem = new QuadTreeSystem();
        Unit unit = new Unit();
        QuadTreeSystem.QuadTreeNode quadTreeNode = new QuadTreeSystem.QuadTreeNode(unit);
        quadTreeNode.coords.pos.set(0, 0);

        quadTreeSystem.addNode(quadTreeNode);
        quadTreeSystem.flushQueues();
        quadTreeSystem.step(1, 1);

        assertTrue(quadTreeSystem.nodes.size() == 1);
        assertTrue(quadTreeSystem.qTree.root != null);
        assertTrue(quadTreeSystem.qTree.root == quadTreeNode._qtTreeNode);

        assertNull(quadTreeNode._qtTreeNode.parent);

        assertTrue(quadTreeSystem.qTree.root.northWest == null);
        assertEquals(1, quadTreeSystem.qTree.root.items.size());
        assertTrue(quadTreeSystem.qTree.root.squareBoundary.x == 0);
        assertTrue(quadTreeSystem.qTree.root.squareBoundary.y == 0);
        assertTrue(quadTreeSystem.qTree.root.squareBoundary.width == 200);
        assertTrue(quadTreeSystem.qTree.root.containsPoint(0, 0));
        assertTrue(quadTreeSystem.qTree.root.containsPoint(4, -4));
        assertTrue(quadTreeSystem.qTree.root.containsPoint(100, 100));
        assertTrue(quadTreeSystem.qTree.root.containsPoint(-100, -100));
        assertFalse(quadTreeSystem.qTree.root.containsPoint(101, 101));
    }

    public void testCanSubdivide() {
        QuadTreeSystem quadTreeSystem = new QuadTreeSystem();
        Unit unit = new Unit();
        QuadTreeSystem.QuadTreeNode quadTreeNode = new QuadTreeSystem.QuadTreeNode(unit);
        quadTreeSystem.addNode(quadTreeNode);

        quadTreeSystem.flushQueues();
        quadTreeSystem.step(1, 1);
        quadTreeNode._qtTreeNode.subdivide();

        assertNotNull(quadTreeNode._qtTreeNode.northWest);
        assertNotNull(quadTreeNode._qtTreeNode.northEast);
        assertNotNull(quadTreeNode._qtTreeNode.southWest);
        assertNotNull(quadTreeNode._qtTreeNode.southEast);

        assertEquals(-50.0, quadTreeNode._qtTreeNode.northWest.squareBoundary.x);
        assertEquals(50.0, quadTreeNode._qtTreeNode.northWest.squareBoundary.y);
        assertEquals(50.0, quadTreeNode._qtTreeNode.northEast.squareBoundary.x);
        assertEquals(50.0, quadTreeNode._qtTreeNode.northEast.squareBoundary.y);
        assertEquals(-50.0, quadTreeNode._qtTreeNode.southWest.squareBoundary.x);
        assertEquals(-50.0, quadTreeNode._qtTreeNode.southWest.squareBoundary.y);
        assertEquals(50.0, quadTreeNode._qtTreeNode.southEast.squareBoundary.x);
        assertEquals(-50.0, quadTreeNode._qtTreeNode.southEast.squareBoundary.y);
        assertEquals(quadTreeNode._qtTreeNode.squareBoundary.width / 2, quadTreeNode._qtTreeNode.northWest.squareBoundary.width);

        assertEquals(quadTreeSystem.qTree.root, quadTreeSystem.qTree.root.northEast.parent);
        assertEquals(quadTreeSystem.qTree.root, quadTreeSystem.qTree.root.northWest.parent);
        assertEquals(quadTreeSystem.qTree.root, quadTreeSystem.qTree.root.southEast.parent);
        assertEquals(quadTreeSystem.qTree.root, quadTreeSystem.qTree.root.southWest.parent);
    }

    public void testCanAddMultipleNodes() {
        QuadTreeSystem quadTreeSystem = new QuadTreeSystem();
        Unit unit = new Unit();
        QuadTreeSystem.QuadTreeNode quadTreeNode = new QuadTreeSystem.QuadTreeNode(unit);
        quadTreeNode.coords.pos.set(-2, -2);
        quadTreeSystem.addNode(quadTreeNode);

        Unit unit2 = new Unit();
        QuadTreeSystem.QuadTreeNode quadTreeNode2 = new QuadTreeSystem.QuadTreeNode(unit2);
        quadTreeNode2.coords.pos.set(2, 2);
        quadTreeSystem.addNode(quadTreeNode2);

        quadTreeSystem.flushQueues();
        quadTreeSystem.step(1, 1);

        assertTrue(quadTreeSystem.nodes.size() == 2);
        assertTrue(quadTreeSystem.qTree.root != null);
        assertTrue(quadTreeSystem.qTree.root == quadTreeNode._qtTreeNode);

        assertEquals(1, quadTreeNode._qtTreeNode.items.size());
        assertEquals(1, quadTreeNode2._qtTreeNode.items.size());

        assertTrue(quadTreeSystem.qTree.root.squareBoundary.x == 0);
        assertTrue(quadTreeSystem.qTree.root.squareBoundary.y == 0);
        assertTrue(quadTreeSystem.qTree.root.squareBoundary.width == 200);
        assertTrue(quadTreeSystem.qTree.root.containsPoint(1, 1));

        assertTrue(quadTreeSystem.qTree.root.northEast != null);
        assertTrue(quadTreeSystem.qTree.root.northEast == quadTreeNode2._qtTreeNode);
        assertTrue(quadTreeSystem.qTree.root.northEast.squareBoundary.x == 50);
        assertTrue(quadTreeSystem.qTree.root.northEast.squareBoundary.y == 50);
        assertTrue(quadTreeSystem.qTree.root.northEast.squareBoundary.width == 100);
        assertTrue(quadTreeSystem.qTree.root.containsPoint(1, 1));

        Unit unit3 = new Unit();
        QuadTreeSystem.QuadTreeNode quadTreeNode3 = new QuadTreeSystem.QuadTreeNode(unit3);
        quadTreeNode3.coords.pos.set(-4, -4);
        quadTreeSystem.addNode(quadTreeNode3);

        quadTreeSystem.flushQueues();
        quadTreeSystem.step(1, 1);

        assertEquals(3, quadTreeSystem.nodes.size());
        assertTrue(quadTreeNode3._qtTreeNode.squareBoundary.containsPoint(-4, -4));
        assertFalse(quadTreeNode3._qtTreeNode.squareBoundary.containsPoint(3, 3));
        assertTrue(quadTreeNode3._qtTreeNode.parent == quadTreeNode._qtTreeNode);
        assertTrue(quadTreeNode3._qtTreeNode.squareBoundary.width == quadTreeNode._qtTreeNode.squareBoundary.width / 2);
    }

    public void testCanRecycleNodes() {}

    public void testCanQueryRange() {
        QuadTreeSystem quadTreeSystem = new QuadTreeSystem();
        Unit unit = new Unit();
        QuadTreeSystem.QuadTreeNode quadTreeNode = new QuadTreeSystem.QuadTreeNode(unit);
        quadTreeNode.coords.pos.set(1, 1);
        quadTreeSystem.addNode(quadTreeNode);

        Unit unit2 = new Unit();
        QuadTreeSystem.QuadTreeNode quadTreeNode2 = new QuadTreeSystem.QuadTreeNode(unit2);
        quadTreeNode2.coords.pos.set(3, 3);
        quadTreeSystem.addNode(quadTreeNode2);

        quadTreeSystem.flushQueues();
        quadTreeSystem.step(1, 1);

        ArrayList results = quadTreeSystem.qTree.queryRange(0, 0, 4);
        assertEquals(2, results.size());

        results = quadTreeSystem.qTree.queryRange(0, 0, 2);
        assertEquals(1, results.size());
        QuadTreeSystem.QuadTreeNode result0 = (QuadTreeSystem.QuadTreeNode)results.get(0);
        assertEquals(1.0, result0.coords.pos.x, 0.001);
        assertEquals(1.0, result0.coords.pos.y, 0.001);

        results = quadTreeSystem.qTree.queryRange(1.1, 1.2, 1);
        assertEquals(1, results.size());
        result0 = (QuadTreeSystem.QuadTreeNode)results.get(0);
        assertEquals(1.0, result0.coords.pos.x, 0.001);
        assertEquals(1.0, result0.coords.pos.y, 0.001);

        quadTreeSystem.step(1,1);

        results = quadTreeSystem.qTree.queryRange(0, 0, 4);
        assertEquals(2, results.size());

        results = quadTreeSystem.qTree.queryRange(0, 0, 2);
        assertEquals(1, results.size());
        result0 = (QuadTreeSystem.QuadTreeNode)results.get(0);
        assertEquals(1.0, result0.coords.pos.x, 0.001);
        assertEquals(1.0, result0.coords.pos.y, 0.001);

        results = quadTreeSystem.qTree.queryRange(1.1, 1.2, 1);
        assertEquals(1, results.size());
        result0 = (QuadTreeSystem.QuadTreeNode)results.get(0);
        assertEquals(1.0, result0.coords.pos.x, 0.001);
        assertEquals(1.0, result0.coords.pos.y, 0.001);
    }
}
