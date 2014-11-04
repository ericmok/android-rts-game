package components;

import game.androidgame2.Vector2;
import utils.Orientation;

/**
 * Created by eric on 10/30/14.
 */
public class WorldComponent extends Component{
    public Vector2 pos = new Vector2();
    public Orientation rot = new Orientation();

    public WorldComponent set(double x, double y) {
        pos.x = x;
        pos.y = y;
        return this;
    }

    public void reset() {
        pos.zero();
    }
}
