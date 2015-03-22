package noteworthyengine;

import java.util.ArrayList;
import java.util.List;

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
        return Math.max(Math.min((int) Math.floor(x / cellSize) + (width / 2), width), 0);
    }

    public int getBucketY(double y) {
        return Math.max(Math.min((int) Math.floor(y / cellSize) + (height / 2), height), 0);
    }

    public void index(GridNode node) {
        int cellX = getBucketX(node.coords.pos.x);
        int cellY = getBucketY(node.coords.pos.y);
        node.gridX.v = cellX;
        node.gridY.v = cellY;
        points[cellX][cellY].add(node);
    }

    public List<GridNode> getBucketForCell(int inX, int inY) {
        return points[inX][inY];
    }

    public List<GridNode> getBucketForNode(GridNode gridNode) {
        return points[getBucketX(gridNode.coords.pos.x)][getBucketY(gridNode.coords.pos.y)];
    }

    public List<GridNode> getSurroundingNodes(GridNode gridNode, double range) {
        ret.clear();
        int lx = this.getBucketX(gridNode.coords.pos.x - range);
        int ly = this.getBucketY(gridNode.coords.pos.y - range);
        int lxMax = this.getBucketX(gridNode.coords.pos.x + range);
        int lyMax = this.getBucketY(gridNode.coords.pos.y + range);

        for (int i = lx; i <= lxMax; i++) {
            for (int j = ly; j <= lyMax; j++) {
                ret.addAll(points[i][j]);
            }
        }
//        for (int i = Math.max(lx, 0); i < Math.min(lx + 1, width); i++) {
//            for (int j = Math.max(ly, 0); j < Math.min(ly + 1, height); j++) {
//                ret.addAll(points[i][j]);
//            }
//        }

        return ret;
    }

    public void clear() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                points[i][j].clear();
            }
        }
    }
}
