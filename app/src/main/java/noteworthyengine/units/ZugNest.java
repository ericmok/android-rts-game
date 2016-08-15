package noteworthyengine.units;

import noteworthyengine.battle.BattleNode;
import noteworthyengine.battle.BattleSystem;
import noteworthyengine.FactoryNode;
import noteworthyengine.FactorySystem;
import noteworthyengine.players.PlayerUnit;
import utils.VoidFunc2;

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
    public void configure(PlayerUnit playerUnit) {
        super.configure(playerUnit);

        factoryNode.buildTime.v = 10;
    }

    public VoidFunc2<FactorySystem, FactoryNode> SPAWN_FUNCTION = new VoidFunc2<FactorySystem, FactoryNode>() {
        @Override
        public void apply(FactorySystem factorySystem, FactoryNode factoryNode) {
            Zug zug = UnitPool.zugs.fetchMemory();
            zug.configure(factoryNode.playerUnitPtr.v);
            zug.battleNode.coords.pos.copy(battleNode.coords.pos);
            zug.battleNode.coords.pos.translate(-0.1, 0);
            factorySystem.getBaseEngine().addUnit(zug);

            zug = UnitPool.zugs.fetchMemory();
            zug.configure(factoryNode.playerUnitPtr.v);
            zug.battleNode.coords.pos.copy(battleNode.coords.pos);
            zug.battleNode.coords.pos.translate(0.1, 0);
            factorySystem.getBaseEngine().addUnit(zug);
        }
    };

    @Override
    public void spawnForEnemy(BattleSystem battleSystem, BattleNode attacker) {
        ZugNest zugNest = UnitPool.zugNests.fetchMemory();
        zugNest.configure(attacker.playerUnitPtr.v);
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
