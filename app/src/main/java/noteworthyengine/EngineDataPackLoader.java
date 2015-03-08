package noteworthyengine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import structure.LevelFileLoader;

/**
 * Created by eric on 3/6/15.
 */
public class EngineDataPackLoader {

    public static boolean loadFromJson(EngineDataPack engineDataPack, String json)
            throws JSONException {

        JSONObject jObj = new JSONObject(json);

        engineDataPack.gameTime = jObj.getDouble("time");

        JSONArray playersArr = jObj.getJSONArray("players");

        for (int jp = 0; jp < playersArr.length(); jp++) {

            JSONObject jPlayerObj = playersArr.getJSONObject(jp);

            Gamer player = new Gamer(jPlayerObj.getString("name"));
            player.team = jPlayerObj.getInt("team");

            engineDataPack.addGamer(player);

            if (player.name.equals("self")) {
                engineDataPack.currentGamer = player;
            } else {
                // Allied or enemy team
            }

            JSONArray troopArr = jPlayerObj.getJSONArray("troop");

            for (int i = 0; i < troopArr.length(); i++) {
                JSONObject jEntity = troopArr.getJSONObject(i);

                //Unit troop = GameEntities.troopsMemoryPool.fetchMemory();
                Unit troopy = UnitPool.troopyMemoryPool.fetchMemory();

                Coords coords = (Coords)troopy.field("coords");
                coords.pos.set(jEntity.getDouble("x"), jEntity.getDouble("y"));
                coords.rot.setDegrees(jEntity.getDouble("r"));

                engineDataPack.units.add(troopy);
                //engineDataPack.unitsByNodes.addDenormalizable(troopy);
                //player.filters.addDenormalizable(troopy);
//
//                pc.set(jEntity.getDouble("x"), jEntity.getDouble("y"));
//                pc.rot.setDegrees(jEntity.getDouble("r"));
//
//                // Destination deserialization
//                JSONObject jDest = jEntity.getJSONObject("dest");
//                if (jDest != null) {
//                    DestinationComponent dc = (DestinationComponent) troop.cData.get(DestinationComponent.class);
//                    double destX = jDest.getDouble("x");
//                    double destY = jDest.getDouble("y");
//                    dc.dest.set(destX, destY);
//                    dc.hasDestination = true;
//                }
//
//                player.denorms.addDenormalizable(troop);
            }
        }

        return true;
    }
}
