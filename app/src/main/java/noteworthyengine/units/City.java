package noteworthyengine.units;

import noteworthyengine.BattleNode;
import noteworthyengine.BattleSystem;
import noteworthyengine.GridNode;
import noteworthyengine.RenderNode;
import noteworthyframework.BaseEngine;
import noteworthyframework.Gamer;
import noteworthyframework.Unit;
import utils.DoublePtr;
import utils.VoidFunc4;

/**
 * Created by eric on 3/29/15.
 */
public class City extends Unit {

    public static final String NAME = "city";

    public double spawnAccumulator = 0;

    public GridNode gridNode;
    public BattleNode battleNode = new BattleNode(this) {
        @Override
        public void onDie(BattleSystem battleSystem) {
            super.onDie(battleSystem);
            City city = UnitPool.cities.fetchMemory();
            city.configure(this.lastAttacker.v.gamer.v);
            city.battleNode.coords.pos.copy(battleNode.coords.pos);
            battleSystem.getBaseEngine().addUnit(city);
        }
    };

    public RenderNode renderNode = new RenderNode(this);

    public City() {
        this.name = NAME;

        gridNode = new GridNode(this, null, battleNode);
    }

    public void configure(Gamer gamer) {
        battleNode.reset();
        battleNode.gamer.v = gamer;
        battleNode.hp.v = 110;
        battleNode.attackDamage.v = 1;
        renderNode.set(0, 0, 0, 1.5f, 1.5f, 90f, Gamer.colorForTeam(gamer.team), "Animations/Buildings/City", 0, 0);
    }

    @Override
    public void step(BaseEngine engine, double dt) {
        super.step(engine, dt);

        spawnAccumulator += dt;
        if (spawnAccumulator >= 20) {
            int d = (int)Math.floor(Math.random() * 3);

            switch (d) {
                case 0:
                    //Platoon platoon = new Platoon();
                    Platoon platoon = UnitPool.platoons.fetchMemory();
                    platoon.configure(battleNode.gamer.v);
                    platoon.battleNode.coords.pos.copy(this.battleNode.coords.pos);
                    engine.addUnit(platoon);
                    break;
                case 1:
                    //Mech mech = new Mech(battleNode.gamer.v);
                    Mech mech = UnitPool.mechs.fetchMemory();
                    mech.configure(battleNode.gamer.v);
                    mech.battleNode.coords.pos.copy(this.battleNode.coords.pos);
                    engine.addUnit(mech);
                    break;
                case 2:
                    //Cannon cannon = new Cannon(battleNode.gamer.v);
                    Cannon cannon = UnitPool.cannons.fetchMemory();
                    cannon.configure(battleNode.gamer.v);
                    cannon.battleNode.coords.pos.copy(this.battleNode.coords.pos);
                    engine.addUnit(cannon);
                    break;
            }
            spawnAccumulator = 0;
        }
    }
}
