package components;

import game.androidgame2.Vector2;

/**
 * Created by eric on 10/30/14.
 */
public class PositionComponent extends Component{
    public Vector2 pos = new Vector2();

    public PositionComponent set(double x, double y) {
        pos.x = x;
        pos.y = y;
        return this;
    }

    public void reset() {
        pos.zero();
    }
}
