package noteworthyengine;

import noteworthyframework.Coords;
import noteworthyframework.Node;
import noteworthyframework.Unit;
import structure.GameCamera;
import utils.DoublePtr;
import utils.IntegerPtr;

/**
 * Created by eric on 3/25/15.
 */
public class CameraNode extends Node {
    public static final String NAME = "cameraNode";

    public Coords coords;
    public IntegerPtr index = new IntegerPtr() {{ v = 0; }};
    public GameCamera camera;

    public CameraNode(Unit unit) {
        super(NAME, unit);
        Node.instantiatePublicFieldsForUnit(unit, CameraNode.class, this);
    }
}
