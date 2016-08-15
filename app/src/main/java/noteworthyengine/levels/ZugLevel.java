package noteworthyengine.levels;

import org.json.JSONException;

import noteworthyengine.CityWinLoseConditionSystem;
import noteworthyengine.NoteworthyEngine;
import noteworthyengine.events.GameEvents;
import noteworthyengine.players.PlayerUnit;
import noteworthyengine.units.Barracks;
import noteworthyengine.units.DefeatUnit;
import noteworthyengine.units.FactoryCounterGUI;
import noteworthyengine.units.Platoon;
import noteworthyengine.units.PufferZug;
import noteworthyengine.units.UnitPool;
import noteworthyengine.units.WinUnit;
import noteworthyengine.units.ZugNest;
import noteworthyframework.BaseEngine;
import structure.Game;
import utils.Orientation;
import utils.Vector2;

/**
 * Created by eric on 10/26/15.
 */
public class ZugLevel {

    private Game game;

    public ZugLevel(Game game) {
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
                baseEngine.addUnit(platoon);
            }
        }
    }

    public void spawnBase1(BaseEngine baseEngine, PlayerUnit playerUnit, Vector2 location) {

        Vector2 rot = new Vector2();
        rot.copy(location);
        rot.x = -rot.x;
        rot.y = -rot.y;
        rot.setNormalized();

        Vector2 perp = new Vector2();
        Orientation.getPerpendicular(perp, rot);

        ZugNest zugNest = UnitPool.zugNests.fetchMemory();
        zugNest.configure(playerUnit);
        zugNest.battleNode.coords.pos.copy(location);
        zugNest.battleNode.coords.pos.translate(perp.x, perp.y);
        baseEngine.addUnit(zugNest);

        zugNest = UnitPool.zugNests.fetchMemory();
        zugNest.configure(playerUnit);
        zugNest.battleNode.coords.pos.copy(location);
        zugNest.battleNode.coords.pos.translate(-perp.x, -perp.y);
        baseEngine.addUnit(zugNest);

        for (int i = -14; i < 14; i++) {
            PufferZug zug = new PufferZug(); // new Cannon(gamer);;
            zug.configure(playerUnit);
            zug.battleNode.coords.pos.copy(location);
            zug.battleNode.coords.pos.translate((i) * perp.x + 2.5 * rot.x, ((i)) * perp.y + 2.5 * rot.y);
            baseEngine.addUnit(zug);

            zug = new PufferZug(); // new Cannon(gamer);;
            zug.configure(playerUnit);
            zug.battleNode.coords.pos.copy(location);
            zug.battleNode.coords.pos.translate(i * perp.x + 4 * rot.x, i * perp.y + 4 * rot.y);
            baseEngine.addUnit(zug);
        }

    }

    public boolean loadFromJson(final NoteworthyEngine noteworthyEngine, String json) throws JSONException {

        noteworthyEngine.addSystem(new CityWinLoseConditionSystem(game, noteworthyEngine.playerSystem));

        noteworthyEngine.gameTime = 0;

        PlayerUnit gamer0 = new PlayerUnit("conniech", 0);

        PlayerUnit gamer1 = new PlayerUnit("avilo", 1);

        noteworthyEngine.playerSystem.setCurrentPlayer(gamer0);
        noteworthyEngine.addUnit(gamer0);
        noteworthyEngine.addUnit(gamer1);

        spawnBase0(noteworthyEngine, gamer0, new Vector2(0, -9));
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
