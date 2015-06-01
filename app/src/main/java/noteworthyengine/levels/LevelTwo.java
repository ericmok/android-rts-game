package noteworthyengine.levels;

import org.json.JSONException;

import java.util.ArrayList;

import noteworthyengine.CityWinLoseConditionSystem;
import noteworthyengine.events.GameEvents;
import noteworthyengine.units.Barracks;
import noteworthyengine.units.Cannon;
import noteworthyengine.units.CannonFactory;
import noteworthyengine.units.DefeatUnit;
import noteworthyengine.units.Mech;
import noteworthyengine.units.MechFactory;
import noteworthyengine.units.Mine;
import noteworthyengine.units.Nanobot;
import noteworthyengine.units.NanobotFactory;
import noteworthyengine.units.Platoon;
import noteworthyengine.units.UnitPool;
import noteworthyengine.units.WinUnit;
import noteworthyframework.BaseEngine;
import noteworthyframework.EngineDataLoader;
import noteworthyframework.Gamer;
import structure.Game;
import utils.Orientation;
import utils.Vector2;

/**
 * Created by eric on 4/27/15.
 */
public class LevelTwo implements EngineDataLoader {

    private Game game;

    public LevelTwo(Game game) {
        this.game = game;
    }

    public void spawnBase0(BaseEngine baseEngine, Gamer gamer, Vector2 location) {

        Vector2 rot = new Vector2();
        rot.copy(location);
        rot.x = -rot.x;
        rot.y = -rot.y;
        rot.setNormalized();

        Vector2 perp = new Vector2();
        Orientation.getPerpendicular(perp, rot);

        Barracks barracks = UnitPool.barracks.fetchMemory(); //new City(gamer);
        barracks.configure(gamer);
        barracks.battleNode.coords.pos.copy(location);
        barracks.battleNode.coords.pos.translate(-1, 0);
        baseEngine.addUnit(barracks);

        barracks = UnitPool.barracks.fetchMemory(); //new City(gamer);
        barracks.configure(gamer);
        barracks.battleNode.coords.pos.copy(location);
        barracks.battleNode.coords.pos.translate(1, 0);
        baseEngine.addUnit(barracks);


        for (int h = 0; h <= 2; h++) {
            for (int i = -6; i <= 6; i++) {
                Platoon platoon = UnitPool.platoons.fetchMemory(); //new Platoon();
                platoon.configure(gamer);
                platoon.battleNode.coords.pos.copy(location);
                platoon.battleNode.coords.pos.translate((i) * perp.x + h * rot.x, (i) * perp.y + h * rot.y);
                //platoon.battleNode.gamer.v = gamer;
                baseEngine.addUnit(platoon);
            }
        }

//        for (int i = -9; i < 9; i++) {
//            Cannon cannon = UnitPool.cannons.fetchMemory(); // new Cannon(gamer);;
//            cannon.configure(gamer);
//            cannon.battleNode.coords.pos.copy(location);
//            cannon.battleNode.coords.pos.translate((i) * perp.x + 2 * rot.x, ((i)) * perp.y + 2 * rot.y);
//            baseEngine.addUnit(cannon);
//        }
    }

    public void spawnBase1(BaseEngine baseEngine, Gamer gamer, Vector2 location) {

        Vector2 rot = new Vector2();
        rot.copy(location);
        rot.x = -rot.x;
        rot.y = -rot.y;
        rot.setNormalized();

        Vector2 perp = new Vector2();
        Orientation.getPerpendicular(perp, rot);

        CannonFactory factory = UnitPool.cannonFactories.fetchMemory();
        factory.configure(gamer);
        factory.battleNode.coords.pos.copy(location);
        factory.battleNode.coords.pos.translate(perp.x, perp.y);
        baseEngine.addUnit(factory);

        CannonFactory cannonFactory = UnitPool.cannonFactories.fetchMemory();
        cannonFactory.configure(gamer);
        cannonFactory.battleNode.coords.pos.copy(location);
        cannonFactory.battleNode.coords.pos.translate(-perp.x, -perp.y);
        baseEngine.addUnit(cannonFactory);

//        for (int i = -10; i < 10; i++) {
//            Nanobot nanobot = UnitPool.nanobots.fetchMemory();
//            nanobot.battleNode.hp.v = 15;
//            nanobot.configure(gamer);
//            nanobot.battleNode.coords.pos.copy(location);
//            nanobot.battleNode.coords.pos.translate((i) * perp.x + 2.5 * rot.x, ((i)) * perp.y + 2.5 * rot.y);
//            baseEngine.addUnit(nanobot);
//        }

        for (int i = -10; i < 10; i++) {
            Cannon cannon = UnitPool.cannons.fetchMemory(); // new Cannon(gamer);;
//            Mine cannon = UnitPool.mines.fetchMemory();
//            cannon.battleNode.targetAcquisitionRange.v = 20;
//            cannon.movementNode.maxSpeed.v = 0.78;
            cannon.battleNode.hp.v = 15;
            //cannon.battleNode.attackCooldown.v = 40;
            cannon.configure(gamer);
            cannon.battleNode.coords.pos.copy(location);
            cannon.battleNode.coords.pos.translate((i) * perp.x + 2.5 * rot.x, ((i)) * perp.y + 2.5 * rot.y);
            baseEngine.addUnit(cannon);
        }

    }

    @Override
    public boolean loadFromJson(final BaseEngine baseEngine, String json) throws JSONException {

        baseEngine.addSystem(new CityWinLoseConditionSystem(game));

        baseEngine.gameTime = 0;

        Gamer gamer0 = new Gamer("taco");
        gamer0.team = 0;

        Gamer gamer1 = new Gamer("avilo");
        gamer1.team = 1;

        baseEngine.gamers.add(gamer0);
        baseEngine.gamers.add(gamer1);

        baseEngine.currentGamer = gamer0;

        spawnBase0(baseEngine, gamer0, new Vector2(0, -9));
        //spawnBase1(baseEngine, gamer0, new Vector2(0, -9));
        spawnBase1(baseEngine, gamer1, new Vector2(0, 9));

        baseEngine.addEventListener(new BaseEngine.EventListener() {
            @Override
            public void onEvent(int event) {
                if (event == GameEvents.WIN) {
                    WinUnit winUnit = new WinUnit();
                    baseEngine.addUnit(winUnit);
                } else if (event == GameEvents.LOSE) {
                    DefeatUnit defeatUnit = new DefeatUnit();
                    baseEngine.addUnit(defeatUnit);
                }
            }
        });

        baseEngine.flushQueues();

        return true;
    }
}
