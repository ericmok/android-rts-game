package noteworthyengine;

import noteworthyengine.players.PlayerUnitPtr;
import noteworthyframework.Node;
import noteworthyframework.Unit;
import utils.IntegerPtr;

/**
 * Created by eric on 10/29/15.
 */
public class FactoryCounterNode extends Node {

    //public GamerPtr gamer = new GamerPtr() {{ v = null; }};
    public PlayerUnitPtr playerUnitPtr = new PlayerUnitPtr();
    public IntegerPtr numberFactories = new IntegerPtr() {{ v = 0; }};

    public FactoryCounterNode(Unit unit) {
        super("factoryCounterNode", unit);
    }
}
