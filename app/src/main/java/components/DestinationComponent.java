package components;

import game.androidgame2.Vector2;

/**
 * Created by eric on 11/2/14.
 */
public class DestinationComponent extends Component {
    public Vector2 dest = new Vector2();

    public void reset() {
        dest.zero();
    }
}
