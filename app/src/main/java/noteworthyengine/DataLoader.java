package noteworthyengine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import noteworthyengine.units.Archer;
import noteworthyengine.units.Cannon;
import noteworthyengine.units.City;
import noteworthyengine.units.Mine;
import noteworthyengine.units.TimelineCommand;
import noteworthyframework.Coords;
import noteworthyframework.BaseEngine;
import noteworthyframework.EngineDataLoader;
import noteworthyframework.Gamer;
import noteworthyframework.GamerPtr;
import noteworthyframework.Unit;
import noteworthyframework.UnitPool;

/**
 * Created by eric on 3/6/15.
 */
public class DataLoader implements EngineDataLoader {

    public boolean loadFromJson(BaseEngine baseEngine, String json) throws JSONException {

        JSONObject jObj = new JSONObject(json);

        baseEngine.gameTime = jObj.getDouble("time");

        JSONArray playersArr = jObj.getJSONArray("players");

        for (int jp = 0; jp < playersArr.length(); jp++) {

            JSONObject jPlayerObj = playersArr.getJSONObject(jp);

            Gamer player = new Gamer(jPlayerObj.getString("name"));
            player.team = jPlayerObj.getInt("team");

            baseEngine.addGamer(player);

            if (player.name.equals("self")) {
                baseEngine.currentGamer = player;
            } else {
                // Allied or enemy team
            }

            City city = new City(player);
            city.battleNode.coords.pos.set(Math.random() * 30 - 15, Math.random() * 30 - 15);
            baseEngine.addUnit(city);

            JSONArray troopArr = jPlayerObj.getJSONArray("troop");

            for (int i = 0; i < troopArr.length(); i++) {

                JSONObject jEntity = troopArr.getJSONObject(i);

                Unit troopy = UnitPool.troopyMemoryPool.fetchMemory();

                GamerPtr gamerPtr = (GamerPtr)troopy.field("gamer");
                gamerPtr.v = player;

                Coords coords = (Coords)troopy.field("coords");
                coords.pos.set(jEntity.getDouble("x"), jEntity.getDouble("y"));
                coords.rot.setDegrees(jEntity.getDouble("r"));

                baseEngine.addUnit(troopy);

                for (int k = 0; k < 3; k++) {
                    //Unit troop = GameEntities.troopsMemoryPool.fetchMemory();
                    if (player.name.equals("evilempire")) {
                        troopy = new Archer(player);
                    }
                    if (player.name.equals("enemy")) {
                        //troopy = new Mine(player);
                        troopy = UnitPool.troopyMemoryPool.fetchMemory();
                    }
                    else {
                        troopy = UnitPool.troopyMemoryPool.fetchMemory();
                    }

                    //GamerPtr gamer = (GamerPtr)troopy.fields.put("gamer", player);
                    //gamer.v = player;
                    gamerPtr = (GamerPtr) troopy.field("gamer");
                    gamerPtr.v = player;

                    coords = (Coords) troopy.field("coords");
                    coords.pos.set(jEntity.getDouble("x") + Math.random(), jEntity.getDouble("y") + Math.random());
                    coords.rot.setDegrees(jEntity.getDouble("r"));

                    baseEngine.addUnit(troopy);
                }

                if (Math.random() > 0.2) {
                    Archer archer = new Archer(player);
                    gamerPtr = (GamerPtr) archer.field("gamer");
                    gamerPtr.v = player;
                    archer.movementNode.coords.pos.set(jEntity.getDouble("x") + Math.random(), jEntity.getDouble("y") + Math.random());
                    baseEngine.addUnit(archer);
                }

                if (Math.random() > 0.8) {
                    Mine mine = new Mine(player);
                    //mine.battleNode.coords.pos.set(Math.random() * 20 - 10, Math.random() * 20 - 10);
                    mine.battleNode.coords.pos.set(jEntity.getDouble("x") + 4 * Math.random() - 2,
                            jEntity.getDouble("y") + 4 * Math.random() - 2);
                    baseEngine.addUnit(mine);
                }
                if (Math.random() > 0.8) {
                    Cannon cannon = new Cannon(player);
                    cannon .movementNode.coords.pos.set(jEntity.getDouble("x") + Math.random(), jEntity.getDouble("y") + Math.random());
                    baseEngine.addUnit(cannon);
                }

                TimelineCommand timelineCommand = new TimelineCommand();
                timelineCommand.timelineNode.gamerPtr.v = player;
                timelineCommand.timelineNode.coords.pos.set(40 * Math.random() - 20, 40 * Math.random() - 20);
                timelineCommand.timelineNode.coords.rot.set(-timelineCommand.timelineNode.coords.pos.x,
                        -timelineCommand.timelineNode.coords.pos.y);
                timelineCommand.timelineNode.frameTime.v = i * 4;
                baseEngine.addUnit(timelineCommand);
            }
        }

        baseEngine.flushQueues();

        return true;
    }
}
