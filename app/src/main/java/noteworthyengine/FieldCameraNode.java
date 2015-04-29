package noteworthyengine;

import noteworthyframework.Coords;
import noteworthyframework.Node;
import noteworthyframework.Unit;
import structure.GameCamera;
import utils.DoublePtr;

/**
 * Created by eric on 4/29/15.
 */
public class FieldCameraNode extends Node {

    public static final String NAME = "fieldCameraNode";

    public Coords coords;
    public DoublePtr springConstant = new DoublePtr() {{ v = 1; }};

    public FieldCameraNode(Unit unit) {
        super(NAME, unit);
        Node.instantiatePublicFieldsForUnit(unit, FieldCameraNode.class, this);
    }
}
