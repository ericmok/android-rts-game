package noteworthyengine;

import noteworthyframework.Coords;
import noteworthyframework.DoublePtr;
import noteworthyframework.Gamer;
import noteworthyframework.IntegerPtr;
import noteworthyframework.Node;
import noteworthyframework.Unit;

/**
 * Created by eric on 3/14/15.
 */
public class TimelineNode extends Node {

    public static final String NAME = "timelineNode";

    public static final int ADD_NODE = 0;

    public Coords coords;
    public DoublePtr frameTime;
    public GamerPtr gamerPtr;
    public IntegerPtr type;

    public TimelineNode(Unit unit) {
        super(NAME, unit);
        Node.instantiatePublicFieldsForUnit(unit, TimelineNode.class, this);
    }

    public void set(double ct, double x, double y, double rx, double ry, Gamer gamer) {
        this.frameTime.v = ct;
        this.coords.pos.set(x, y);
        this.coords.rot.setDirection(rx, ry);
        this.gamerPtr.v = gamer;
        this.type.v = ADD_NODE;
    }
}
