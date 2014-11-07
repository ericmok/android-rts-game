package components;

import utils.Vector2;

/**
 * Created by eric on 11/2/14.
 */
public class DestinationComponent extends Component {
    /**
     * A point to move to. This will never be null. Check the hasDestination flag instead.
     */
    public Vector2 dest = new Vector2();

    /**
     * Maybe there is no destination
     */
    public boolean hasDestination = false;

    public void reset() {
        dest.zero();
        hasDestination = false;
    }
}
