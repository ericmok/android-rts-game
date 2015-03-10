package noteworthyframework;

import utils.Orientation;
import utils.Vector2;

/**
 * Created by eric on 3/7/15.
 */
public class Coords {
    public Vector2 pos = new Vector2();
    public Orientation rot = new Orientation();

    public Coords set(double x, double y) {
        pos.x = x;
        pos.y = y;
        return this;
    }

    public void reset() {
        pos.zero();
        rot.setDegrees(0);
    }
}
