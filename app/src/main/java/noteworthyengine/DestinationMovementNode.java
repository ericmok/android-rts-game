package noteworthyengine;

import noteworthyframework.Coords;
import noteworthyframework.Node;
import noteworthyframework.Unit;
import utils.DoublePtr;
import utils.Vector2;
import utils.VoidFunc;

/**
 * Created by eric on 10/8/15.
 */
public class DestinationMovementNode extends Node {
    public static final String _NAME = "destinationMovementNode";
    public String _name = _NAME;

    public Coords coords;
    public Vector2 gfxOldPosition;

    public Vector2 velocity;
    public Vector2 acceleration;

    public Vector2 destination;

    public DoublePtr maxSpeed = new DoublePtr() {{ v = 1; }};
    public DoublePtr maxAcceleartion = new DoublePtr() {{ v = 0.01; }};

    public VoidFunc<DestinationMovementSystem> onDestinationReached = null;

    public DestinationMovementNode(Unit unit) {
        super(DestinationMovementNode.class, unit);
        Node.instantiatePublicFieldsForUnit(unit, DestinationMovementNode.class, this);
    }
}
