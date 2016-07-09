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
public class LevelOne {

    private Game game;

    public LevelOne(Game game) {
        this.game = game;
    }

    public void spawnBase(BaseEngine baseEngine, PlayerUnit gamer, Vector2 location) {

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
        barracks.battleNode.coords.pos.translate(1, 1);
        baseEngine.addUnit(barracks);


        for (int h = 0; h <= 1; h++) {
            for (int i = -3; i <= 3; i++) {
                Platoon platoon = UnitPool.platoons.fetchMemory(); //new Platoon();
                platoon.configure(gamer);
                platoon.battleNode.coords.pos.copy(location);
                platoon.battleNode.coords.pos.translate((1.4 * i) * perp.x + h * rot.x, (1.4 * i) * perp.y + h * rot.y);
                //platoon.battleNode.gamer.v = gamer;
                baseEngine.addUnit(platoon);
            }
        }

    }

    public boolean loadFromJson(final NoteworthyEngine baseEngine, String json) throws JSONException {

        baseEngine.addSystem(new CityWinLoseConditionSystem(game, baseEngine.playerSystem));

        baseEngine.gameTime = 0;

        PlayerUnit gamer0 = new PlayerUnit("taco", 0);
        PlayerUnit gamer1 = new PlayerUnit("avilo", 1);
        PlayerUnit gamer2 = new PlayerUnit("grubby", 2);
        PlayerUnit gamer3 = new PlayerUnit("connie", 3);

        baseEngine.addUnit(gamer0);
        baseEngine.addUnit(gamer1);
        baseEngine.addUnit(gamer2);
        baseEngine.addUnit(gamer3);

        Vector2 spawnLocation0 = new Vector2(0, -12 + Math.random() * 2 - 1);
        Vector2 spawnLocation1 = new Vector2(0, 12 + Math.random() * 2 - 1);
        Vector2 spawnLocation2 = new Vector2(-12 + Math.random() * 2 - 1, 0);
        Vector2 spawnLocation3 = new Vector2(12 + Math.random() * 2 - 1, 0);

        ArrayList<Vector2> spawnLocations = new ArrayList<Vector2>(4);
        spawnLocations.add(spawnLocation0);
        spawnLocations.add(spawnLocation1);
        spawnLocations.add(spawnLocation2);
        spawnLocations.add(spawnLocation3);

//        for (int i = 0; i < baseEngine.playerSystem.players.size(); i++) {
//            spawnBase(baseEngine, (PlayerUnit)baseEngine.playerSystem.players.get(i).unit, spawnLocations.get(i));
//        }

        spawnBase(baseEngine, gamer0, spawnLocations.get(0));
        spawnBase(baseEngine, gamer1, spawnLocations.get(1));
        spawnBase(baseEngine, gamer2, spawnLocations.get(2));
        spawnBase(baseEngine, gamer3, spawnLocations.get(3));


        PlayerUnit gamer4 = new PlayerUnit("neutral", 4);
        baseEngine.addUnit(gamer4);

        ArrayList<Vector2> neutralSpawnLocations = new ArrayList<Vector2>(8);

        baseEngine.playerSystem.setCurrentPlayer(gamer0);

        FactoryCounterGUI factoryCounterGUI = new FactoryCounterGUI();
        factoryCounterGUI.configure(gamer0);
        baseEngine.addUnit(factoryCounterGUI);

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
