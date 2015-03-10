package noteworthyengine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utils.Vector2;

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

                //troopy.fields.put("gamer", player);
                BattleNode battleNode = (BattleNode)troopy.node(BattleNode._NAME);
                battleNode.gamer = player;

                Coords coords = (Coords)troopy.field("coords");
                coords.pos.set(jEntity.getDouble("x"), jEntity.getDouble("y"));
                coords.rot.setDegrees(jEntity.getDouble("r"));

                FieldNode fieldNode = (FieldNode)troopy.node(FieldNode._NAME);
                //fieldNode.isFieldControl.v = 0;
                //fieldNode._movementNode = (MovementNode)troopy.node(MovementNode._NAME);

                engineDataPack.addUnit(troopy);
                //engineDataPack.movementNodes.items.add((MovementNode)troopy.node(MovementNode._NAME));
                //engineDataPack.addUnit(troopy);

                // Debug
                //FieldUnit fieldUnit = new FieldUnit();
                ArrowCommand arrowCommand = new ArrowCommand();
                engineDataPack.addUnit(arrowCommand);

                Coords arrowCoords = (Coords)arrowCommand.field("coords");
                arrowCoords.pos.copy(coords.pos);
                arrowCoords.pos.translate(Math.ceil(30 * Math.random() - 15), Math.ceil(30 * Math.random() - 15));
                arrowCoords.rot.setDirection(2 * Math.random() - 1, 2 * Math.random() - 1);

                //Vector2 fieldDirection = (Vector2)arrowCommand.field("fieldDirection");
                //fieldDirection.set((2 * Math.random()) - 1, (2 * Math.random()) - 1);

//                fieldNode = (FieldNode) fieldUnit.node(FieldNode._NAME);
//                fieldNode._arrowCommand = fieldUnit.arrowCommand;
//
//                fieldUnit.arrowCommand.position.copy(coords.pos);
//                fieldUnit.arrowCommand.direction.set(0, 1);

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

        engineDataPack.flushQueues();

        return true;
    }
}
