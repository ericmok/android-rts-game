package noteworthyengine.levels;

import org.json.JSONException;

import java.util.ArrayList;

import noteworthyengine.CityWinLoseConditionSystem;
import noteworthyengine.NoteworthyEngine;
import noteworthyengine.events.GameEvents;
import noteworthyengine.players.PlayerUnit;
import noteworthyengine.units.Barracks;
import noteworthyengine.units.Cannon;
import noteworthyengine.units.CannonFactory;
import noteworthyengine.units.DefeatUnit;
import noteworthyengine.units.FactoryCounterGUI;
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
import structure.Game;
import utils.Orientation;
import utils.Vector2;

/**
 * Created by eric on 4/27/15.
 */
public class LevelTwo {

    private Game game;

    public LevelTwo(Game game) {
        this.game = game;
    }

    public void spawnBase0(BaseEngine baseEngine, PlayerUnit playerUnit, Vector2 location) {

        Vector2 rot = new Vector2();
        rot.copy(location);
        rot.x = -rot.x;
        rot.y = -rot.y;
        rot.setNormalized();

        Vector2 perp = new Vector2();
        Orientation.getPerpendicular(perp, rot);

        Barracks barracks = UnitPool.barracks.fetchMemory(); //new City(gamer);
        barracks.configure(playerUnit);
        barracks.battleNode.coords.pos.copy(location);
        barracks.battleNode.coords.pos.translate(-1, 0);
        baseEngine.addUnit(barracks);

        barracks = UnitPool.barracks.fetchMemory(); //new City(gamer);
        barracks.configure(playerUnit);
        barracks.battleNode.coords.pos.copy(location);
        barracks.battleNode.coords.pos.translate(1, 0);
        baseEngine.addUnit(barracks);


        for (int h = 0; h <= 2; h++) {
            for (int i = -6; i <= 6; i++) {
                Platoon platoon = UnitPool.platoons.fetchMemory(); //new Platoon();
                platoon.configure(playerUnit);
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

    public void spawnBase1(BaseEngine baseEngine, PlayerUnit gamer, Vector2 location) {

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

    public boolean loadFromJson(final NoteworthyEngine noteworthyEngine, String json) throws JSONException {

        noteworthyEngine.addSystem(new CityWinLoseConditionSystem(game, noteworthyEngine.playerSystem));

        noteworthyEngine.gameTime = 0;

        PlayerUnit gamer0 = new PlayerUnit("taco", 0);
        PlayerUnit gamer1 = new PlayerUnit("avilo", 1);

        noteworthyEngine.addUnit(gamer0);
        noteworthyEngine.addUnit(gamer1);

        noteworthyEngine.playerSystem.setCurrentPlayer(gamer0);

        spawnBase0(noteworthyEngine, gamer0, new Vector2(0, -9));
        //spawnBase1(baseEngine, gamer0, new Vector2(0, -9));
        spawnBase1(noteworthyEngine, gamer1, new Vector2(0, 9));

        FactoryCounterGUI factoryCounterGUI = new FactoryCounterGUI();
        factoryCounterGUI.configure(gamer0);
        noteworthyEngine.addUnit(factoryCounterGUI);

        noteworthyEngine.addEventListener(new BaseEngine.EventListener() {
            @Override
            public void onEvent(int event) {
                if (event == GameEvents.WIN) {
                    WinUnit winUnit = new WinUnit();
                    noteworthyEngine.addUnit(winUnit);
                } else if (event == GameEvents.LOSE) {
                    DefeatUnit defeatUnit = new DefeatUnit();
                    noteworthyEngine.addUnit(defeatUnit);
                }
            }
        });

        noteworthyEngine.flushQueues();

        return true;
    }
}
