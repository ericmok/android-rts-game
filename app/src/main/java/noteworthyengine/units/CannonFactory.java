package noteworthyengine.units;

import noteworthyengine.BattleNode;
import noteworthyengine.FactoryNode;
import noteworthyengine.FactorySystem;
import noteworthyframework.Gamer;
import noteworthyframework.Unit;
import utils.VoidFunc;

/**
 * Created by eric on 4/30/15.
 */
public class CannonFactory extends Barracks {

    public static final String NAME = "cannonFactory";

    public CannonFactory(final Gamer gamer) {
        super(gamer);
        this.name = NAME;

        factoryNode.spawnFunction = SPAWN_FUNCTION;
    }

    @Override
    public void configure(Gamer gamer) {
        super.configure(gamer);

        factoryNode.buildTime.v = 45;
    }

    public VoidFunc<FactorySystem> SPAWN_FUNCTION = new VoidFunc<FactorySystem>() {
        @Override
        public void apply(FactorySystem factorySystem) {
            Cannon cannon = UnitPool.cannons.fetchMemory();
            cannon.configure(battleNode.gamer.v);
            cannon.battleNode.coords.pos.copy(battleNode.coords.pos);
            factorySystem.getBaseEngine().addUnit(cannon);
        }
    };
}
