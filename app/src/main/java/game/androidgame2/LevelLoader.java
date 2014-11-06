package game.androidgame2;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import components.EngineLoader;
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
                EngineLoader.load(engine, "self", json);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            Log.e("JSON", e.getMessage());
            e.printStackTrace();
        }
    }
}
