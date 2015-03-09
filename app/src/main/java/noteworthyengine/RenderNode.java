package noteworthyengine;

import android.graphics.Color;

import utils.Vector2;

/**
 * Created by eric on 3/7/15.
 */
public class RenderNode extends Node {

    public static final String _NAME = "renderNode";
    public String _name = _NAME;

    public Coords coords;
    public Vector2 gfxOldPosition;
    public DoublePtr z;

    public IntegerPtr isGfxInterpolated = new IntegerPtr() {{ v = 0; }};

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
