package noteworthyengine.units;

import noteworthyengine.BattleNode;
import noteworthyengine.BattleSystem;
import noteworthyengine.FactoryNode;
import noteworthyengine.FactorySystem;
import noteworthyengine.players.PlayerUnit;
import utils.DoublePtr;
import utils.VoidFunc2;
import utils.VoidFunc4;

/**
 * Created by eric on 5/1/15.
 */
public class NanobotFactory extends Barracks {

    public NanobotFactory() {
        super();

        factoryNode.spawnFunction = new VoidFunc2<FactorySystem, FactoryNode>() {
            @Override
            public void apply(FactorySystem system, FactoryNode node) {
                Nanobot nanobot = UnitPool.nanobots.fetchMemory();
                nanobot.configure(node.playerUnitPtr.v);
                nanobot.battleNode.coords.pos.copy(node.coords.pos);
                nanobot.battleNode.coords.pos.translate(0.8, 0);
                system.getBaseEngine().addUnit(nanobot);

                nanobot = UnitPool.nanobots.fetchMemory();
                nanobot.configure(node.playerUnitPtr.v);
                nanobot.battleNode.coords.pos.copy(node.coords.pos);
                nanobot.battleNode.coords.pos.translate(-0.8, 0);
                system.getBaseEngine().addUnit(nanobot);

                nanobot = UnitPool.nanobots.fetchMemory();
                nanobot.configure(node.playerUnitPtr.v);
                nanobot.battleNode.coords.pos.copy(node.coords.pos);
                system.getBaseEngine().addUnit(nanobot);
            }
        };
    }

    @Override
    public void configure(PlayerUnit playerUnit) {
        super.configure(playerUnit);

        factoryNode.buildTime.v = 5;
        factoryNode.buildProgress.v = 0;
    }

    @Override
    public void spawnForEnemy(BattleSystem battleSystem, BattleNode attacker) {
        NanobotFactory nanobotFactory = UnitPool.nanobotFactories.fetchMemory();
        nanobotFactory.configure(attacker.playerUnitPtr.v);
        nanobotFactory.battleNode.coords.pos.copy(battleNode.coords.pos);
        battleSystem.getBaseEngine().addUnit(nanobotFactory);
    }
}
