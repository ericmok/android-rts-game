package noteworthyengine;

import noteworthyframework.Coords;
import noteworthyframework.IntegerPtr;
import noteworthyframework.Node;
import noteworthyframework.Unit;
import utils.Vector2;

/**
 * Created by eric on 3/16/15.
 */
public class SeparationNode extends Node {

    public static final String NAME = "separationName";

    public Coords coords;
    public Vector2 separationForce;

    public IntegerPtr gridX;
    public IntegerPtr gridY;

    public SeparationNode(Unit unit) {
        super(NAME, unit);
        Node.instantiatePublicFieldsForUnit(unit, SeparationNode.class, this);
    }
}
