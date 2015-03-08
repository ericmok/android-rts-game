package noteworthyengine;

import utils.Vector2;

/**
 * Created by eric on 3/8/15.
 */
public class FieldNode extends Node {

    public static final String _NAME = "fieldNode";
    public String _name = _NAME;

    public Coords coords;
    public IntegerPtr isFieldControl = new IntegerPtr() {{ v = 0; }};

    public FieldNode(Unit unit) {
        super(_NAME, unit);
        Node.instantiatePublicFieldsForUnit(unit, FieldNode.class, this);
    }

    public FieldNode(String name, Unit unit) {
        super(name, unit);
        Node.instantiatePublicFieldsForUnit(unit, FieldNode.class, this);
    }

}
