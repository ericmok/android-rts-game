package structure.grid;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.util.List;

import noteworthyengine.Grid;
import noteworthyengine.GridNode;
import noteworthyframework.Unit;

/**
 * Created by eric on 3/20/15.
 */
public class GridTest extends ApplicationTestCase<Application> {
    public GridTest() {
        super(Application.class);
    }

    public void testGrid() {
        assertEquals(1, (int)Math.round(0.5));
        assertEquals(0, (int)Math.round(0.4));
        assertEquals(0, (int)Math.round(-0.4));
        assertEquals(-1, (int)Math.round(-0.51));
        assertEquals(-1, (int)Math.round(-1.1));

        Grid grid = new Grid(100, 100, 4);

        GridUnit gridUnit = new GridUnit();
        gridUnit.gridNode.coords.pos.zero();

        grid.index(gridUnit.gridNode);

        List<GridNode> nodes = grid.getBucketForNode(gridUnit.gridNode);
        assertTrue(nodes.size() == 1);

        assertTrue(grid.getBucketForCell(50, 50).size() == 1);
        assertTrue(grid.getBucketForCell(51, 51).size() == 0);

        grid.clear();
        gridUnit.gridNode.coords.pos.x = 1;
        gridUnit.gridNode.coords.pos.y = 1;
        grid.index(gridUnit.gridNode);
        assertTrue(grid.getBucketForCell(50, 50).size() == 1);

        grid.clear();
        gridUnit.gridNode.coords.pos.x = -1;
        gridUnit.gridNode.coords.pos.y = -1;
        grid.index(gridUnit.gridNode);
        int bucketX = grid.getBucketX(gridUnit.gridNode.coords.pos.x);
        int bucketY = grid.getBucketY(gridUnit.gridNode.coords.pos.y);
        assertEquals(50, bucketX);
        assertEquals(50, bucketY);
        assertEquals(1, grid.getBucketForCell(50, 50).size());

        grid.clear();
        gridUnit.gridNode.coords.pos.x = 3;
        gridUnit.gridNode.coords.pos.y = 3;
        grid.index(gridUnit.gridNode);
        bucketX = grid.getBucketX(gridUnit.gridNode.coords.pos.x);
        bucketY = grid.getBucketY(gridUnit.gridNode.coords.pos.y);
        assertEquals(51, bucketX);
        assertEquals(51, bucketY);
        assertEquals(0, grid.getBucketForCell(49, 49).size());
        assertEquals(0, grid.getBucketForCell(49, 50).size());
        assertEquals(0, grid.getBucketForCell(49, 51).size());
        assertEquals(0, grid.getBucketForCell(50, 49).size());
        assertEquals(0, grid.getBucketForCell(50, 50).size());
        assertEquals(0, grid.getBucketForCell(50, 51).size());
        assertEquals(0, grid.getBucketForCell(51, 49).size());
        assertEquals(0, grid.getBucketForCell(51, 50).size());
        assertEquals(1, grid.getBucketForCell(51, 51).size());

        grid.clear();
        gridUnit.gridNode.coords.pos.x = 4;
        gridUnit.gridNode.coords.pos.y = 4;
        grid.index(gridUnit.gridNode);
        assertEquals(1, grid.getBucketForCell(51, 51).size());
        assertTrue(grid.getSurroundingNodes(50, 50, 0).size() == 0);
        assertTrue(grid.getSurroundingNodes(50, 52, 0).size() == 0);
        assertTrue(grid.getSurroundingNodes(52, 50, 0).size() == 0);
        assertTrue(grid.getSurroundingNodes(52, 52, 0).size() == 0);
        assertTrue(grid.getSurroundingNodes(50, 50, 1).size() == 1);
        assertTrue(grid.getSurroundingNodes(50, 52, 1).size() == 1);
        assertTrue(grid.getSurroundingNodes(52, 50, 1).size() == 1);
        assertTrue(grid.getSurroundingNodes(52, 52, 1).size() == 1);
        assertTrue(grid.getSurroundingNodes(49, 49, 1).size() == 0);
        assertTrue(grid.getSurroundingNodes(49, 53, 1).size() == 0);
        assertTrue(grid.getSurroundingNodes(53, 49, 1).size() == 0);
        assertTrue(grid.getSurroundingNodes(53, 53, 1).size() == 0);
        assertTrue(grid.getSurroundingNodes(49, 49, 4).size() == 0);
        assertTrue(grid.getSurroundingNodes(49, 53, 4).size() == 0);
        assertTrue(grid.getSurroundingNodes(53, 49, 4).size() == 0);
        assertTrue(grid.getSurroundingNodes(53, 53, 4).size() == 0);
        assertTrue(grid.getSurroundingNodes(49, 49, 8).size() == 1);
        assertTrue(grid.getSurroundingNodes(49, 53, 8).size() == 1);
        assertTrue(grid.getSurroundingNodes(53, 49, 8).size() == 1);
        assertTrue(grid.getSurroundingNodes(53, 53, 8).size() == 1);

        grid.clear();
        // Cell (-1, -1)
        gridUnit.gridNode.coords.pos.x = -5;
        gridUnit.gridNode.coords.pos.y = -5;
        grid.index(gridUnit.gridNode);
        assertTrue(grid.getBucketForCell(49, 49).size() == 1);

        // Cell (-2, -2)
        gridUnit.gridNode.coords.pos.x = -7;
        gridUnit.gridNode.coords.pos.y = -7;
        grid.index(gridUnit.gridNode);
        assertTrue(grid.getBucketForCell(48, 48).size() == 1);

        // Cell (2, 2)
        gridUnit.gridNode.coords.pos.x = 7;
        gridUnit.gridNode.coords.pos.y = 7;
        grid.index(gridUnit.gridNode);
        assertTrue(grid.getBucketForCell(52, 52).size() == 1);

        // Cell (0, 0)
        gridUnit.gridNode.coords.pos.x = -1.5;
        gridUnit.gridNode.coords.pos.y = -1.5;
        grid.index(gridUnit.gridNode);
        assertTrue(grid.getBucketForCell(50, 50).size() == 1);

        /*
          * 0 0 0 0 1
          * 0 0 0 0 0
          * 0 0 1 0 0
          * 0 1 0 0 0
          * 1 0 0 0 0
         */

        List<GridNode> shell = grid.getShell(50, 50, 0);
        assertEquals(1, shell.size());

        shell = grid.getShell(50, 50, 1);
        assertEquals(1, shell.size());

        shell = grid.getShell(50, 50, 2);
        assertEquals(2, shell.size());

        shell = grid.getShell(51, 51, 0);
        assertEquals(0, shell.size());

        shell = grid.getShell(51, 51, 1);
        assertEquals(2, shell.size());
    }
}
