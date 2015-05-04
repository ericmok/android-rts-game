package noteworthyengine.units;

import noteworthyengine.BattleNode;
import noteworthyengine.BattleSystem;
import noteworthyengine.FactoryNode;
import noteworthyengine.FactorySystem;
import noteworthyframework.Gamer;
import utils.DoublePtr;
import utils.VoidFunc2;
import utils.VoidFunc4;

/**
 * Created by eric on 5/1/15.
 */
public class NanobotFactory extends Barracks {

    public NanobotFactory(Gamer gamer) {
        super(gamer);

        factoryNode.spawnFunction = new VoidFunc2<FactorySystem, FactoryNode>() {
            @Override
            public void apply(FactorySystem system, FactoryNode node) {
                Nanobot nanobot = UnitPool.nanobots.fetchMemory();
                nanobot.configure(node.gamer.v);
                nanobot.battleNode.coords.pos.copy(node.coords.pos);
                nanobot.battleNode.coords.pos.translate(0.8, 0);
                system.getBaseEngine().addUnit(nanobot);

                nanobot = UnitPool.nanobots.fetchMemory();
                nanobot.configure(node.gamer.v);
                nanobot.battleNode.coords.pos.copy(node.coords.pos);
                nanobot.battleNode.coords.pos.translate(-0.8, 0);
                system.getBaseEngine().addUnit(nanobot);

                nanobot = UnitPool.nanobots.fetchMemory();
                nanobot.configure(node.gamer.v);
                nanobot.battleNode.coords.pos.copy(node.coords.pos);
                system.getBaseEngine().addUnit(nanobot);
            }
        };

        factoryNode.buildTime.v = 5;
        factoryNode.buildProgress.v = 0;
    }

    @Override
    public VoidFunc4<BattleSystem, BattleNode, BattleNode, DoublePtr> createOnDieFunction() {
        return new VoidFunc4<BattleSystem, BattleNode, BattleNode, DoublePtr>() {
            @Override
            public void apply(BattleSystem battleSystem, BattleNode battleNode, BattleNode attacker, DoublePtr damage) {
                battleNode.hp.v -= damage.v;

                if (battleNode.hp.v <= 0) {
                    NanobotFactory nanobotFactory = UnitPool.nanobotFactories.fetchMemory();
                    nanobotFactory.configure(attacker.gamer.v);
                    nanobotFactory.battleNode.coords.pos.copy(battleNode.coords.pos);
                    battleSystem.getBaseEngine().addUnit(nanobotFactory);
                }
            }
        };
    }
}