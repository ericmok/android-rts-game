package noteworthyengine.units;

import noteworthyengine.BattleNode;
import noteworthyengine.BattleSystem;
import noteworthyengine.FactoryNode;
import noteworthyengine.FactorySystem;
import noteworthyengine.players.PlayerUnit;
import utils.DoublePtr;
import utils.VoidFunc;
import utils.VoidFunc2;
import utils.VoidFunc4;

/**
 * Created by eric on 4/30/15.
 */
public class MechFactory extends Barracks {

    public static final String NAME = "cannonFactory";

    public MechFactory() {
        super();
        this.name = NAME;

        factoryNode.spawnFunction = SPAWN_FUNCTION;
    }

    @Override
    public void configure(PlayerUnit playerUnit) {
        super.configure(playerUnit);

        factoryNode.buildTime.v = 35;
    }

    @Override
    public void spawnForEnemy(BattleSystem battleSystem, BattleNode attacker) {
        MechFactory mechFactory = UnitPool.mechFactories.fetchMemory();
        mechFactory.configure(attacker.playerUnitPtr.v);
        mechFactory.battleNode.coords.pos.copy(battleNode.coords.pos);
        battleSystem.getBaseEngine().addUnit(mechFactory);
    }

    public VoidFunc2<FactorySystem, FactoryNode> SPAWN_FUNCTION = new VoidFunc2<FactorySystem, FactoryNode>() {
        @Override
        public void apply(FactorySystem factorySystem, FactoryNode factoryNode) {
            Mech mech = UnitPool.mechs.fetchMemory();
            mech.configure(factoryNode.playerUnitPtr.v);
            mech.battleNode.coords.pos.copy(battleNode.coords.pos);
            factorySystem.getBaseEngine().addUnit(mech);
        }
    };
}
