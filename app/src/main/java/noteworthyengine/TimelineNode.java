package noteworthyengine;

import noteworthyengine.players.PlayerUnit;
import noteworthyengine.players.PlayerUnitPtr;
import noteworthyframework.Coords;
import noteworthyframework.Node;
import noteworthyframework.Unit;
import utils.DoublePtr;
import utils.IntegerPtr;

/**
 * Created by eric on 3/14/15.
 */
public class TimelineNode extends Node {

    public static final String NAME = "timelineNode";

    public static final int ADD_NODE = 0;

    public Coords coords;
    public DoublePtr frameTime;
    public PlayerUnitPtr playerUnitPtr;
    public IntegerPtr type;

    public TimelineNode(Unit unit) {
        super(TimelineNode.class, unit);
        Node.instantiatePublicFieldsForUnit(unit, TimelineNode.class, this);
    }

    public void set(double ct, double x, double y, double rx, double ry, PlayerUnit playerUnit) {
        this.frameTime.v = ct;
        this.coords.pos.set(x, y);
        this.coords.rot.setDirection(rx, ry);
        this.playerUnitPtr.v = playerUnit;
        this.type.v = ADD_NODE;
    }
}
