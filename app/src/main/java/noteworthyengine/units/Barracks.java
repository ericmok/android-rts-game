package noteworthyengine.units;

import noteworthyengine.BattleNode;
import noteworthyengine.BattleSystem;
import noteworthyengine.FactoryNode;
import noteworthyengine.FactorySystem;
import noteworthyengine.GridNode;
import noteworthyengine.RenderNode;
import noteworthyframework.BaseEngine;
import noteworthyframework.Gamer;
import noteworthyframework.Unit;
import utils.DoublePtr;
import utils.VoidFunc;
import utils.VoidFunc4;

/**
 * Created by eric on 4/30/15.
 */
public class Barracks extends Unit {

    public static final String NAME = "barracks";

    public GridNode gridNode;

    public FactoryNode factoryNode = new FactoryNode(this);

    public BattleNode battleNode = new BattleNode(this);
    public RenderNode renderNode = new RenderNode(this);

    public Barracks(final Gamer gamer) {
        this.name = NAME;

        this.reset();
        this.configure(gamer);

        gridNode = new GridNode(this, null, battleNode);

        battleNode.inflictDamage = new VoidFunc4<BattleSystem, BattleNode, BattleNode, DoublePtr>() {
            @Override
            public void apply(BattleSystem battleSystem, BattleNode that, BattleNode attacker, DoublePtr damage) {
                battleNode.hp.v -= damage.v;

                if (battleNode.hp.v <= 0) {
                    City city = UnitPool.cities.fetchMemory();
                    city.configure(attacker.gamer.v);
                    city.battleNode.coords.pos.copy(battleNode.coords.pos);
                    battleSystem.getBaseEngine().addUnit(city);
                }
            }
        };

        factoryNode.spawnFunction = new VoidFunc<FactorySystem>() {
            @Override
            public void apply(FactorySystem factorySystem) {
                Platoon platoon = UnitPool.platoons.fetchMemory();
                platoon.configure(gamer);
                platoon.battleNode.coords.pos.copy(battleNode.coords.pos);
                factorySystem.getBaseEngine().addUnit(platoon);
            }
        };
    }

    public void reset() {
        battleNode.hp.v = 100;
        battleNode.attackDamage.v = 1;
    }

    public void configure(Gamer gamer) {
        battleNode.gamer.v = gamer;
        renderNode.set(0, 0, 0, 1.5f, 1.5f, 90f, Gamer.colorForTeam(gamer.team), "Animations/Buildings/City", 0, 0);

        factoryNode.configure(gamer);
        factoryNode.buildTime.v = 20;
        factoryNode.buildProgress.v = 0;
    }
}
