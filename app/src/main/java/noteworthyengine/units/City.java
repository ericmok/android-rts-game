package noteworthyengine.units;

import art.Constants;
import noteworthyengine.battle.BattleNode;
import noteworthyengine.battle.BattleSystem;
import noteworthyengine.GridNode;
import noteworthyengine.RenderNode;
import noteworthyengine.battle.ChangeOwnershipOnDeathBattleEffect;
import noteworthyengine.players.PlayerUnit;
import noteworthyframework.BaseEngine;
import noteworthyframework.Unit;

/**
 * Created by eric on 3/29/15.
 */
public class City extends Unit implements ChangeOwnershipOnDeathBattleEffect.SpawnForEnemyable {

    public static final String NAME = "city";

    public double spawnAccumulator = 0;

    public GridNode gridNode;
    public BattleNode battleNode = new BattleNode(this) {{
        this.battleEffects.add(new ChangeOwnershipOnDeathBattleEffect(this, City.this));
    }};

    public RenderNode renderNode = new RenderNode(this);

    public City() {
        this.name = NAME;

        gridNode = new GridNode(this, null, battleNode);
    }

    public void configure(PlayerUnit playerUnit) {
        battleNode.reset();
        battleNode.playerUnitPtr.v = playerUnit;
        battleNode.hp.v = 110;
        battleNode.attackDamage.v = 1;
        renderNode.set(0, 0, 0, 1.5f, 1.5f, 90f, Constants.colorForTeam(playerUnit.playerNode.playerData.team), "Animations/Buildings/City", 0, 0);
    }

    @Override
    public void spawnForEnemy(BattleSystem battleSystem, BattleNode attacker) {
        City city = UnitPool.cities.fetchMemory();
        city.configure(attacker.playerUnitPtr.v);
        city.battleNode.coords.pos.copy(battleNode.coords.pos);
        battleSystem.getBaseEngine().addUnit(city);
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
                    platoon.configure(battleNode.playerUnitPtr.v);
                    platoon.battleNode.coords.pos.copy(this.battleNode.coords.pos);
                    engine.addUnit(platoon);
                    break;
                case 1:
                    //Mech mech = new Mech(battleNode.gamer.v);
                    Mech mech = UnitPool.mechs.fetchMemory();
                    mech.configure(battleNode.playerUnitPtr.v);
                    mech.battleNode.coords.pos.copy(this.battleNode.coords.pos);
                    engine.addUnit(mech);
                    break;
                case 2:
                    //Cannon cannon = new Cannon(battleNode.gamer.v);
                    Cannon cannon = UnitPool.cannons.fetchMemory();
                    cannon.configure(battleNode.playerUnitPtr.v);
                    cannon.battleNode.coords.pos.copy(this.battleNode.coords.pos);
                    engine.addUnit(cannon);
                    break;
            }
            spawnAccumulator = 0;
        }
    }
}
