package noteworthyengine;

import android.graphics.Color;

/**
 * Created by eric on 3/7/15.
 */
public class RenderNode extends Node {

    public static final String _NAME = "renderNode";
    public String _name = _NAME;

    public Coords coords;
    public DoublePtr width = new DoublePtr() {{ v = 1; }};
    public DoublePtr height = new DoublePtr() {{ v = 1; }};
    public String animationName;
    public DoublePtr animationProgress;
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
