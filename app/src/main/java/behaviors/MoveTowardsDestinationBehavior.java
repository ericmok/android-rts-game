package behaviors;

import model.Entity;

/**
 * Created by eric on 3/5/15.
 * Mixin Space for MoveTowardsDestinationFunction
 *
 * Components: [world, velocity, destination]
 */
public class MoveTowardsDestinationBehavior extends Component {

    public WorldComponent worldComponent = null;
    public VelocityComponent velocityComponent = null;
    public DestinationComponent destinationComponent = null;

    // TODO: Consider using string keys for entity internal variable lookup
    public static final Class[] COMPONENTS = {
      WorldComponent.class, VelocityComponent.class, DestinationComponent.class
    };

    public static void mixin(Entity entity) {

        // TODO: Instead of putting this in labels, put this in a behaviors list
        entity.labels().add(Behaviors.BEHAVIOR_MOVES_TOWARD_DESTINATION);

        MoveTowardsDestinationBehavior behaviorToAdd =
                new MoveTowardsDestinationBehavior();
        entity.cData.put(MoveTowardsDestinationBehavior.class, behaviorToAdd);

        ensureRequirements(entity, behaviorToAdd);

        behaviorToAdd.worldComponent =
                (WorldComponent)entity.cData.get(WorldComponent.class);
        behaviorToAdd.velocityComponent =
                (VelocityComponent)entity.cData.get(VelocityComponent.class);
        behaviorToAdd.destinationComponent =
                (DestinationComponent)entity.cData.get(DestinationComponent.class);

    }

    // For isolatable mixins, we should have used strings instead of component classes
    // since the entities component store uses component classes for the keys
    //public static void mixin(Entity entity,
    //                         WorldComponent wc,
    //                         VelocityComponent vc,
    //                         DestinationComponent dc) {
    //}

    /**
     * For each component used by this behavior, check to see if component exists
     * and add if not.
     *
     * @param entity
     * @param behavior
     */
    public static void ensureRequirements(Entity entity, MoveTowardsDestinationBehavior behavior) {

        for (int i = 0; i < COMPONENTS.length; i++) {

            if (entity.cData.get(COMPONENTS[i]) == null) {
                try {

                    Object componentToAdd = COMPONENTS[i].newInstance();
                    entity.cData.put(COMPONENTS[i], (Component)componentToAdd);

                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
