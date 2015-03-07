package noteworthyengine;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by eric on 3/6/15.
 */
public class EngineDataPackLoader {

    public static boolean loadFromJson(EngineDataPack engineDataPack, String mainPlayerName, String json) throws JSONException {

//        JSONObject jObj = new JSONObject(json);
//
//        this.gameTime = jObj.getDouble("time");
//
//        JSONArray playersArr = jObj.getJSONArray("players");
//
//        for (int jp = 0; jp < playersArr.length(); jp++) {
//
//            JSONObject jPlayerObj = playersArr.getJSONObject(jp);
//
//            Gamer player = new Gamer(jPlayerObj.getString("name"));
//            player.team = jPlayerObj.getInt("team");
//
//            engine.addPlayer(player);
//
//            if (player.name.equals(mainPlayerName)) {
//                engine.currentPlayer = player;
//            } else {
//                // Allied or enemy team
//            }
//
//            JSONArray troopArr = jPlayerObj.getJSONArray("troop");
//
//            for (int i = 0; i < troopArr.length(); i++) {
//                JSONObject jEntity = troopArr.getJSONObject(i);
//
//                Unit troop = GameEntities.troopsMemoryPool.fetchMemory();
//
//                WorldComponent pc =
//                        ((WorldComponent) troop.cData.get(WorldComponent.class));
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
//            }
//        }
//
        return true;
    }
}
