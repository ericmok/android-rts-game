package noteworthyengine.units;

import noteworthyengine.BattleNode;
import noteworthyengine.BattleSystem;
import noteworthyengine.CityWinLoseConditionNode;
import noteworthyengine.FactoryNode;
import noteworthyengine.FactorySystem;
import noteworthyengine.GridNode;
import noteworthyengine.RenderNode;
import noteworthyframework.BaseEngine;
import noteworthyframework.Gamer;
import noteworthyframework.Unit;
import utils.DoublePtr;
import utils.VoidFunc;
import utils.VoidFunc2;
import utils.VoidFunc4;

/**
 * Created by eric on 4/30/15.
 */
public class Barracks extends Unit {

    public static final String NAME = "barracks";

    public GridNode gridNode;

    public FactoryNode factoryNode = new FactoryNode(this);

    public BattleNode battleNode = new BattleNode(this) {
        public void onDie(BattleSystem battleSystem) {
            Barracks.this.spawnForEnemy(battleSystem, this.lastAttacker.v);
        }
    };

    public RenderNode renderNode = new RenderNode(this);

    public CityWinLoseConditionNode cityWinLoseConditionNode = new CityWinLoseConditionNode(this);

    public Barracks() {
        this.name = NAME;

        gridNode = new GridNode(this, null, battleNode);

        factoryNode.spawnFunction = new VoidFunc2<FactorySystem, FactoryNode>() {
            @Override
            public void apply(FactorySystem factorySystem, FactoryNode factoryNode) {
                Platoon platoon = UnitPool.platoons.fetchMemory();
                platoon.configure(factoryNode.gamer.v);
                platoon.battleNode.coords.pos.copy(battleNode.coords.pos);
                factorySystem.getBaseEngine().addUnit(platoon);
            }
        };
    }

    public void configure(Gamer gamer) {
        battleNode.reset();
        battleNode.hp.v = 100;
        battleNode.attackDamage.v = 1;
        battleNode.gamer.v = gamer;
        renderNode.set(0, 0, 0, 1.5f, 1.5f, 90f, Gamer.colorForTeam(gamer.team), "Animations/Buildings/City", 0, 0);

        factoryNode.configure(gamer);
        factoryNode.buildTime.v = 25;
        factoryNode.buildProgress.v = 0;
    }

    public void spawnForEnemy(BattleSystem battleSystem, BattleNode attacker) {
        Barracks barracks = UnitPool.barracks.fetchMemory();
        barracks.configure(attacker.gamer.v);
        barracks.battleNode.coords.pos.copy(battleNode.coords.pos);
        battleSystem.getBaseEngine().addUnit(barracks);
    }
}
