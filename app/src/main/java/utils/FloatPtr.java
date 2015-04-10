package utils;

import utils.JsonSerializable;

/**
 * Created by eric on 3/8/15.
 */
public class FloatPtr implements JsonSerializable {
    public float v = 0;

    @Override
    public String json() {
        return Float.toString(v);
    }
}
