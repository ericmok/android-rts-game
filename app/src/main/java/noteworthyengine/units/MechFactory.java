package noteworthyengine.units;

import noteworthyengine.FactorySystem;
import noteworthyframework.Gamer;
import utils.VoidFunc;

/**
 * Created by eric on 4/30/15.
 */
public class MechFactory extends Barracks {

    public static final String NAME = "cannonFactory";

    public MechFactory(final Gamer gamer) {
        super(gamer);
        this.name = NAME;

        factoryNode.spawnFunction = SPAWN_FUNCTION;
    }

    @Override
    public void configure(Gamer gamer) {
        super.configure(gamer);

        factoryNode.buildTime.v = 30;
    }

    public VoidFunc<FactorySystem> SPAWN_FUNCTION = new VoidFunc<FactorySystem>() {
        @Override
        public void apply(FactorySystem factorySystem) {
            Mech mech = UnitPool.mechs.fetchMemory();
            mech.configure(battleNode.gamer.v);
            mech.battleNode.coords.pos.copy(battleNode.coords.pos);
            factorySystem.getBaseEngine().addUnit(mech);
        }
    };
}
