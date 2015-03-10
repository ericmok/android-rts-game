package noteworthyengine;

import noteworthyframework.Coords;
import noteworthyframework.Node;
import noteworthyframework.Unit;
import utils.Vector2;

/**
 * Created by eric on 3/8/15.
 */
public class FieldNode extends Node {

    public static final String _NAME = "fieldNode";
    public String _name = _NAME;

    public FieldArrowNode _fieldArrowNode;
    public FieldAgentNode _fieldAgentNode;

    public FieldNode(Unit unit) {
        super(_NAME, unit);
        Node.instantiatePublicFieldsForUnit(unit, FieldNode.class, this);
    }

    public FieldNode(String name, Unit unit) {
        super(name, unit);
        Node.instantiatePublicFieldsForUnit(unit, FieldNode.class, this);
    }


    public static class FieldArrowNode extends Node {
        public static final String _NAME = "fieldArrowNode";
        public String _name = _NAME;

        public Coords coords;

        public FieldArrowNode(Unit unit) {
            super(_NAME, unit);
            Node.instantiatePublicFieldsForUnit(unit, FieldArrowNode.class, this);
        }

        public FieldArrowNode(String name, Unit unit) {
            super(name, unit);
            Node.instantiatePublicFieldsForUnit(unit, FieldArrowNode.class, this);
        }
    }


    public static class FieldAgentNode extends Node {
        public static final String _NAME = "fieldAgentNode";
        public String _name = _NAME;

        public Coords coords;
        public Vector2 fieldForce;

        public FieldAgentNode(Unit unit) {
            super(_NAME, unit);
            Node.instantiatePublicFieldsForUnit(unit, FieldAgentNode.class, this);
        }

        public FieldAgentNode(String name, Unit unit) {
            super(name, unit);
            Node.instantiatePublicFieldsForUnit(unit, FieldAgentNode.class, this);
        }
    }
}
