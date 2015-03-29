package noteworthyframework;

import utils.JsonSerializable;

/**
 * Created by eric on 3/7/15.
 */
public class DoublePtr implements JsonSerializable {
    public double v = 0;

    public String json() {
        return Double.toString(v);
    }
}
