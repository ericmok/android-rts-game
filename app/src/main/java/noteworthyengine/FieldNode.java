package noteworthyengine;

import noteworthyframework.Coords;
import utils.DoublePtr;
import noteworthyframework.GamerPtr;
import noteworthyframework.Node;
import noteworthyframework.Unit;
import utils.Vector2;

/**
 * Created by eric on 3/8/15.
 */
public class FieldNode extends Node {

    public static final String _NAME = "fieldNode";
    public String _name = _NAME;

    public GamerPtr gamer;

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

    public static FieldNode createAgentFieldNode(Unit unit) {
        FieldNode fieldNode = new FieldNode(unit);
        fieldNode._fieldAgentNode = new FieldAgentNode(unit);
        return fieldNode;
    }

    public static FieldNode createArrowFieldNode(Unit unit) {
        FieldNode fieldNode = new FieldNode(unit);
        fieldNode._fieldArrowNode = new FieldArrowNode(unit);
        return fieldNode;
    }


    public static class FieldArrowNode extends Node {
        public static final String _NAME = "fieldArrowNode";
        public String _name = _NAME;

        public Coords coords;
        public DoublePtr fieldArrowInfluenceRadius = new DoublePtr() {{
            v = 3.5;
        }};
        public DoublePtr rampDistance = new DoublePtr() {{ v = 10; }};

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

        public DoublePtr maxSpeed;

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
