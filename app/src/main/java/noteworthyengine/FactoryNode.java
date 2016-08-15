package noteworthyengine;

import noteworthyengine.players.PlayerUnit;
import noteworthyengine.players.PlayerUnitPtr;
import noteworthyframework.Coords;
import noteworthyframework.Node;
import noteworthyframework.Unit;
import utils.DoublePtr;
import utils.VoidFunc2;

//import noteworthyframework.Gamer;
//import noteworthyframework.GamerPtr;

/**
 * Created by eric on 4/30/15.
 */
public class FactoryNode extends Node {

    public static final String NAME = "factoryNode";

    public static final VoidFunc2<FactorySystem, FactoryNode> DEFAULT_SPAWN_FUNCTION = new VoidFunc2<FactorySystem, FactoryNode>() {
        @Override
        public void apply(FactorySystem element, FactoryNode factoryNode) {
        }
    };

    public Coords coords;
    //public GamerPtr gamer;
    public PlayerUnitPtr playerUnitPtr;
    public DoublePtr buildTime =  new DoublePtr() {{ v = 20; }};
    public DoublePtr buildProgress = new DoublePtr() {{ v = 0; }};

    public VoidFunc2<FactorySystem, FactoryNode> spawnFunction = DEFAULT_SPAWN_FUNCTION;

    public FactoryNode(Unit unit) {
        super(FactoryNode.class, unit);
        Node.instantiatePublicFieldsForUnit(unit, FactoryNode.class, this);
    }

    public void configure(PlayerUnit playerUnit) {
        this.playerUnitPtr.v = playerUnit;
    }
}
