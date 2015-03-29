package utils;

/**
 * Created by eric on 3/28/15.
 */
public class StringPtr implements JsonSerializable {
    public String v = null;

    @Override
    public String json() {
        return "\"" + v + "\"";
    }
}
