package noteworthyengine.units;

import android.graphics.Color;

import noteworthyengine.BattleNode;
import noteworthyengine.GridNode;
import noteworthyengine.RenderNode;
import noteworthyframework.BaseEngine;
import noteworthyframework.Gamer;
import noteworthyframework.Unit;

/**
 * Created by eric on 3/29/15.
 */
public class City extends Unit {

    public static final String NAME = "city";

    public double spawnAccumulator = 0;

    public GridNode gridNode;
    public BattleNode battleNode = new BattleNode(this);
    public RenderNode renderNode = new RenderNode(this);

    public City(Gamer gamer) {
        this.name = NAME;

        gridNode = new GridNode(this, null, battleNode);

        battleNode.gamer.v = gamer;
        battleNode.hp.v = 100;
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
                    Platoon platoon = new Platoon();
                    platoon.battleNode.gamer.v = battleNode.gamer.v;
                    platoon.battleNode.coords.pos.copy(this.battleNode.coords.pos);
                    engine.addUnit(platoon);
                    break;
                case 1:
                    Archer archer = new Archer(battleNode.gamer.v);
                    archer.battleNode.coords.pos.copy(this.battleNode.coords.pos);
                    engine.addUnit(archer);
                    break;
                case 2:
                    Cannon cannon = new Cannon(battleNode.gamer.v);
                    cannon.battleNode.coords.pos.copy(this.battleNode.coords.pos);
                    engine.addUnit(cannon);
                    break;
            }
            spawnAccumulator = 0;
        }
    }
}
