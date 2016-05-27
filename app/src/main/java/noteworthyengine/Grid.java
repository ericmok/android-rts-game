package noteworthyengine;

import java.util.ArrayList;
import java.util.List;

import utils.BooleanFunc;
import utils.BooleanFunc2;
import utils.Vector2;

/**
 * Created by eric on 3/20/15.
 * Grid for spacial indexing
 */
public class Grid {
    private int width = 100;
    private int height = 100;
    private double cellSize = 4;

    public static final int EXPECT_NUMBER_POINTS_PER_CELL = 30;

    private List<GridNode>[][] points;

    private ArrayList<GridNode> ret = new ArrayList<GridNode>(EXPECT_NUMBER_POINTS_PER_CELL);

    private final Vector2 centerOfMass = new Vector2();
    private final Vector2 resultVector = new Vector2();
    private int numberIndexed = 0;

    public Grid(int width, int height, double cellSize) {
        this.width = width;
        this.height = height;
        this.cellSize = cellSize;

        points = new ArrayList[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                points[i][j] = new ArrayList<GridNode>(EXPECT_NUMBER_POINTS_PER_CELL);
            }
        }
    }

    public int getBucketX(double x) {
        int cell = (int) Math.round(x / cellSize);
        int bias = (width / 2);
        return Math.max(Math.min(cell + bias, width - 1), 0);
    }

    public int getBucketY(double y) {
        return Math.max(Math.min((int) Math.round(y / cellSize) + (height / 2), height - 1), 0);
    }

    public void index(GridNode node) {
        centerOfMass.translate(node.coords.pos.x, node.coords.pos.y);
        numberIndexed += 1;

        int cellX = getBucketX(node.coords.pos.x);
        int cellY = getBucketY(node.coords.pos.y);
        node.gridX.v = cellX;
        node.gridY.v = cellY;
        points[cellX][cellY].add(node);
    }

    /**
     * Get a vector representing the center of mass of all the units indexed.
     * @return A vector owned by the grid
     */
    public Vector2 calculateCenterOfMass() {
        resultVector.copy(centerOfMass);
        resultVector.x = resultVector.x / numberIndexed;
        resultVector.y = resultVector.y / numberIndexed;
        return resultVector;
    }

    public List<GridNode> getBucketForCell(int inX, int inY) {
        return points[inX][inY];
    }

    public List<GridNode> getBucketForNode(GridNode gridNode) {
        return points[getBucketX(gridNode.coords.pos.x)][getBucketY(gridNode.coords.pos.y)];
    }

    public List<GridNode> getSurroundingNodes(int gridX, int gridY, double range) {
        ret.clear();

        int discreteRange = (int)Math.ceil(range / cellSize);

        int lx = Math.max(gridX - discreteRange, 0);
        int ly = Math.max(gridY - discreteRange, 0);
        int lxMax = Math.min(gridX + discreteRange, width - 1);
        int lyMax = Math.min(gridY + discreteRange, height - 1);

        for (int i = lx; i <= lxMax; i++) {
            for (int j = ly; j <= lyMax; j++) {

                List<GridNode> nodesToRed = points[i][j];
                for (int k = nodesToRed.size() - 1; k >= 0; k--) {
                    ret.add(points[i][j].get(k));
                }
            }
        }

        return ret;
    }

    public List<GridNode> getSurroundingNodes(GridNode gridNode, double range) {
        ret.clear();
        int lx = this.getBucketX(gridNode.coords.pos.x - range);
        int ly = this.getBucketY(gridNode.coords.pos.y - range);
        int lxMax = this.getBucketX(gridNode.coords.pos.x + range);
        int lyMax = this.getBucketY(gridNode.coords.pos.y + range);

        for (int i = lx; i <= lxMax; i++) {
            for (int j = ly; j <= lyMax; j++) {

                List<GridNode> nodesToRed = points[i][j];
                for (int k = nodesToRed.size() - 1; k >= 0; k--) {
                    ret.add(points[i][j].get(k));
                }
            }
        }

        return ret;
    }

    public List<GridNode> getShell(GridNode gridNode, int cellRange) {
        return getShell(gridNode.gridX.v, gridNode.gridY.v, cellRange);
    }

    /**
     * Returns a list of units of the outer cells of the rectangular spatial query.
     * An iterative search of increasing cellRanges from 0 to cellRange allows
     * for a performant strategy.
     *
     * <pre>
     * 0 0 0 0 0
     * 0 0 0 1 0
     * 0 0 2 0 0
     * 0 1 0 0 0
     * 1 0 0 0 0
     * </pre>
     *
     * shell(x: 0, y: 0, cellRange: 0) => 2
     * shell(x: 0, y: 0, cellRange: 1) => 2
     * shell(x: 0, y: 0, cellRange: 2) => 1
     *
     * @param gridX
     * @param gridY
     * @param cellRange
     * @return
     */
    public List<GridNode> getShell(int gridX, int gridY, int cellRange) {
        ret.clear();

        // Untested bounds check
        gridX = Math.max(Math.min(gridX, width - 1), 0);
        gridY = Math.max(Math.min(gridY, height - 1), 0);

        // Boundary condition, at 0 range, there is no shell
        if (cellRange == 0) {
            List<GridNode> nodesToRet = points[gridX][gridY];
            for (int k = nodesToRet.size() - 1; k >= 0; k--) {
                ret.add(nodesToRet.get(k));
            }

            return ret;
        }

        int lowX = gridX - cellRange;
        int lowY = gridY - cellRange;
        int highX = gridX + cellRange;
        int highY = gridY + cellRange;

        for (int i = lowX; i <= highX; i++) {
            for (int j = lowY; j <= highY; j++) {

                // Bounds of shell
                if (i < 0 || i > width - 1 || j < 0 || j > height - 1) {
                    continue;
                }

                if (i > lowX && i < highX && j > lowY && j < highY) {
                    continue;
                }
                else {
                    List<GridNode> nodesToRet = points[i][j];
                    for (int k = nodesToRet.size() - 1; k >= 0; k--) {
                        ret.add(points[i][j].get(k));
                    }
                }

            }
        }

        return ret;
    }


    // TODO: test
    public List<GridNode> iterativeShellSearch(int gridX, int gridY, int cellRange, BooleanFunc<GridNode> filter) {
        ret.clear();

        int queryRange = 0;

        while(queryRange <= cellRange) {

            List<GridNode> query = getShell(gridX, gridY, queryRange);
            for (int i = query.size() - 1; i >= 0; i--) {

                GridNode nodeToTest = query.get(i);

                if (filter.apply(nodeToTest)) {
                    ret.add(nodeToTest);
                }
            }

            queryRange += 1;
        }

        return ret;
    }


    public int numberCellsForRange(double range) {
        return (int)Math.ceil(range / cellSize);
    }


    /**
     * Returns the closest shell that is occupied by any gridNode
     * Kind of useless..
     * @param gridX
     * @param gridY
     * @return
     */
    public List<GridNode> findClosestShell(int gridX, int gridY) {
        ret.clear();

        int queryX = gridX;
        int queryY = gridY;

        // Get a shell around the query point, increase range
        int range = 0;
        List<GridNode> test = getShell(queryX, queryY, range);

        if (test.size() != 0) {
            return test;
        }

        range += 1;

        // Sloppy bounds check...
        while (range <= Math.max(width - 1, height - 1) && test.size() == 0) {

            test = getShell(queryX, queryY, range);

            if (test.size() != 0) {
                return test;
            }

            range += 1;
        }

        return ret;
    }

    public void clear() {

        centerOfMass.zero();
        numberIndexed = 0;

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                points[i][j].clear();
            }
        }
    }
}
