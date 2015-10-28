package noteworthyengine.units;

import noteworthyengine.BattleNode;
import noteworthyengine.BattleSystem;
import noteworthyengine.FactoryNode;
import noteworthyengine.FactorySystem;
import noteworthyframework.Gamer;
import noteworthyframework.Unit;
import utils.DoublePtr;
import utils.VoidFunc;
import utils.VoidFunc2;
import utils.VoidFunc4;

/**
 * Created by eric on 4/30/15.
 */
public class CannonFactory extends Barracks {

    public static final String NAME = "cannonFactory";

    public CannonFactory() {
        super();
        this.name = NAME;

        factoryNode.spawnFunction = SPAWN_FUNCTION;
    }

    @Override
    public void configure(Gamer gamer) {
        super.configure(gamer);

        factoryNode.buildTime.v = 60;
    }

    @Override
    public void spawnForEnemy(BattleSystem battleSystem, BattleNode attacker) {
        CannonFactory cannonFactory = UnitPool.cannonFactories.fetchMemory();
        cannonFactory.configure(attacker.gamer.v);
        cannonFactory.battleNode.coords.pos.copy(battleNode.coords.pos);
        battleSystem.getBaseEngine().addUnit(cannonFactory);
    }

    public VoidFunc2<FactorySystem, FactoryNode> SPAWN_FUNCTION = new VoidFunc2<FactorySystem, FactoryNode>() {
        @Override
        public void apply(FactorySystem factorySystem, FactoryNode factoryNode) {
            Cannon cannon = UnitPool.cannons.fetchMemory();
            cannon.configure(factoryNode.gamer.v);
            cannon.battleNode.coords.pos.copy(battleNode.coords.pos);
            factorySystem.getBaseEngine().addUnit(cannon);
        }
    };
}
