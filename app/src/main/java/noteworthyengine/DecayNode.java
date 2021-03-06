package noteworthyengine;

import noteworthyframework.Node;
import noteworthyframework.Unit;
import utils.DoublePtr;

/**
 * Created by eric on 3/19/15.
 */
public class DecayNode extends Node {

    public static final String NAME = "decayNode";

    /// Time to live in seconds
    public DoublePtr timeToLive = new DoublePtr() {{ v = 1; }};

    public DecayNode(Unit unit) {
        super(DecayNode.class, unit);
        Node.instantiatePublicFieldsForUnit(unit, DecayNode.class, this);
    }
}
