package game.androidgame2;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import components.Entity;
import components.GameEntities;
import components.WorldComponent;
import components.Engine;
import components.Player;

/**
 * Created by eric on 10/31/14.
 */
public class LevelLoader {

    Context context;

    public LevelLoader(Context context) {
        this.context = context;
    }

    public void load(Engine engine, String filename) {
        try {
            InputStream is = this.context.getResources().getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");
            Log.i("LEVEL JSON", json);
            try {
                JSONObject jObj = new JSONObject(json);

                engine.gameTime = jObj.getDouble("time");

                JSONArray playersArr = jObj.getJSONArray("players");

                for (int jp = 0; jp < playersArr.length(); jp++) {

                    JSONObject jPlayerObj = playersArr.getJSONObject(jp);

                    Player player = new Player(jPlayerObj.getString("name"));
                    player.team = jPlayerObj.getInt("team");

                    engine.addPlayer(player);

                    if (player.name.equals("self")) {
                        engine.currentPlayer = player;
                    } else {
                        // Allied or enemy team
                    }

                    JSONArray troopArr = jPlayerObj.getJSONArray("troop");

                    for (int i = 0; i < troopArr.length(); i++) {
                        JSONObject jEntity = troopArr.getJSONObject(i);

                        Entity troop = GameEntities.buildTroop();

                        WorldComponent pc =
                                ((WorldComponent) troop.cData.get(WorldComponent.class));
                        pc.set(jEntity.getDouble("x"), jEntity.getDouble("y"));

                        player.denorms.addDenormalizable(troop);
                    }
                }
//
//                JSONObject enemyObject = playersObj.getJSONObject("enemy");
//                troopArr = enemyObject.getJSONArray("troop");
//
//                for (int i = 0; i < troopArr.length(); i++) {
//                    JSONObject jEntity = troopArr.getJSONObject(i);
//
//                    Entity troop = GameEntities.buildTroop(
//                            PlayerComponent.TAG_ENEMY_OWNED, Entity.TAG_FOLLOWER);
//                    engine.players.get(1).denorms.addDenormalizable(troop);
//                    //engine.addEntity(troop);
//
//                    WorldComponent pc =
//                            ((WorldComponent)troop.cData.get(WorldComponent.class));
//                    pc.set(jEntity.getDouble("x"), jEntity.getDouble("y"));
//                }
            }
            catch (JSONException e) {
                Log.e("JSON", e.getMessage());
                e.printStackTrace();
            }

        } catch (IOException e) {
            Log.e("JSON", e.getMessage());
            e.printStackTrace();
        }
    }
}
