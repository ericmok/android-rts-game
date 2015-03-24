package noteworthyengine;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import noteworthyframework.Coords;
import noteworthyframework.BaseEngine;
import noteworthyframework.EngineDataLoader;
import noteworthyframework.Gamer;
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

            JSONArray troopArr = jPlayerObj.getJSONArray("troop");

            for (int i = 0; i < troopArr.length(); i++) {

                JSONObject jEntity = troopArr.getJSONObject(i);

                //Unit troop = GameEntities.troopsMemoryPool.fetchMemory();
                Unit troopy = UnitPool.troopyMemoryPool.fetchMemory();

                //GamerPtr gamer = (GamerPtr)troopy.fields.put("gamer", player);
                //gamer.v = player;
                GamerPtr gamerPtr = (GamerPtr)troopy.field("gamer");
                gamerPtr.v = player;

                Coords coords = (Coords)troopy.field("coords");
                coords.pos.set(jEntity.getDouble("x"), jEntity.getDouble("y"));
                coords.rot.setDegrees(jEntity.getDouble("r"));

                FieldNode fieldNode = (FieldNode)troopy.node(FieldNode._NAME);
                //fieldNode.isFieldControl.v = 0;
                //fieldNode._movementNode = (MovementNode)troopy.node(MovementNode._NAME);

                baseEngine.addUnit(troopy);
                //engineData.movementNodes.items.add((MovementNode)troopy.node(MovementNode._NAME));

                for (int k = 0; k < 1; k++) {
                    //Unit troop = GameEntities.troopsMemoryPool.fetchMemory();
                    troopy = UnitPool.troopyMemoryPool.fetchMemory();

                    //GamerPtr gamer = (GamerPtr)troopy.fields.put("gamer", player);
                    //gamer.v = player;
                    gamerPtr = (GamerPtr) troopy.field("gamer");
                    gamerPtr.v = player;

                    coords = (Coords) troopy.field("coords");
                    coords.pos.set(jEntity.getDouble("x") + Math.random(), jEntity.getDouble("y") + Math.random());
                    coords.rot.setDegrees(jEntity.getDouble("r"));

                    fieldNode = (FieldNode) troopy.node(FieldNode._NAME);
                    //fieldNode.isFieldControl.v = 0;
                    //fieldNode._movementNode = (MovementNode)troopy.node(MovementNode._NAME);

                    baseEngine.addUnit(troopy);
                    //engineData.movementNodes.items.add((MovementNode)troopy.node(MovementNode._NAME));
                }

                if (Math.random() > 0.2) {
                    Archer archer = new Archer(player);
                    gamerPtr = (GamerPtr) archer.field("gamer");
                    gamerPtr.v = player;
                    archer.movementNode.coords.pos.set(jEntity.getDouble("x") + Math.random(), jEntity.getDouble("y") + Math.random());
                    baseEngine.addUnit(archer);
                }
                //ArrowCommand arrowCommand = new ArrowCommand();
                //double randX = Math.ceil(30 * Math.random() - 15);
                //double randY = Math.ceil(30 * Math.random() - 15);

                //arrowCommand.set(player,
                //        randX,
                //        randY,
                //        -randX + 0.3 * ((Math.random()) - 0.5),
                //        -randY + 0.3 * ((Math.random()) - 0.5));

                //baseEngine.addUnit(arrowCommand);

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

                //Vector2 fieldDirection = (Vector2)arrowCommand.field("fieldDirection");
                //fieldDirection.set((2 * Math.random()) - 1, (2 * Math.random()) - 1);

//                fieldNode = (FieldNode) fieldUnit.node(FieldNode._NAME);
//                fieldNode._arrowCommand = fieldUnit.arrowCommand;
//
//                fieldUnit.arrowCommand.position.copy(coords.pos);
//                fieldUnit.arrowCommand.direction.set(0, 1);

                //engineData.unitsByNodes.addDenormalizable(troopy);
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

        baseEngine.flushQueues();

        return true;
    }
}
