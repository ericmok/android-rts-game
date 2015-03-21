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
        double bucketX = grid.getBucketX(gridUnit.gridNode.coords.pos.x);
        double bucketY = grid.getBucketY(gridUnit.gridNode.coords.pos.y);
        assertTrue(bucketX == 49);
        assertTrue(bucketY == 49);
        assertTrue(grid.getBucketForCell(49, 49).size() == 1);

        grid.clear();
        gridUnit.gridNode.coords.pos.x = 3;
        gridUnit.gridNode.coords.pos.y = 3;
        grid.index(gridUnit.gridNode);
        assertTrue(grid.getBucketForCell(50, 50).size() == 1);

        grid.clear();
        gridUnit.gridNode.coords.pos.x = 4;
        gridUnit.gridNode.coords.pos.y = 4;
        grid.index(gridUnit.gridNode);
        assertTrue(grid.getBucketForCell(51, 51).size() == 1);

        grid.clear();
        gridUnit.gridNode.coords.pos.x = -5;
        gridUnit.gridNode.coords.pos.y = -5;
        grid.index(gridUnit.gridNode);
        assertTrue(grid.getBucketForCell(48, 48).size() == 1);
    }
}
