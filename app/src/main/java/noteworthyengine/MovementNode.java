package noteworthyengine;

import noteworthyframework.Coords;
import utils.DoublePtr;
import noteworthyframework.Node;
import noteworthyframework.Unit;
import utils.JsonSerializable;
import utils.Vector2;

/**
 * Created by eric on 3/6/15.
 */
public class MovementNode extends Node implements JsonSerializable {

    public static final String _NAME = "movementNode";
    public String _name = _NAME;

    public boolean _enabled = true;

    public Coords coords;
    public Vector2 gfxOldPosition;

    public Vector2 velocity;
    public Vector2 acceleration;

    public Vector2 fieldForce;
    public Vector2 formationForce;
    public Vector2 separationForce;
    public Vector2 enemyAttractionForce;

    public BattleNode.Ptr target;

    public DoublePtr turnFactor = new DoublePtr() {{ v = 0.4; }};

    public DoublePtr maxSpeed = new DoublePtr() {{ v = 1; }};

    //public double crowdSpeed = 1;

    public MovementNode(Unit unit) {
        super(_NAME, unit);
        Node.instantiatePublicFieldsForUnit(unit, MovementNode.class, this);
    }

    public MovementNode(String name, Unit unit) {
        super(name, unit);
        Node.instantiatePublicFieldsForUnit(unit, MovementNode.class, this);
    }

    public String json() {

        return "";
    }
}
