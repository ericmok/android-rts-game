package noteworthyengine.units;

import noteworthyengine.BattleNode;
import noteworthyengine.BattleSystem;
import noteworthyengine.FactoryNode;
import noteworthyengine.FactorySystem;
import noteworthyframework.Gamer;
import noteworthyframework.Unit;
import utils.DoublePtr;
import utils.VoidFunc2;
import utils.VoidFunc4;

/**
 * Created by eric on 10/26/15.
 */
public class ZugNest extends Barracks {

    public static final String NAME = "zugNest";

    public ZugNest() {
        super();
        this.name = NAME;

        factoryNode.spawnFunction = SPAWN_FUNCTION;
    }

    @Override
    public void configure(Gamer gamer) {
        super.configure(gamer);

        factoryNode.buildTime.v = 10;
    }

    public VoidFunc2<FactorySystem, FactoryNode> SPAWN_FUNCTION = new VoidFunc2<FactorySystem, FactoryNode>() {
        @Override
        public void apply(FactorySystem factorySystem, FactoryNode factoryNode) {
            Zug zug = UnitPool.zugs.fetchMemory();
            zug.configure(factoryNode.gamer.v);
            zug.battleNode.coords.pos.copy(battleNode.coords.pos);
            factorySystem.getBaseEngine().addUnit(zug);
        }
    };

    @Override
    public void spawnForEnemy(BattleSystem battleSystem, BattleNode attacker) {
        ZugNest zugNest = UnitPool.zugNests.fetchMemory();
        zugNest.configure(attacker.gamer.v);
        zugNest.battleNode.coords.pos.copy(battleNode.coords.pos);
        battleSystem.getBaseEngine().addUnit(zugNest);
    }

//    public VoidFunc4<BattleSystem, BattleNode, BattleNode, DoublePtr> createOnDieFunction() {
//        return new VoidFunc4<BattleSystem, BattleNode, BattleNode, DoublePtr>() {
//            @Override
//            public void apply(BattleSystem battleSystem, BattleNode that, BattleNode attacker, DoublePtr damage) {
//                battleNode.hp.v -= damage.v;
//
//                if (!battleNode.isAlive()) {
//                    ZugNest zugNest = UnitPool.zugNests.fetchMemory();
//                    zugNest.configure(attacker.gamer.v);
//                    zugNest.battleNode.coords.pos.copy(battleNode.coords.pos);
//                    battleSystem.getBaseEngine().addUnit(zugNest);
//                }
//            }
//        };
//    }
}
