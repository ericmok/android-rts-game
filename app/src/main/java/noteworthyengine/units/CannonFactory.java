package noteworthyengine.units;

import noteworthyengine.battle.BattleNode;
import noteworthyengine.battle.BattleSystem;
import noteworthyengine.FactoryNode;
import noteworthyengine.FactorySystem;
import noteworthyengine.players.PlayerUnit;
import utils.VoidFunc2;

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
    public void configure(PlayerUnit playerUnit) {
        super.configure(playerUnit);

        factoryNode.buildTime.v = 60;
    }

    @Override
    public void spawnForEnemy(BattleSystem battleSystem, BattleNode attacker) {
        CannonFactory cannonFactory = UnitPool.cannonFactories.fetchMemory();
        cannonFactory.configure(attacker.playerUnitPtr.v);
        cannonFactory.battleNode.coords.pos.copy(battleNode.coords.pos);
        battleSystem.getBaseEngine().addUnit(cannonFactory);
    }

    public VoidFunc2<FactorySystem, FactoryNode> SPAWN_FUNCTION = new VoidFunc2<FactorySystem, FactoryNode>() {
        @Override
        public void apply(FactorySystem factorySystem, FactoryNode factoryNode) {
            Cannon cannon = UnitPool.cannons.fetchMemory();
            cannon.configure(factoryNode.playerUnitPtr.v);
            cannon.battleNode.coords.pos.copy(battleNode.coords.pos);
            factorySystem.getBaseEngine().addUnit(cannon);
        }
    };
}
