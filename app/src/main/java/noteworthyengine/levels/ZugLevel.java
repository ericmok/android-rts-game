package noteworthyengine.levels;

import org.json.JSONException;

import noteworthyengine.CityWinLoseConditionSystem;
import noteworthyengine.events.GameEvents;
import noteworthyengine.units.Barracks;
import noteworthyengine.units.DefeatUnit;
import noteworthyengine.units.Platoon;
import noteworthyengine.units.UnitPool;
import noteworthyengine.units.WinUnit;
import noteworthyengine.units.Zug;
import noteworthyengine.units.ZugNest;
import noteworthyframework.BaseEngine;
import noteworthyframework.EngineDataLoader;
import noteworthyframework.Gamer;
import structure.Game;
import utils.Orientation;
import utils.Vector2;

/**
 * Created by eric on 10/26/15.
 */
public class ZugLevel implements EngineDataLoader {

    private Game game;

    public ZugLevel(Game game) {
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
                baseEngine.addUnit(platoon);
            }
        }
    }

    public void spawnBase1(BaseEngine baseEngine, Gamer gamer, Vector2 location) {

        Vector2 rot = new Vector2();
        rot.copy(location);
        rot.x = -rot.x;
        rot.y = -rot.y;
        rot.setNormalized();

        Vector2 perp = new Vector2();
        Orientation.getPerpendicular(perp, rot);

        ZugNest zugNest = UnitPool.zugNests.fetchMemory();
        zugNest.configure(gamer);
        zugNest.battleNode.coords.pos.copy(location);
        zugNest.battleNode.coords.pos.translate(perp.x, perp.y);
        baseEngine.addUnit(zugNest);

        zugNest = UnitPool.zugNests.fetchMemory();
        zugNest.configure(gamer);
        zugNest.battleNode.coords.pos.copy(location);
        zugNest.battleNode.coords.pos.translate(-perp.x, -perp.y);
        baseEngine.addUnit(zugNest);

        for (int i = -14; i < 14; i++) {
            Zug zug = new Zug(); // new Cannon(gamer);;
            zug.configure(gamer);
            zug.battleNode.coords.pos.copy(location);
            zug.battleNode.coords.pos.translate((i) * perp.x + 2.5 * rot.x, ((i)) * perp.y + 2.5 * rot.y);
            baseEngine.addUnit(zug);

            zug = new Zug(); // new Cannon(gamer);;
            zug.configure(gamer);
            zug.battleNode.coords.pos.copy(location);
            zug.battleNode.coords.pos.translate(i * perp.x + 4 * rot.x, i * perp.y + 4 * rot.y);
            baseEngine.addUnit(zug);
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
