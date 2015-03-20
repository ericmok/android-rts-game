package structure;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by eric on 10/31/14.
 */
public class LevelFileLoader {

    Context context;

    public LevelFileLoader(Context context) {
        this.context = context;
    }

    public String jsonFromFile(String filename) throws IOException {
//        try {
            InputStream is = this.context.getResources().getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, "UTF-8");
            Log.i("LEVEL JSON", json);

//            try {
//                EngineLoader.load(engine, "self", json);
//            }
//            catch (JSONException e) {
//                e.printStackTrace();
//            }

            return json;

//        } catch (IOException e) {
//            Log.e("JSON", e.getMessage());
//            e.printStackTrace();
//        }
    }
}
