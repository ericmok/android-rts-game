package processors;

import components.SelectionComponent;
import components.Entity;
import utils.VoidFunc;

/**
 * Created by eric on 11/3/14.
 */
public class Functional {
    public static final VoidFunc<Entity> FN_NOOP = new VoidFunc<Entity>() {
        public void apply(Entity element) {
            // Do nothing
        }
    };
}
