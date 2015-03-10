package noteworthyframework;

import org.json.JSONException;

/**
 * Created by eric on 3/10/15.
 */
public interface EngineDataLoader {
    public boolean loadFromJson(BaseEngine baseEngine, String json) throws JSONException;
}
