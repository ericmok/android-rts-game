package noteworthyengine;

import noteworthyframework.Coords;
import noteworthyframework.IntegerPtr;
import noteworthyframework.Node;
import noteworthyframework.Unit;
import utils.Vector2;

/**
 * Created by eric on 3/20/15.
 */
public class GridNode extends Node {
    public static final String NAME = "gridNode";

    public Coords coords;
    public IntegerPtr gridX;
    public IntegerPtr gridY;


    public GridNode(Unit unit) {
        super(NAME, unit);
        Node.instantiatePublicFieldsForUnit(unit, GridNode.class, this);
    }
}
