package noteworthyengine;

import noteworthyframework.Coords;
import noteworthyframework.Node;
import noteworthyframework.Unit;
import utils.DoublePtr;
import utils.FloatPtr;

/**
 * Created by eric on 4/29/15.
 */
public class FieldCameraNode extends Node {

    public static final String NAME = "fieldCameraNode";

    public Coords coords;
    public FloatPtr scale = new FloatPtr() {{ v = 1; }};
    public FloatPtr zoomScale = new FloatPtr() {{ v = 1; }};
    public DoublePtr springConstant = new DoublePtr() {{ v = 1; }};

    public float _originalCameraScale = 1;

    public FieldCameraNode(Unit unit, float scale, float zoomScale) {
        super(FieldCameraNode.class, unit);
        Node.instantiatePublicFieldsForUnit(unit, FieldCameraNode.class, this);

        this._originalCameraScale = scale;
        this.scale.v = scale;
        this.zoomScale.v = zoomScale;
    }
}
