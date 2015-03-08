package noteworthyengine;

import android.graphics.Color;

/**
 * Created by eric on 3/7/15.
 */
public class RenderNode extends Node {

    public static final String _NAME = "renderNode";
    public String _name = _NAME;

    public Coords coords;
    public DoublePtr z;

    public IntegerPtr color = new IntegerPtr() {{ v = Color.WHITE; }};

    public FloatPtr width = new FloatPtr() {{ v = 1; }};
    public FloatPtr height = new FloatPtr() {{ v = 1; }};

    public String animationName;
    public IntegerPtr animationProgress;
    public IntegerPtr team;

    public RenderNode(Unit unit) {
        super(_NAME, unit);
        Node.instantiatePublicFieldsForUnit(unit, RenderNode.class, this);
    }

    public RenderNode(String name, Unit unit) {
        super(name, unit);
        Node.instantiatePublicFieldsForUnit(unit, RenderNode.class, this);
    }
}
