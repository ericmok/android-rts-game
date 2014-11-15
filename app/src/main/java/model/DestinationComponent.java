package model;

import utils.Vector2;
import utils.VoidFunc;

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

    public static final VoidFunc<Entity> DEFAULT_DESTINATION_REACHED = new VoidFunc<Entity>() {
        @Override
        public void apply(Entity element) {
            // NOTHING
        }
    };

    public VoidFunc<Entity> onDestinationReached = DEFAULT_DESTINATION_REACHED;

    public void reset() {
        dest.zero();
        hasDestination = false;
    }
}
