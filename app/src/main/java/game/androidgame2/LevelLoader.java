package game.androidgame2;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import components.Entity;
import components.GameEntities;
import components.PlayerComponent;
import components.WorldComponent;
import components.Engine;

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

                JSONObject playersObj = jObj.getJSONObject("players");

                JSONObject meObj = playersObj.getJSONObject("me");

                JSONArray troopArr = meObj.getJSONArray("troop");

                for (int i = 0; i < troopArr.length(); i++) {
                    JSONObject jEntity = troopArr.getJSONObject(i);

                    Entity troop = GameEntities.buildTroop(
                            PlayerComponent.TAG_PLAYER_OWNED, Entity.TAG_FOLLOWER);
                    engine.addEntity(troop);

                    WorldComponent pc =
                            ((WorldComponent)troop.cData.get(WorldComponent.class));
                    pc.set(jEntity.getDouble("x"), jEntity.getDouble("y"));
                }

                JSONObject enemyObject = playersObj.getJSONObject("enemy");
                troopArr = enemyObject.getJSONArray("troop");

                for (int i = 0; i < troopArr.length(); i++) {
                    JSONObject jEntity = troopArr.getJSONObject(i);

                    Entity troop = GameEntities.buildTroop(
                            PlayerComponent.TAG_ENEMY_OWNED, Entity.TAG_FOLLOWER);
                    engine.addEntity(troop);

                    WorldComponent pc =
                            ((WorldComponent)troop.cData.get(WorldComponent.class));
                    pc.set(jEntity.getDouble("x"), jEntity.getDouble("y"));
                }
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
