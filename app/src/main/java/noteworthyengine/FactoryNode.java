package noteworthyengine;

import noteworthyengine.units.Platoon;
import noteworthyengine.units.UnitPool;
import noteworthyframework.Coords;
import noteworthyframework.Gamer;
import noteworthyframework.GamerPtr;
import noteworthyframework.Node;
import noteworthyframework.Unit;
import utils.DoublePtr;
import utils.VoidFunc;

/**
 * Created by eric on 4/30/15.
 */
public class FactoryNode extends Node {

    public static final String NAME = "factoryNode";

    public static final VoidFunc<FactorySystem> DEFAULT_SPAWN_FUNCTION = new VoidFunc<FactorySystem>() {
        @Override
        public void apply(FactorySystem element) {
        }
    };

    public Coords coords;
    public GamerPtr gamer;
    public DoublePtr buildTime =  new DoublePtr() {{ v = 20; }};
    public DoublePtr buildProgress = new DoublePtr() {{ v = 0; }};

    public VoidFunc<FactorySystem> spawnFunction = DEFAULT_SPAWN_FUNCTION;

    public FactoryNode(Unit unit) {
        super(NAME, unit);
        Node.instantiatePublicFieldsForUnit(unit, FactoryNode.class, this);
    }

    public void configure(Gamer gamer) {
        this.gamer.v = gamer;
    }
}
