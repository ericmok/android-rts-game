package behaviors;

import model.Entity;

/**
 * Created by eric on 3/5/15.
 * Under consideration: Mixin Spaces
 */
public abstract class Behavior {

    //public void mixin(Entity entity);

    // If we want to isolate mixins, we might do something with this
    //public void mixin(Entity entity, Component[] fields);

    // TODO: Consider using string keys for entity internal variable lookup
    public abstract Class[] getComponents();

    /**
     * For each component used by this behavior, check to see if component exists
     * and add if not.
     *
     * @param entity
     */
    public void ensureRequirements(Entity entity) {

        Class[] components = getComponents();

        for (int i = 0; i < components.length; i++) {

            if (entity.cData.get(components[i]) == null) {
                try {

                    Object componentToAdd = components[i].newInstance();
                    entity.cData.put(components[i], (Component)componentToAdd);

                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
