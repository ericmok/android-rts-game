package utils;

import utils.JsonSerializable;

/**
 * Created by eric on 3/7/15.
 */
public class IntegerPtr implements JsonSerializable {
    public int v = 0;

    public IntegerPtr() {
    }

    @Override
    public String json() {
        return Integer.toString(v);
    }
}
